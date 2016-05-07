package com.neucrack.server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Time;

import org.omg.CORBA.DATA_CONVERSION;

import com.neucrack.communication.ToDevices;
import com.neucrack.devices.*;
import com.neucrack.tool.CRC;
import com.neucrack.tool.Date_TimeStamp;

/*
 * 服务器端线程处理类
 */
public class ServerThead extends Thread {

	protected Socket socket = null;
	protected String recvData = null;
	protected DataInputStream mInStream=null;
	protected DataOutputStream mOutStream=null;
	
	public SignInfo mSignInfo = new SignInfo();
	
	public ToDevices mToDevices;
	
	private boolean mIsSginIn=false;

	public ServerThead(Socket socket) {
		this.socket = socket;
	}

	// 线程执行的操作，响应客户端的请求
	public void run() {
		//记录时间戳
		long startTime = Date_TimeStamp.timeStamp();
		// 获取输入流，并读取客户端信息
		if(!getIn_Out_stream())
		{
			System.out.println("获取输入输出流错误");
			Close();
			return;
		}
		
		long keepAliveTime = Date_TimeStamp.timeStamp();
		while(true){
			
			//登录检测
			if(!mIsSginIn){
				try {
					socket.setSoTimeout(20000);
				} catch (SocketException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//等待登录请求
				if(mToDevices.WaitSignIn(mSignInfo)){
					System.out.println("设备登录请求到达，设备号："+mSignInfo.device+"用户名："+mSignInfo.userName);
					//用户和设备验证
					if(!SignInVerify()){//验证失败
						System.out.println("登录验证失败！！！！");
						break;
					}
					System.out.println("设备登录成功!!");
					mIsSginIn = true;
					try {
						socket.setSoTimeout(5000);
					} catch (SocketException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					continue;
				}
				if(Date_TimeStamp.timeStamp()-startTime>20000){//20s还没有登录则关闭连接
					System.out.println("等待登录请求超时");
					Close();
				}
				continue;
			}
			
			//心跳保持包,5分钟一次
			if(Date_TimeStamp.timeStamp()-keepAliveTime>=300000){
				keepAliveTime = Date_TimeStamp.timeStamp();
				if(!mToDevices.KeepAlive(mSignInfo.device)){
					System.out.println("心跳保持失败");
					if(!mToDevices.KeepAlive(mSignInfo.device)){
						System.out.println("第二次心跳保持失败!失去连接！！！！！！");
						Close();
						break;
					}
				}
				System.out.println("链路保持成功！");
			}
			
			
		}
	}



	private boolean getIn_Out_stream(){
		try {
			mInStream = new DataInputStream(socket.getInputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// 获取输出流,响应客户端请求
		try {
			mOutStream = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		mToDevices = new ToDevices(mInStream,mOutStream);
		return true;
	}

	
	//验证设备和用户
	private boolean SignInVerify() {
		//查找数据库有无数据以及设备对应是否正确
		return true;
	}
	



	protected void Close() {
		try {
			if(mOutStream!=null)
				mOutStream.close();
			if(mInStream!=null)
				mInStream.close();
			if (socket != null)
				socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Abnormal , Socket : 关闭socket资源异常 ：");
			e.printStackTrace();
		}
	}

	 public static byte[] charToByte(char[] c) {
		 	byte[] b=new byte[c.length];
		 	for(int i=0;i<c.length;++i){
		 		b[i]=(byte) (c[i]&0x00ff);
		 	}
	        return b;
	    }


	 public static void PrintBytes(byte[] c,int size){
		 for(int i=0;i<size;++i)
				System.out.print(Integer.toHexString(c[i]&0xff)+" ");
			System.out.println();
	 }

}
