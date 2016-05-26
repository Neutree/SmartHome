package com.neucrack.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import com.neucrack.communication.ToDevices;
import com.neucrack.communication.ToUser;
import com.neucrack.entity.DeviceDoor;
import com.neucrack.entity.DeviceSensor;
import com.neucrack.entity.DeviceSwitch;
import com.neucrack.entity.User;
import com.neucrack.tool.CRC;
import com.neucrack.tool.Date_TimeStamp;
import com.neucrack.tool.Session;
import com.neucrack.tool.StringRelated;

public class ServerToUserThread extends Thread {

	ArrayList<ServerToDeviceThead> mSocketList=null;
	private Socket mSocket = null;
	private DataInputStream mInStream=null;
	private DataOutputStream mOutStream=null;
	private ToUser mToUser = null;
	
	private byte[] dataToRead = new byte[2048];
	
	
	public ServerToUserThread(ArrayList<ServerToDeviceThead> mScocketList ,Socket socket) {
		mSocketList = mScocketList;
		mSocket = socket;
	}
	
	
	@Override
	public void run() {
		super.run();
		//记录时间戳
		long startTime = Date_TimeStamp.timeStamp();
		// 获取输入流，并读取客户端信息
		if(!getIn_Out_stream())
		{
			System.out.println("获取输入输出流错误");
			Close();
			return;
		}
		User user = new User();
		try {
			mSocket.setSoTimeout(20000);//设置超时为20s
			int size = mInStream.read(dataToRead);
			if(size>0){
				if(!mToUser.VerifyFrame(dataToRead)){
					System.out.println("消息帧不完整!!!");
					Close();
					return;
				}
				byte[] sessionBytes = new byte[32];
				System.arraycopy(dataToRead, 5, sessionBytes, 0, 32);
				String sessionString = StringRelated.SessionTokenBytes32ToString(sessionBytes);
				user.setSession(sessionString);
				if(!mToUser.CheckIfSignedIn(user.getSession())){//未登录
					
					if(dataToRead[4] != 0x01 || dataToRead[2] != 0x00 || (dataToRead[3]!=0x11 && dataToRead[3]!=0x12)){//不是登录或者注册请求
						System.out.println("还未登录，请求被拒绝！！！");
						Close();
						return;
					}
					byte[] userName = new byte[11];
					System.arraycopy(dataToRead, 45, userName, 0, 11);
					String name = StringRelated.BytesToString(userName,11);//获取用户名
					byte[] userPasswd = new byte[16];
					System.arraycopy(dataToRead, 56, userPasswd, 0, 16);
					String passwd = StringRelated.MD5_32_BytesToString(userPasswd);//获取密码
					user.setmName(name);
					user.setmPasswd(passwd);
					
									
					if(dataToRead[2] == 0x00 && dataToRead[3]==0x12){
						//处理注册请求
						byte[] userNickName = new byte[10];
						System.arraycopy(dataToRead, 56, userNickName, 0, 11);
						user.setmNickName(StringRelated.BytesToString(userNickName,10));//获取昵称
						
						int signInResult =mToUser.DealSignUp(user); 
						if(signInResult<=0){
							System.out.println("注册出错,错误代码："+signInResult);
							Close();
							return;
						}
					}
					else{
						//处理登录请求
						int signInResult =mToUser.DealSignIn(user); 
						if(signInResult <= 0){
							System.out.println("登录出错,错误代码："+signInResult);
							Close();
							return;
						}
						System.out.println("用户登录成功，用户名："+user.getmName());
					}
				}
				else{//已经登录
					user.setmName((String)Session.getAttribute(user.getSession()));//根据session获得用户名
					
					String device = StringRelated.Byte6ToMac(dataToRead,45);//要通信的设备号
					//从在线设备列表中获得该设备号的设备通信的对象
					ToDevices toDevice = null;
					for (int i = 0;i<mSocketList.size();++i) {
						if(!mSocketList.get(i).IsAlive())//已经关闭连接了，可以移除
							mSocketList.remove(i);
						else{//连接还正常（设备在线）//判断是否与要通信的设备名相同
							if(mSocketList.get(i).mSignInfo.device.equals(device)){
								toDevice = mSocketList.get(i).mToDevices;
								break;
							}
						}
					}
					if(toDevice==null){
						System.out.println("该设备没有登录,无法连接到设备！！！");
						Close();
						return;
					}
					if(dataToRead[4] == 0x01){//控制请求
						int type =  ((int)dataToRead[2]<<8&0xff00) | ((int)dataToRead[3]&0xff);
						long switchName = (dataToRead[51]<<24&0xff000000)|(dataToRead[52]<<16&0xff0000)|(dataToRead[53]<<8&0xff00)|(dataToRead[54]&0xff);
						switch (type) {
						case DeviceSwitch.DEVICE_TYPE:
							int timeout = mSocket.getSoTimeout();
							mSocket.setSoTimeout(30000);
							if(!mToUser.ControlSwitch(toDevice,user,device,switchName,(dataToRead[55]==1)))
								System.out.println("控制开关失败");
							else {
								System.out.println("控制开关成功"+dataToRead[55]);
							}
							mSocket.setSoTimeout(timeout);
							break;
						case DeviceDoor.DEVICE_TYPE:
							System.out.println("控制门请使用开关命令");
							break;
						default:
							System.out.println("未知设备控制请求");
							break;
						}
					}
					else if(dataToRead[4] == 0x03){//询问请求
						int type =  ((int)dataToRead[2]<<8&0xff00) | ((int)dataToRead[3]&0xff); 
						long switchName = (dataToRead[51]<<24&0xff000000)|(dataToRead[52]<<16&0xff0000)|(dataToRead[53]<<8&0xff00)|(dataToRead[54]&0xff);
						switch (type) {
						case DeviceSwitch.DEVICE_TYPE:
							if(!mToUser.QuerySwitch(toDevice,user,device,switchName))
								System.out.println("查询开关状态失败");
							break;
						case DeviceSensor.DEVICE_TYPE:
							if(!mToUser.QuerySensor(toDevice,user,device,switchName))
								System.out.println("查询传感器状态失败");
							else {
								System.out.println("查询传感器状态成功");
							}
							break;
						case DeviceDoor.DEVICE_TYPE:
							System.out.println("请使用查询开关的命令代替");
							break;
						default:
							System.out.println("未知设备控制请求");
							break;
						}
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (SocketTimeoutException e) {
			System.out.println("超时，接收请求消息失败");
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			mOutStream.flush();
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Close();
	}

		
	private boolean getIn_Out_stream(){
		try {
			mInStream = new DataInputStream(mSocket.getInputStream());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		// 获取输出流,响应客户端请求
		try {
			mOutStream = new DataOutputStream(mSocket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		mToUser = new ToUser(mInStream,mOutStream);
		return true;
	}
	
	private void Close() {
		try {
			if(mOutStream!=null)
				mOutStream.close();
			if(mInStream!=null)
				mInStream.close();
			if (mSocket != null)
				mSocket.close();
		} catch (IOException e) {
			System.out.println("Abnormal , Socket : 关闭socket资源异常 ：");
			e.printStackTrace();
		}
	}


}
