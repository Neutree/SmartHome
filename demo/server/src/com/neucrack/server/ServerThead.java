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
import java.sql.Time;

import org.omg.CORBA.DATA_CONVERSION;

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
	
	public byte mUserName[]= new byte[11];
	public byte[] mDeviceNumber=new byte[6];
	public byte[] mUserPasswd  = new byte[16];
	public byte mDeviceType;
	
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
			close();
			return;
		}
		
		long keepAliveTime = Date_TimeStamp.timeStamp();
		while(true){
			
			//登录检测
			if(!mIsSginIn){
				//等待登录请求
				if(WaitForSignIn()){
					System.out.println("设备登录请求到达，设备号："+Byte6ToMac(mDeviceNumber)+"用户名："+BytesToString(mUserName,11));
					//用户和设备验证
					if(!SignInVerify()){//验证失败
						System.out.println("登录验证失败！！！！");
						break;
					}
					System.out.println("设备登录成功!!");
					mIsSginIn = true;
					break;
				}
				System.out.println("fail");
				if(Date_TimeStamp.timeStamp()-startTime>20000){//20s还没有登录则关闭连接
					System.out.println("等待登录请求超时");
					close();
				}
				continue;
			}
			
			//心跳保持包
			if(Date_TimeStamp.timeStamp()-keepAliveTime==5){
				keepAliveTime = Date_TimeStamp.timeStamp();
				if(!KeepAlive()){
					System.out.println("心跳保持失败");
					if(!KeepAlive()){
						System.out.println("第二次心跳保持失败!失去连接！！！！！！");
						break;
					}
				}
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
		return true;
	}
	//等待设备登录
	private boolean WaitForSignIn(){
		byte dataToRead[] = new byte[1024] ;
		try {
			int size = mInStream.read(dataToRead);
			if(size>0){
				//帧头
				if(((short)dataToRead[0]&0xff)!=0xab || ((short)dataToRead[1]&0xff)!=0xac)
					return false;
				int datalength = (short)dataToRead[17]<<8|dataToRead[18];
				//CRC校验
				int parity = CRC.CRC16Calculate(dataToRead,datalength+19);
				int parity2 = (dataToRead[19+datalength]<<8|dataToRead[20+datalength])&0xffff;
				if(parity != parity2)
					return false;
				//校验成功
				if(dataToRead[4]==1){
					System.arraycopy(dataToRead, 5, mDeviceNumber, 0, 6);
					System.arraycopy(dataToRead, 19, mUserName, 0, 11);
					System.arraycopy(dataToRead, 30, mUserPasswd, 0, 16);
					mDeviceType = (byte) dataToRead[46];
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	
	//验证设备和用户
	private boolean SignInVerify() {
		//查找数据库有无数据以及设备对应是否正确
		return true;
	}
	
	//链路保持
	private boolean KeepAlive(){
		return true;
	}

	public void deal() {

	}

	protected void write() {
//		try {
//			
//			pw.write(backMsg);
//			pw.flush();// 调用flush()方法将缓冲输出
//			System.out.println("Info ， Socket send :" + backMsg);
//		} catch (Exception e) {
//			System.out.println("Abnormal , Socket : 写入数据异常 ：");
//			e.printStackTrace();
//		}

	}

	protected void close() {
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
	 public static String Byte6ToMac(byte[] c) {
	        return Integer.toHexString(((int)c[0]&0xff))+":"+Integer.toHexString(((int)c[1]&0xff))+":"+Integer.toHexString(((int)c[2]&0xff))+":"
	        		+((short)c[3]&0xff)+":"+Integer.toHexString(((int)c[4]&0xff))+":"+Integer.toHexString(((int)c[5]&0xff)); 
	    }
	 public static String BytesToString(byte[] b,int size){
		 PrintBytes(b,size);
		 String str= "";
		 for(int i=0;i<size;++i){
			 str+=b[i]-'0';
		 }
		 return str;
	 }
	 public static void PrintBytes(byte[] c,int size){
		 for(int i=0;i<size;++i)
				System.out.print(Integer.toHexString(c[i]&0xff)+" ");
			System.out.println();
	 }

}
