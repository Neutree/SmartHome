/**
 * 与移动客户端通信
 * @author neucrack
 *
 */
package com.neucrack.communication;

import java.awt.color.ICC_ColorSpace;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.neucrack.DAO.DAOHelper;
import com.neucrack.devices.Curtain;
import com.neucrack.devices.Door;
import com.neucrack.devices.Light;
import com.neucrack.devices.Sensor;
import com.neucrack.devices.SignInfo;
import com.neucrack.entity.DeviceSwitch;
import com.neucrack.entity.User;
import com.neucrack.tool.CRC;
import com.neucrack.tool.Date_TimeStamp;
import com.neucrack.tool.Encrypt;
import com.neucrack.tool.Session;
import com.neucrack.tool.StringRelated;

public class ToUser {

	private DataInputStream mInStream=null;
	private DataOutputStream mOutStream=null;
	
	public ToUser(DataInputStream inStream,DataOutputStream outStream){
		mInStream = inStream;
		mOutStream = outStream;
	}


	public boolean VerifyFrame(byte[] dataToRead){
		if(dataToRead.length<47)
			return false;
		//帧头
		if(((short)dataToRead[0]&0xff)!=0xab || ((short)dataToRead[1]&0xff)!=0xac)
			return false;
		int datalength = (short)dataToRead[43]<<8|dataToRead[44];
		//CRC校验
		int parity = CRC.CRC16Calculate(dataToRead,datalength+45);
		int parity2 = (dataToRead[45+datalength]<<8|dataToRead[46+datalength]&0xff)&0xffff;
		if(parity != parity2)
			return false;
		return true;
	}
	public boolean CheckIfSignedIn(String session){
		//验证用户名和session值
		if(null==Session.getAttribute(session))
		{
			return false;
		}
		return true;
	}
	
	public int DealSignIn(User user){
		DAOHelper dao = new DAOHelper();
		int result = dao.VerifyUser(user);
		if(result <= 0){//验证失败
			System.out.println("验证用户失败，错误代码："+result);
		}		
		//随机数+时间戳作为session key
		/*long randomVal = (long)(1+Math.random()*(60000-1+1));
		String sessionKey =""+randomVal+Date_TimeStamp.timeStamp();
		sessionKey = Encrypt.md5(sessionKey);//对session key进行MD5加密
		Session.setAttribute(sessionKey, user);
		user.setSession(sessionKey);
		*///不使用随机数+时间戳，采用
		//响应用户的登录消息，dao.VerifyUser(user)里面会添加sessionToken
		SendToUserSignInResult(user,result);
		if(result>0)
			Session.setAttribute(user.getSession(), user.getmName());
		return result;
	}
	
	public int DealSignUp(User user){
		DAOHelper dao = new DAOHelper();
		int result = dao.QueryUser(user);
		if(result != 0){//新增用户出错或者已经存在
			System.out.println("新增用户出错，错误代码："+result);
			if(result>0)
				result=0;
		}
		else{
			result = dao.AddUser(user);
		}
		//响应用户的登录消息
		SendToUserSignUpResult(user,result);
		return result;
	}
	
	
	
	
	
	private boolean SendToUserSignInResult(User user,int data){
		byte[] d = new byte[31];
		byte[] userName = user.getmName().getBytes();
		byte[] userPassword = StringRelated.MD5_32_StringToBytes(user.getmPasswd());
		System.arraycopy(userName, 0, d, 0, 11);
		System.arraycopy(userPassword, 0, d, 11, 16);
		d[27] = (byte) (data>>24&0xff);
		d[28] = (byte) (data>>16&0xff);
		d[29] = (byte) (data>>8&0xff);
		d[30] = (byte) (data&0xff);
		return SendToUser((short)0x0011,(byte)0x02,user.getSession(),(short)31,d);
	}
	
	private boolean SendToUserSignUpResult(User user,int data) {
		byte d[] ={ (byte) (data&0xff)};
		return SendToUser((short)0x0012,(byte)0x02,user.getSession(),(short)31,d);
	}
	
