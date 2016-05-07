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
	public DoorInfo GetDoorStatus(String device){
		DoorInfo door = new DoorInfo();
		return door;
	}
	
	public LightInfo GetLightStatus(String device){
		LightInfo light = new LightInfo();
		return light;
	}
	public CurtainInfo GetCurtainStatus(String device){
		CurtainInfo curtain = new CurtainInfo();
		return curtain;
	}
	public SensorInfo GetSensorData(String device,int SensorName){
		SensorInfo sensor = new SensorInfo();
		return sensor;
	}
	
	class SignInfo{
		boolean isValid;
		String device;
		String userName;
		String passwd;
	}
	class LightInfo{
		boolean isValid;
		boolean isOn;
	}
	class CurtainInfo{
		boolean isValid;
		boolean isOn;
	}
	class SensorInfo{
		boolean isValid;
		int value;
	}
	class DoorInfo{
		boolean isValid;
		boolean isOn;
	}
}
