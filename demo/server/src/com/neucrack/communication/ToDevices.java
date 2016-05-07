package com.neucrack.communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class ToDevices {
	private DataInputStream mInStream=null;
	private DataOutputStream mOutStream=null;
	
	public ToDevices(DataInputStream inStream,DataOutputStream outStream){
		mInStream = inStream;
		mOutStream = outStream;
	}
	
	public SignInfo WaitSignIn(){
		SignInfo result = new SignInfo();
		return result;
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
	public boolean GetDoorStatus(String device,DoorInfo door){
		return false;
	}
	
	public boolean GetLightStatus(String device,LightInfo light){
		return false;
	}
	public boolean GetCurtainStatus(String device,CurtainInfo curtain){
		return false;
	}
	public boolean GetSensorData(String device,int SensorName,SensorInfo sensor){
		return false;
	}
	
	class SignInfo{
		public String device;
		public String userName;
		public String passwd;
	}
	class LightInfo{
		public boolean isOn;
	}
	class CurtainInfo{
		public boolean isOn;
	}
	class SensorInfo{
		public int value;
	}
	class DoorInfo{
		public boolean isOn;
	}
}
