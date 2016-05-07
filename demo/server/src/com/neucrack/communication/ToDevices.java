package com.neucrack.communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.neucrack.devices.*;
import com.neucrack.tool.CRC;

public class ToDevices {
	private DataInputStream mInStream=null;
	private DataOutputStream mOutStream=null;
	
	public ToDevices(DataInputStream inStream,DataOutputStream outStream){
		mInStream = inStream;
		mOutStream = outStream;
	}
	
	public boolean WaitSignIn(SignInfo signIn){
		byte[] mDeviceNumber = new byte[6];
		byte[] mUserName = new byte[11];
		byte[] mUserPasswd = new byte[16];
		
		byte dataToRead[] = new byte[512] ;
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
				if(dataToRead[4]!=1)//不是是请求登录
					return false;
				System.arraycopy(dataToRead, 5, mDeviceNumber, 0, 6);
				System.arraycopy(dataToRead, 19, mUserName, 0, 11);
				System.arraycopy(dataToRead, 30, mUserPasswd, 0, 16);
				signIn.device = Byte6ToMac(mDeviceNumber);
				signIn.userName = BytesToString(mUserName,11);
				signIn.userPasswd = MD5_32BytesToString(mUserPasswd);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	public boolean KeepAlive(String device){
		byte[] data = new byte[50];
		byte[] deviceNumber = MacToBytes(device);
		data[0] = (byte) 0xab;
		data[1] = (byte) 0xac;
		data[2] = (byte) 0x00;
		data[3] = (byte) 0x10;
		data[4] = (byte) 0x01;
		System.arraycopy(deviceNumber, 0, data, 5, 6);
		data[17] = (byte) 0x00;
		data[18] = (byte) 0x01;
		data[19] = (byte) 0x01;
		long crc16 = CRC.CRC16Calculate(data, 20);
		data[20] = (byte) (crc16>>8&0xff);
		data[21] = (byte) (crc16&0xff);
		try {
			mOutStream.write(data, 0, 22);;
			int size = mInStream.read(data);
			if(size>0){
				if(!VerifyFrame(data))
					return false;
				//校验成功
				if(data[4]!=2)//不是是响应链路保持
					return false;
				if(data[19]==1){//设备
					
				}
				else if(data[20]==2){//手机
					
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public boolean LightControl(String device,boolean isOn){
		return isOn;
	}
	public boolean CurtainControl(String device,boolean isOn){
		return isOn;
	}
	public boolean DoorControl(String device,boolean isOn){
		return isOn;
	}
	public boolean GetDoorStatus(String device,Door door){
		return false;
	}
	
	public boolean GetLightStatus(String device,Light light){
		return false;
	}
	public boolean GetCurtainStatus(String device,Curtain curtain){
		return false;
	}
	public boolean GetSensorData(String device,int SensorName,Sensor sensor){
		return false;
	}
	
	private boolean VerifyFrame(byte[] data){
		//帧头
		if(((short)data[0]&0xff)!=0xab || ((short)data[1]&0xff)!=0xac)
			return false;
		int datalength = (short)data[17]<<8|data[18];
		//CRC校验
		int parity = CRC.CRC16Calculate(data,datalength+19);
		int parity2 = (data[19+datalength]<<8|data[20+datalength])&0xffff;
		if(parity != parity2)
			return false;
		return true;
	}
	 private static String Byte6ToMac(byte[] c) {
	        return Integer.toHexString(((int)c[0]&0xff))+":"+Integer.toHexString(((int)c[1]&0xff))+":"+Integer.toHexString(((int)c[2]&0xff))+":"
	        		+((short)c[3]&0xff)+":"+Integer.toHexString(((int)c[4]&0xff))+":"+Integer.toHexString(((int)c[5]&0xff)); 
	 }
	 
	 private static byte[] MacToBytes(String mac) {
		byte []macBytes = new byte[6];
		String [] strArr = mac.split(":"); 
		for(int i = 0;i < strArr.length; i++){
			int value = Integer.parseInt(strArr[i],16);
			macBytes[i] = (byte) value;
		}
		return macBytes;
	 }
	 
	 private static String BytesToString(byte[] b,int size){
		 String str= "";
		 for(int i=0;i<size;++i){
			 str+=b[i]-'0';
		 }
		 return str;
	 }
	 private static String MD5_32BytesToString(byte[] md5){
		 String r="";
		 String temp=null;
		 for(int i=0;i<16;++i){
			 temp = Integer.toHexString(((int)md5[i]&0xff));
			 if(temp.length()<2)
				 temp = "0"+temp;
			 r += temp;
		 }
		 return r;
	 }
}