	/**
	 * 发送数据帧到用户客户端
	 * @param type
	 * @param operationType
	 * @param session
	 * @param dataLength
	 * @param data
	 * @return
	 */
	private boolean  SendToUser(Short type,byte operationType,String session,Short dataLength,byte[] data) {
		byte[] dataToWrite = new byte[47+dataLength];
		byte[] sessionBytes = StringRelated.SessionTokenToBytes32(session);
		dataToWrite[0] = (byte) 0xab;
		dataToWrite[1] = (byte) 0xac;
		dataToWrite[2] = (byte) (type>>8&0xff);
		dataToWrite[3] = (byte) (type&0xff);
		dataToWrite[4] = (byte) operationType;
		System.arraycopy(sessionBytes, 0, dataToWrite, 5, 32);
		dataToWrite[43] = (byte) (dataLength>>8&0xff);
		dataToWrite[44] = (byte) (dataLength&0xff);
		System.arraycopy(data, 0, dataToWrite, 45, dataLength);
		long crc16 = CRC.CRC16Calculate(dataToWrite, dataLength+45);
		dataToWrite[45+dataLength] = (byte) (crc16>>8&0xff);
		dataToWrite[46+dataLength] = (byte) (crc16&0xff);
		try {
			mOutStream.write(dataToWrite, 0, 47+dataLength); 
			mOutStream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		};
		return true;
	}


	public boolean ControlSwitch(ToDevices toDevice,User user,String device, long switchName, boolean isOn) {
		//验证设备是否属于该用户
		DAOHelper daoHelper = new DAOHelper();
		if(!daoHelper.QueryOwnerOfDevice(device).equals(user.getmName()))
			return false;
		//验证通过，使用ToDevices进行控制
		boolean result=false;
		if(switchName == 1){
			result = toDevice.LightControl(device, isOn);
		}
		else if(switchName ==2 ){
			result = toDevice.CurtainControl(device, isOn);
		}
		else if(switchName ==3 ){
			result = toDevice.DoorControl(device, isOn);
		}
		if(!result)
			return false;
		byte deviceBytes[] = StringRelated.MacToBytes(device);
		byte[] data = {0,0,0,0,0,0,0,0,0,1,(byte) (isOn?1:0),0,0};
		System.arraycopy(deviceBytes, 0, data, 0, 6);
		return SendToUser((short)0x0001, (byte)2, user.getSession(), (short)13, data);
	}


	public boolean QuerySwitch(ToDevices toDevice,User user, String device, long switchName) {
		//验证设备是否属于该用户
		DAOHelper daoHelper = new DAOHelper();
		if(!daoHelper.QueryOwnerOfDevice(device).equals(user.getmName()))
			return false;
		//验证通过，使用ToDevices进行状态查询
		boolean result=false;
		boolean isOn = false;
		if(switchName == 1){
			Light light = new Light();
			result = toDevice.GetLightStatus(device, light);
			isOn = light.isOn;
		}
		else if(switchName ==2 ){
			Curtain curtain = new Curtain();
			result = toDevice.GetCurtainStatus(device, curtain);
			isOn = curtain.isOn;
		}
		else if(switchName ==3 ){
			Door door = new Door();
			result = toDevice.GetDoorStatus(device, door);
			isOn = door.isOn;
		}
		if(!result)
			return false;
		byte deviceBytes[] = StringRelated.MacToBytes(device);
		byte[] data = {0,0,0,0,0,0,0,0,0,1,(byte) (isOn?1:0),0,0};
		System.arraycopy(deviceBytes, 0, data, 0, 6);
		return SendToUser((short)0x0001, (byte)2, user.getSession(), (short)13, data);
	}


	public boolean QuerySensor(ToDevices toDevice,User user, String device, long switchName) {
		//验证设备是否属于该用户
		DAOHelper daoHelper = new DAOHelper();
		if(!daoHelper.QueryOwnerOfDevice(device).equals(user.getmName()))
			return false;
		//验证通过，使用ToDevices进行状态查询
		boolean result=false;
		int value = 0;
//		if(switchName == 1){//传感器
			Sensor sensor = new Sensor();			
			result = toDevice.GetSensorData(device, switchName, sensor);
			value = sensor.value;
//		}
		if(!result)
			return false;
		byte deviceBytes[] = StringRelated.MacToBytes(device);
		byte[] data = {0,0,0,0,0,0,0,0,0,1,1,(byte)value,0,0};
		System.arraycopy(deviceBytes, 0, data, 0, 6);
		return SendToUser((short)0x0002, (byte)2, user.getSession(), (short)14, data);
	}
}
