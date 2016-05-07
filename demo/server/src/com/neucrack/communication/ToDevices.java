package com.neucrack.communication;

import java.io.DataInputStream;

public class ToDevices {

	static public boolean WaitSignIn(DataInputStream inStream){
		return false;
	}
	static public boolean LightControl(String device,boolean isOn){
		return isOn;
	}
	static public boolean CurtainControl(String device,boolean isOn){
		return isOn;
	}
	static public boolean DoorControl(String device,boolean isOn){
		return isOn;
	}
	static public boolean GetDoorStatus(String device,boolean isOn){
		return isOn;
	}
	
	static public boolean GetLightStatus(String device,boolean isOn){
		return isOn;
	}
	static public boolean GetCurtainStatus(String device,boolean isOn){
		return isOn;
	}
	static public boolean GetSensorData(String device,int SensorName,int value){
		return false;
	}
	
}
