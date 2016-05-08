package com.neucrack.entity;

import java.util.ArrayList;

public class DeviceDoor {
	static public final short DEVICE_TYPE = 0X0003;
	
	boolean mIsOn;
	ArrayList<User> mOwner;
	public boolean ismIsOn() {
		return mIsOn;
	}
	public void setmIsOn(boolean mIsOn) {
		this.mIsOn = mIsOn;
	}
	public ArrayList<User> getmOwner() {
		return mOwner;
	}
	public void setmOwner(ArrayList<User> mOwner) {
		this.mOwner = mOwner;
	}
	
	
}
