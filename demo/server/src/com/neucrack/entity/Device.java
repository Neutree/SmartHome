package com.neucrack.entity;

import java.util.ArrayList;

public class Device {
	String mDeviceNumber;     //设备编号
	String mOwner;            //所有者用户名
	
	ArrayList<SubDevice> mSubDevices;     //下面挂载的子设备（组件）
	
	
	String mNickName;
	String mComment;
}
