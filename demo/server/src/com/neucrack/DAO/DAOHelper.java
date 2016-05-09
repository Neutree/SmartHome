package com.neucrack.DAO;

import java.io.UnsupportedEncodingException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.neucrack.entity.Device;
import com.neucrack.entity.DeviceDoor;
import com.neucrack.entity.DeviceSensor;
import com.neucrack.entity.DeviceSwitch;
import com.neucrack.entity.User;
import com.neucrack.server.HttpRequest;

public class DAOHelper {
	
	/**
	 * 
	 * @param user 要查找的用户呢
	 * @return 1:成功  0：没有该用户   <0:发生错误
	 */
	public int QueryUser(User user) {
		String param = "username="+user.getmName()+"&password="+user.getmPasswd();
		JSONObject jsonObject =null;
		try {
			String result = HttpRequest.sendGet(DAOConnectionInfo.mUrl+"login", param);
			if(result == null){
				System.out.println("用户登录错误，数据错误或者数据服务器响应失败，请看返回的响应，错误400则为服务器响应失败，请重试");
				return -1;
			}
			 jsonObject= new JSONObject(result);
		} catch (JSONException e) {
			e.printStackTrace();
			return -1;
		}
		try {
			user.setSession((String) jsonObject.get("sessionToken"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		return 1;
	}
	
	public int AdminSignIn(){
		String param = "username="+DAOConnectionInfo.adminName+"&password="+DAOConnectionInfo.adminPassword;
		JSONObject jsonObject =null;
		try {
			String result = HttpRequest.sendGet(DAOConnectionInfo.mUrl+"login", param);
			if(result == null){
				System.out.println("管理员登录错误，数据错误或者数据服务器响应失败，请看返回的响应，错误400则为服务器响应失败，请重试");
				return -1;
			}
			 jsonObject= new JSONObject(result);
		} catch (JSONException e) {
			e.printStackTrace();
			return -1;
		}
		try {
			DAOConnectionInfo.session = (String) jsonObject.get("sessionToken");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		return 1;
	}
	/**
	 * 验证用户用户名和密码是否正确
	 * @param user 要查找的用户呢
	 * @return 1:成功  0：没有该用户   <0:发生错误
	 */
	public int VerifyUser(User user) {
		String param = "username="+user.getmName()+"&password="+user.getmPasswd();
		JSONObject jsonObject =null;
		try {
			String result = HttpRequest.sendGet(DAOConnectionInfo.mUrl+"login", param);
			if(result == null){
				System.out.println("验证用户信息错误，数据错误或者数据服务器响应失败，请看返回的响应，错误400则为服务器响应失败，请重试");
				return -1;
			}
			 jsonObject= new JSONObject(result);
		} catch (JSONException e) {
			e.printStackTrace();
			return -1;
		}
		try {
			user.setSession((String) jsonObject.get("sessionToken"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		return 1;
	}
	
	public int AddUser(User user){
		JSONObject json = new JSONObject();
		try {
			json.put("username",user.getmName());
			json.put("password", user.getmPasswd());
			json.put("mobilePhoneNumber", user.getmName());
			json.put("nickname", user.getmNickName());
			json.put("headImage", user.getmHeadPicture());
		} catch (JSONException e) {
			e.printStackTrace();
			return -2;
		}
		
		String param = json.toString();
		String result = HttpRequest.sendPost(DAOConnectionInfo.mUrl+"users", param);
		if(result==null)
			return -1;
		return 1;
	}
	
	public int DropUser(User user){
		//TODO 未写完
		if(QueryUser(user)<=0)
			return -1;
		
		return -1;
	}
	
	public int EditUser(User oldOne,User newOne){
		//TODO 未写完
		return -1;
	}
	public String QueryOwnerOfDevice(String deviceName){
		if(AdminSignIn()<=0)
			return null;
		String name=null;
		String param = "where={\"deviceName\":\""+deviceName+"\"}";
		JSONObject jsonObject =null;
		try {
			String result = HttpRequest.sendGet(DAOConnectionInfo.mUrl+"classes/Devices", param);
			if(result == null){
				System.out.println("获取设备信息错误，数据错误或者数据服务器响应失败");
				return null;
			}
			 jsonObject= (JSONObject)(new JSONObject(result)).getJSONArray("results").get(0);
			 if(!jsonObject.get("deviceName").equals(deviceName))
				 return  null;
			 name = (String) jsonObject.get("userName");
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		
		return name;
	}
	
	public int AddDevice(Device device,User user){
		if(AdminSignIn()<=0)
			return -1;
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		try {
			for(int i=0;i<device.getmSubDevices().size();++i){
				JSONObject json = new JSONObject();
				json.put("deviceType", device.getmSubDevices().get(i).getmType());
				json.put("subDeviceNumber", device.getmSubDevices().get(i).getmNumber());
				json.put("subDeviceNickName", device.getmSubDevices().get(i).getmNickName());
				JSONObject json2 = new JSONObject();
				if( device.getmSubDevices().get(i).getmType() == DeviceSwitch.DEVICE_TYPE){
					json2.put("status", ((DeviceSwitch)device.getmSubDevices().get(i).getmDeviceInfo()).getmStatus() );
				}
				else if(device.getmSubDevices().get(i).getmType() == DeviceSensor.DEVICE_TYPE){
					json2.put("value", ((DeviceSensor)device.getmSubDevices().get(i).getmDeviceInfo()).getmValue());
				}
				else if(device.getmSubDevices().get(i).getmType() == DeviceDoor.DEVICE_TYPE){
					
				}
				json.put("subDeviceData", json2);
				jsonArray.put(json);
			}
			jsonObject.put("deviceName", device.getmName());
			jsonObject.put("nickName", device.getmNickName());
			jsonObject.put("userName", device.getmOwner());
			jsonObject.put("comment", device.getmComment());
			jsonObject.put("data", jsonArray);
		} catch (JSONException e) {
			e.printStackTrace();
			return -1;
		}
		String param = jsonObject.toString();
		String result = HttpRequest.sendPost(DAOConnectionInfo.mUrl+"classes/Devices", param);
		if(result==null)
			return -1;
		
		return 1;
	}
}
