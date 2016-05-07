package com.neucrack.communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class ToDevices {
	private DataInputStream mInStream=null;
	private DataOutputStream mOutStream=null;
	
	public boolean WaitSignIn(String device,String userName,String passwd){
		return false;
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
	public boolean GetDoorStatus(String device,boolean isOn){
		return isOn;
	}
	
	public boolean GetLightStatus(String device,boolean isOn){
		return isOn;
	}
	public boolean GetCurtainStatus(String device,boolean isOn){
		return isOn;
	}
	public boolean GetSensorData(String device,int SensorName,int value){
		return false;
	}
	
}
