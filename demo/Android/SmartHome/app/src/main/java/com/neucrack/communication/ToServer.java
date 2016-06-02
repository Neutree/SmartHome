package com.neucrack.communication;

import android.content.Context;

import com.neucrack.entity.PreferenceData;
import com.neucrack.entity.ServerInfo;
import com.neucrack.entity.User;
import com.neucrack.tool.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by neucrack on 2016-05-09.
 */
public class ToServer {
    Context mContext;
    public ToServer(Context context){
        mContext = context;
    }
    public boolean SignIn(User user){
        user.setmSession("");
        if(user==null||user.getmPasswd()==null||user.getmPasswd().equals(""))
            return false;
        RequestData d = new RequestData();
        d.mData = new byte[31];
        byte[] userName = user.getmName().getBytes();
        byte[] userPassword = StringRelated.MD5_32_StringToBytes(Encrypt.md5(user.getmPasswd()));
        System.arraycopy(userName, 0, d.mData, 0, 11);
        System.arraycopy(userPassword, 0, d.mData, 11, 16);
        d.mData[27] = 0;
        d.mData[28] = 0;
        d.mData[29] = 0;
        d.mData[30] = 0;
        if(!SendRequest((short)0x0011,(byte)0x01,user.getmSession(),(short)31,d))
            return false;
        String sessionString = StringRelated.SessionTokenBytes32ToString(d.mSession);
        user.setmSession(sessionString);
        return true;
    }

    public boolean SignUp(User user){
        //TODO 未写
        return false;
    }

    public boolean SendRequest(Short type,byte operationType,String session,Short dataLength,RequestData data){
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
        System.arraycopy(data.mData, 0, dataToWrite, 45, dataLength);

        long crc16 = CRC.CRC16Calculate(dataToWrite, dataLength+45);
        dataToWrite[45+dataLength] = (byte) (crc16>>8&0xff);
        dataToWrite[46+dataLength] = (byte) (crc16&0xff);
        try {
            Socket socket = new Socket(ServerInfo.mAddress,ServerInfo.mPort);
            socket.setSoTimeout(20000);
            DataInputStream mInStream=new DataInputStream(socket.getInputStream());
            DataOutputStream mOutStream=new DataOutputStream(socket.getOutputStream());//构建输入输出流对象
            mOutStream.write(dataToWrite, 0, 47+dataLength);
            mOutStream.flush();
            int size =0;
            try{
                size = mInStream.read(dataToWrite);
            }catch (ArrayIndexOutOfBoundsException e){
                //接收到的数据可能会比发送的时候多
            }
            if(size<=0)
                return false;
            if(!VerifyFrame(dataToWrite))
                return false;
            System.arraycopy(dataToWrite, 45, data.mData, 0, dataLength);
            System.arraycopy(dataToWrite, 5, data.mSession, 0, 32);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
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
        int parity2 =((int)(dataToRead[45+datalength]<<8|dataToRead[46+datalength]&0xff)&0xffff);
        if(parity != parity2)
            return false;
        return true;
    }

    public boolean SetSwitch(String deviceName, int subDeviceNumber, boolean isOn) {
        User user = PreferenceData.GetUserInfo(mContext);
        if(user==null)
            return false;
        RequestData d = new RequestData();
        d.mData = new byte[13];
        byte[] deviceNameBytes = StringRelated.MacToBytes(deviceName);
        System.arraycopy(deviceNameBytes, 0, d.mData, 0, 6);
        d.mData[6] = (byte) (subDeviceNumber>>24&0xff);
        d.mData[7] = (byte) (subDeviceNumber>>16&0xff);
        d.mData[8] = (byte) (subDeviceNumber>>8&0xff);
        d.mData[9] = (byte) (subDeviceNumber&0xff);
        d.mData[10]= (byte) (isOn?1:0);
        d.mData[11]=0;
        d.mData[12]=0;
        if(!SendRequest((short)0x0001,(byte)0x01,user.getmSession(),(short)13,d))
            return false;
        return true;
    }

    public long GetSensor(String deviceName, int subDeviceNumber) {
        User user = PreferenceData.GetUserInfo(mContext);
        if(user==null)
            return -1;
        RequestData d = new RequestData();
        d.mData = new byte[14];
        byte[] deviceNameBytes = StringRelated.MacToBytes(deviceName);
        System.arraycopy(deviceNameBytes, 0, d.mData, 0, 6);
        d.mData[6] = (byte) (subDeviceNumber>>24&0xff);
        d.mData[7] = (byte) (subDeviceNumber>>16&0xff);
        d.mData[8] = (byte) (subDeviceNumber>>8&0xff);
        d.mData[9] = (byte) (subDeviceNumber&0xff);
        d.mData[10]= 1;
        d.mData[11]=0;
        d.mData[12]=0;
        d.mData[13]=0;
        if(!SendRequest((short)0x0002,(byte)0x03,user.getmSession(),(short)14,d))
            return -1;
        return (long)(d.mData[11]&0xff);
    }

    public int GetSwitchStatus(String deviceName, int subDeviceNumber) {
        User user = PreferenceData.GetUserInfo(mContext);
        if(user==null)
            return -1;
        RequestData d = new RequestData();
        d.mData = new byte[13];
        byte[] deviceNameBytes = StringRelated.MacToBytes(deviceName);
        System.arraycopy(deviceNameBytes, 0, d.mData, 0, 6);
        d.mData[6] = (byte) (subDeviceNumber>>24&0xff);
        d.mData[7] = (byte) (subDeviceNumber>>16&0xff);
        d.mData[8] = (byte) (subDeviceNumber>>8&0xff);
        d.mData[9] = (byte) (subDeviceNumber&0xff);
        d.mData[10]= 0;
        d.mData[11]=0;
        d.mData[12]=0;
        if(!SendRequest((short)0x0001,(byte)0x03,user.getmSession(),(short)13,d))
            return -1;
        return d.mData[10];
    }


    class RequestData{
        public byte[] mData;
        public byte[] mSession = new byte[32];;
    }
}
