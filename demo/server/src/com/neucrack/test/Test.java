package com.neucrack.test;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.neucrack.DAO.DAOConnectionInfo;
import com.neucrack.DAO.DAOHelper;
import com.neucrack.communication.ToDevices;
import com.neucrack.devices.Curtain;
import com.neucrack.devices.Light;
import com.neucrack.entity.Device;
import com.neucrack.entity.DeviceSensor;
import com.neucrack.entity.DeviceSwitch;
import com.neucrack.entity.SubDevice;
import com.neucrack.entity.User;
import com.neucrack.server.HttpRequest;
import com.neucrack.tool.CRC;
import com.neucrack.tool.Encrypt;
import com.neucrack.tool.Session;
import com.neucrack.tool.StringRelated;
import com.sun.org.apache.bcel.internal.generic.NEW;

public class Test {

	@org.junit.Test
	public void test() {
		DAOHelper dao = new DAOHelper();
		Device device;
		User user = new User(DAOConnectionInfo.adminName, DAOConnectionInfo.adminPassword, "admin", "/assets/pic/headImage/15023490062.png");
		
		
	//	System.out.println(dao.QueryOwnerOfDevice("1:2:3:4:5:6"));
/*		
		
		
		
		//创建管理员
		
		System.out.println(dao.AddUser(user));
		System.out.println(DAOConnectionInfo.adminName+"\t"+DAOConnectionInfo.adminPassword);
		
		//创建卧室1
		
			device = new Device("1:2:3:4:5:6", "15023490062", StringRelated.newString_UTF_8("卧室1"), StringRelated.newString_UTF_8("卧室1"),
new SubDevice(DeviceSwitch.DEVICE_TYPE,(long) 1, StringRelated.newString_UTF_8("灯"), new DeviceSwitch()),
new SubDevice(DeviceSwitch.DEVICE_TYPE,(long) 2, StringRelated.newString_UTF_8("窗帘"), new DeviceSwitch()),
new SubDevice(DeviceSensor.DEVICE_TYPE,(long) 1, StringRelated.newString_UTF_8("光传感器"), new DeviceSensor()));
				System.out.println(dao.AddDevice(device, user));
		//创建卧室2
				device = new Device("1:2:3:4:5:7", "15023490062", StringRelated.newString_UTF_8("卧室2"), StringRelated.newString_UTF_8("卧室2"),
	new SubDevice(DeviceSwitch.DEVICE_TYPE,(long) 1, StringRelated.newString_UTF_8("灯"), new DeviceSwitch()),
	new SubDevice(DeviceSwitch.DEVICE_TYPE,(long) 2, StringRelated.newString_UTF_8("窗帘"), new DeviceSwitch()),
	new SubDevice(DeviceSensor.DEVICE_TYPE,(long) 1, StringRelated.newString_UTF_8("光传感器"), new DeviceSensor()));
				System.out.println(dao.AddDevice(device, user));
		//创建卧室3
		device = new Device("1:2:3:4:5:8", "15023490062", StringRelated.newString_UTF_8("卧室3"), StringRelated.newString_UTF_8("卧室3"),
	new SubDevice(DeviceSwitch.DEVICE_TYPE,(long) 1, StringRelated.newString_UTF_8("灯"), new DeviceSwitch()),
	new SubDevice(DeviceSwitch.DEVICE_TYPE,(long) 2, StringRelated.newString_UTF_8("窗帘"), new DeviceSwitch()),
	new SubDevice(DeviceSensor.DEVICE_TYPE,(long) 1, StringRelated.newString_UTF_8("光传感器"), new DeviceSensor()));
				System.out.println(dao.AddDevice(device, user));
		
		//创建厨房
		device = new Device("1:2:3:4:5:9", "15023490062", StringRelated.newString_UTF_8("厨房"), StringRelated.newString_UTF_8("厨房"),
	new SubDevice(DeviceSensor.DEVICE_TYPE,(long) 1, StringRelated.newString_UTF_8("火焰传感器"), new DeviceSensor()),
	new SubDevice(DeviceSensor.DEVICE_TYPE,(long) 2, StringRelated.newString_UTF_8("烟雾感器"), new DeviceSensor()));
				System.out.println(dao.AddDevice(device, user));
		
		//创建门设备
		device = new Device("1:2:3:4:5:a", "15023490062", StringRelated.newString_UTF_8("门"), StringRelated.newString_UTF_8("门"),
	new SubDevice(DeviceSensor.DEVICE_TYPE,(long) 1, StringRelated.newString_UTF_8("门"), new DeviceSensor()));
				System.out.println(dao.AddDevice(device, user));
		*/

	}
	


}
