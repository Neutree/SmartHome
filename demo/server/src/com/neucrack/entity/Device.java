package com.neucrack.entity;

import java.util.ArrayList;

public class Device {
	String mName;     //设备编号
	String mOwner;            //所有者用户名
	
	ArrayList<SubDevice> mSubDevices = new ArrayList<SubDevice>();     //下面挂载的子设备（组件）
	
	
	String mNickName;
	String mComment;
	
	public Device(String name,String Ownner,String nickName,String comment,SubDevice... subdevice) {
		mName =name;
		mOwner =Ownner;
		mNickName =nickName;
		mComment = comment;
		for(int i=0;i<subdevice.length;++i){
			mSubDevices.add(subdevice[i]);
		}
	}
	
	public String getmName() {
		return mName;
	}
	public void setmName(String mDeviceName) {
		this.mName = mDeviceName;
	}
	public String getmOwner() {
		return mOwner;
	}
	public void setmOwner(String mOwner) {
		this.mOwner = mOwner;
	}
	public ArrayList<SubDevice> getmSubDevices() {
		return mSubDevices;
	}
	public void setmSubDevices(ArrayList<SubDevice> mSubDevices) {
		this.mSubDevices = mSubDevices;
	}
	public String getmNickName() {
		return mNickName;
	}
	public void setmNickName(String mNickName) {
		this.mNickName = mNickName;
	}
	public String getmComment() {
		return mComment;
	}
	public void setmComment(String mComment) {
		this.mComment = mComment;
	}
	
	
}
