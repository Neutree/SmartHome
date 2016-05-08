package com.neucrack.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import com.neucrack.communication.ToDevices;
import com.neucrack.communication.ToUser;
import com.neucrack.entity.DeviceDoor;
import com.neucrack.entity.DeviceSensor;
import com.neucrack.entity.DeviceSwitch;
import com.neucrack.entity.User;
import com.neucrack.tool.CRC;
import com.neucrack.tool.Date_TimeStamp;
import com.neucrack.tool.Session;
import com.neucrack.tool.StringRelated;

public class ServerToUserThread extends Thread {

	ArrayList<ServerToDeviceThead> mSocketList=null;
	private Socket mSocket = null;
	private DataInputStream mInStream=null;
	private DataOutputStream mOutStream=null;
	private ToUser mToUser = null;
	
	private byte[] dataToRead = new byte[2048];
	
	
	public ServerToUserThread(ArrayList<ServerToDeviceThead> mScocketList ,Socket socket) {
		mSocketList = mScocketList;
		mSocket = socket;
	}
	
	
	@Override
	public void run() {
		super.run();
		//��¼ʱ���
		long startTime = Date_TimeStamp.timeStamp();
		// ��ȡ������������ȡ�ͻ�����Ϣ
		if(!getIn_Out_stream())
		{
			System.out.println("��ȡ�������������");
			Close();
			return;
		}
		User user = new User();
		try {
			mSocket.setSoTimeout(20000);//���ó�ʱΪ20s
			int size = mInStream.read(dataToRead);
			if(size>0){
				if(!mToUser.VerifyFrame(dataToRead)){
					System.out.println("��Ϣ֡������!!!");
					Close();
					return;
				}
				String sessionString = "";
				String temp=null;
				for(int i=0;i<16;++i){//��ȡsessionֵ
					temp = Integer.toHexString(((int)dataToRead[i+5]&0xff));
					if(temp.length()<2)
						temp = "0"+temp;
					sessionString += temp;
				}
				user.setSession(sessionString);
				if(!mToUser.CheckIfSignedIn(user.getSession())){//δ��¼
					
					if(dataToRead[4] != 0x01 || dataToRead[2] != 0x00 || (dataToRead[3]!=0x11 && dataToRead[3]!=0x12)){//���ǵ�¼����ע������
						System.out.println("��δ��¼�����󱻾ܾ�������");
						Close();
						return;
					}
					byte[] userName = new byte[11];
					System.arraycopy(dataToRead, 29, userName, 0, 11);
					String name = StringRelated.BytesToString(userName,11);//��ȡ�û���
					byte[] userPasswd = new byte[16];
					System.arraycopy(dataToRead, 40, userPasswd, 0, 16);
					String passwd = StringRelated.MD5_32BytesToString(userPasswd);//��ȡ�û���
					user.setmName(name);
					user.setmPasswd(passwd);
					
									
					if(dataToRead[2] == 0x00 && dataToRead[3]==0x12){
						//����ע������
						byte[] userNickName = new byte[10];
						System.arraycopy(dataToRead, 56, userNickName, 0, 11);
						user.setmNickName(StringRelated.BytesToString(userNickName,10));//��ȡ�ǳ�
						
						int signInResult =mToUser.DealSignUp(user); 
						if(signInResult<=0){
							System.out.println("ע�����,������룺"+signInResult);
							Close();
							return;
						}
					}
					else{
						//������¼����
						int signInResult =mToUser.DealSignIn(user); 
						if(signInResult <= 0){
							System.out.println("��¼����,������룺"+signInResult);
							Close();
							return;
						}
					}
				}
				else{//�Ѿ���¼
					user.setmName((String)Session.getAttribute(user.getSession()));//����session����û���
					
					String device = StringRelated.Byte6ToMac(dataToRead,29);//Ҫͨ�ŵ��豸��
					//�������豸�б��л�ø��豸�ŵ��豸ͨ�ŵĶ���
					ToDevices toDevice = null;
					for (int i = 0;i<mSocketList.size();++i) {
						if(!mSocketList.get(i).IsAlive())//�Ѿ��ر������ˣ������Ƴ�
							mSocketList.remove(i);
						else{//���ӻ��������豸���ߣ�//�ж��Ƿ���Ҫͨ�ŵ��豸����ͬ
							if(mSocketList.get(i).mSignInfo.device.equals(device)){
								toDevice = mSocketList.get(i).mToDevices;
								break;
							}
						}
					}
					
					if(dataToRead[4] == 0x01){//��������
						int type =  ((int)dataToRead[2]<<8&0xff00) | ((int)dataToRead[3]&0xff);
						long switchName = (dataToRead[35]<<24&0xff000000)|(dataToRead[36]<<16&0xff0000)|(dataToRead[37]<<8&0xff00)|(dataToRead[38]&0xff);
						switch (type) {
						case DeviceSwitch.DEVICE_TYPE:
							if(!mToUser.ControlSwitch(toDevice,user,device,switchName,(dataToRead[39]==1)))
								System.out.println("���ƿ���ʧ��");
							break;
						case DeviceDoor.DEVICE_TYPE:
							System.out.println("��������ʹ�ÿ�������");
							break;
						default:
							System.out.println("δ֪�豸��������");
							break;
						}
					}
					else if(dataToRead[2] == 0x00 && dataToRead[3] == 0x03){//ѯ������
						int type =  ((int)dataToRead[2]<<8&0xff00) | ((int)dataToRead[3]&0xff); 
						long switchName = (dataToRead[35]<<24&0xff000000)|(dataToRead[36]<<16&0xff0000)|(dataToRead[37]<<8&0xff00)|(dataToRead[38]&0xff);
						switch (type) {
						case DeviceSwitch.DEVICE_TYPE:
							if(!mToUser.QuerySwitch(toDevice,user,device,switchName))
								System.out.println("��ѯ����״̬ʧ��");
							break;
						case DeviceSensor.DEVICE_TYPE:
							if(!mToUser.QuerySensor(toDevice,user,device,switchName))
								System.out.println("��ѯ������״̬ʧ��");
							break;
						case DeviceDoor.DEVICE_TYPE:
							System.out.println("��ʹ�ò�ѯ���ص��������");
							break;
						default:
							System.out.println("δ֪�豸��������");
							break;
						}
					}
				}
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Close();
		}
		Close();
	}

		
	private boolean getIn_Out_stream(){
		try {
			mInStream = new DataInputStream(mSocket.getInputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// ��ȡ�����,��Ӧ�ͻ�������
		try {
			mOutStream = new DataOutputStream(mSocket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		mToUser = new ToUser(mInStream,mOutStream);
		return true;
	}
	
	private void Close() {
		try {
			if(mOutStream!=null)
				mOutStream.close();
			if(mInStream!=null)
				mInStream.close();
			if (mSocket != null)
				mSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Abnormal , Socket : �ر�socket��Դ�쳣 ��");
			e.printStackTrace();
		}
	}


}