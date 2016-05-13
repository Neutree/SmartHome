#include "app.h"

const char App::mDeviceNumber[]={1,2,3,4,5,6};

App::App()
:mApSetName("SmartHome"),mApSetPasswd("1208077207"),
mApJoinName("ICanHearYou"),mApJoinPasswd("1208077207"),
mUserName("15023490062"),mUserPasswd("1208077207")
{
	mSensorName[0]=0;
	mSensorName[1]=0;
	mSensorName[2]=0;
	mSensorName[3]=1;
	mToServer.head=0xabac;
	memcpy(mToServer.deviceNumber,mDeviceNumber,6);
	mToServer.data=mToServerData;
	mSwitch.comment = mToServerComment;
	mSwitch.commentLength = 1;
	mSensor.comment = mToServerComment;
	mSensor.commentLength = 0;
	mSensor.data = mToServerSensorData;
	mDoor.comment = mToServerComment;
	mDoor.commentLength = 1;
	
}

void App::TimerInterrupt()
{
	stepMotor.MotorConfig();
}


void App::Init()
{
	//关闭LED
	light.Off();
	//步进电机使能
	stepMotor.Disable();
	//设置步进电机速度，值越小速度越大
	stepMotor.Run(true,2);
	//初始化wifi
	WifiInit();
	
	TaskManager::DelayS(5);
	if(!SignIn())
		com1<<"sign in fail!!!!!!!!!!!!\n\n\n";
	
	com1<<"initialize complete!\n";
}

void App::loop()
{
	static float time;
	if(TaskManager::Time()-time>=20)
	{
		time = TaskManager::Time();
		com1<<".";
	}
	//硬件健康状态检查
	if(!CheckHardware())
		com1<<"haredware error!\n";

	
	//连接状态检查
//	if(!CheckConnectionToServer())
//		com1<<"connection to server error!\n";
	
	
	//接收来自服务器的数据
	ReceiveAndDeal();
}

void App::WifiInit()
{
	//WIFI initialize
	if(!wifi.Kick())//检查连接
	{
		com1<<"no wifi!\n\n\n";
		light.Blink(3,300);
		return;
	}
	else
		com1<<"wifi is healthy\n";
	wifi.SetEcho(false);//关闭回响
//	wifi.SetMode(esp8266::esp8266_MODE_STATION_AP,esp8266::esp8266_PATTERN_DEF);//设置为station+ap模式
	wifi.SetMUX(false);//单连接模式
//	wifi.SetApParam(mApSetName,mApSetPasswd,esp8266::esp8266_PATTERN_DEF);//设置热点信息
//	wifi.JoinAP(mApJoinName,mApJoinPasswd,esp8266::esp8266_PATTERN_DEF);//加入AP
	
	if(!wifi.Connect((char*)"192.168.191.1",8090,Socket_Type_Stream,Socket_Protocol_IPV4))
	{
		com1<<"connect server fail!\n\n\n";
		light.Blink(4,300);
		return;
	}
	com1<<"WIFI initialize complete!\n";
	light.Blink(2,200);
	light.Off();
}


bool App::SignIn()
{
	mToServer.operationType = Protocol::OperationType_Control;
	mToServer.dataType = Protocol::SignIn::dataType;
	mToServer.dataLength = 28;//11username+16passwd+1device type
	memcpy(mToServer.data,mUserName,11);
	
	unsigned char decrypt[16];      
    MD5_CTX md5;
    MD5Init(&md5);
    MD5Update(&md5,(unsigned char*)mUserPasswd,strlen((char *)mUserPasswd));
    MD5Final(&md5,decrypt);
	memcpy(mToServer.data+11,decrypt,16);
	LittleEndianToBigEndian(mToServer.data+27,(short)0);
	if(Write(mToServer))
	{
		mTimeReceivedKeepAlive=TaskManager::Time();
		mIsAlive=true;
		return true;
	}
	mIsAlive = false;
	return false;
}

//硬件健康状态检查
bool App::CheckHardware()
{
	return true;//wifi.Kick();
}


//连接状态检查
bool App::CheckConnectionToServer()
{
	if(TaskManager::Time()-mTimeReceivedKeepAlive>320)//超时（5分钟），没有收到来自服务器的保持包
	{
		mIsAlive = false;
		return false;
	}
	return true;
}


//接收来自服务器的数据
void App::ReceiveAndDeal()
{
	static unsigned short size=0;
	if(com2.ReceiveBufferSize()>5)
	{
		size = wifi.Read(mDataTemp);
		if(size>0)
		{
			void* data;
			static Protocol::OperationType operationType;
			short dataType = Decode(&data,&operationType);
			com1<<"type:"<<dataType<<"\n";
			if(dataType == Protocol::Switch::dataType)
			{
				if(operationType == Protocol::OperationType_Control)
				{
					if(*((Protocol::Switch*)data)->comment == 1)//灯光
					{
						if(((Protocol::Switch*)data)->status==0)
						{
							light.Off();
							mLightOn = false;
						}
						else if(((Protocol::Switch*)data)->status==1)
						{
							light.On();
							mLightOn = true;
						}
						SendLightInfoToServer();
					}
					else if(*((Protocol::Switch*)data)->comment == 2)//窗帘
					{
						SendCurtainInfoToServer(((Protocol::Switch*)data)->status==1);
						//窗帘开关
						if(((Protocol::Switch*)data)->status==0)
						{
							CloseCurtain();
						}
						else if(((Protocol::Switch*)data)->status==1)
						{
							OpenCurtain();
						}
					}
					else if(*((Protocol::Switch*)data)->comment == 3)//门锁
					{
						//这个节点没有门锁
					}
					
				}//控制开关请求
				else if(operationType == Protocol::OperationType_Ask)
				{
					if(*((Protocol::Switch*)data)->comment == 1)//灯光
					{
						SendLightInfoToServer();
					}
					else if(*((Protocol::Switch*)data)->comment == 2)//窗帘
					{
						SendCurtainInfoToServer(mCurtainOn);
					}
					else if(*((Protocol::Switch*)data)->comment == 3)//门锁
					{
						//这个节点没有门锁
					}
				}//询问开关状态
			}
			else if(dataType == Protocol::Sensor::dataType)
			{
				if(operationType == Protocol::OperationType_Ask)
				{
					SendSensorInfoToServer();
				}//询问开关状态
			}
			else if(dataType == Protocol::Door::dataType)
			{/*  
				if(operationType == Protocol::OperationType_Control)
				{
					if(((Protocol::Switch*)data)->status==0)
						light.SetDuty(1,0,true);
					else if(((Protocol::Switch*)data)->status==1)
						light.SetDuty(1,100,true);
				}//控制开关请求
				else if(operationType == Protocol::OperationType_Ask)
				{
					SenLightInfoToServer();
				}//询问开关状态
				*/
			}/*
			else if(dataType == Protocol::SignIn::dataType)
			{
				
			}*/
			else if(dataType == Protocol::KeepAlive::dataType)
			{
				SendKeepAliveToServer();
			}
		}
	}
}




short App::Decode(void* *data,Protocol::OperationType* operationType)
{
	//验证头部
	if((uint8_t)mDataTemp[0]!=0xab || (uint8_t)mDataTemp[1]!=0xac)
		return 0;
	mToServer.dataLength = (short)mDataTemp[17]<<8|mDataTemp[18];
	//CRC校验
	short parity = CRC16Calculate((const uint8_t*)mDataTemp,19+mToServer.dataLength);
	short parity2 = ( (short)mDataTemp[19+mToServer.dataLength]<<8|mDataTemp[20+mToServer.dataLength]);
	if(parity != parity2)
		return 0;
	//校验成功，数据分解
	mToServer.operationType = mDataTemp[4];
	mToServer.dataType = ((short)mDataTemp[2]<<8|mDataTemp[3]);
	memcpy(mToServer.deviceNumber,mDataTemp+5,6);
	memcpy(mToServer.UTC,mDataTemp+11,6);
	mToServer.dataLength = (short)mDataTemp[17]<<8|mDataTemp[18];
	memcpy(mToServer.data,mDataTemp+19,mToServer.dataLength);
	
	if(mToServer.dataType == Protocol::Switch::dataType)
	{
		*data = &mSwitch;
		mSwitch.status = mToServer.data[0];
		mSwitch.commentLength = (short)mToServer.data[1]<<8|mToServer.data[2];
		memcpy(mSwitch.comment,mToServer.data+3,mSwitch.commentLength);
	}
	else if(mToServer.dataType == Protocol::Sensor::dataType)
	{
		*data = &mSensor;
		mSensor.name = (long int)mToServer.data[0]<<24|(long int)mToServer.data[1]<<16|(long int)mToServer.data[2]<<8|(long int)mToServer.data[3];
		mSensor.datalength = mToServer.data[4];
		memcpy(mSensor.data,mToServer.data+5,mSensor.datalength);
		mSensor.commentLength = (short)mToServer.data[1]<<8|mToServer.data[2];
		memcpy(mSensor.comment,mToServer.data+3,((Protocol::Sensor*)data)->commentLength);
	}
	else if(mToServer.dataType == Protocol::Door::dataType)
	{
	}
	else if(mToServer.dataType == Protocol::KeepAlive::dataType)
	{
		*data = &mKeepAlive;
		mKeepAlive.type=mToServer.data[0];
	}
	*operationType = (Protocol::OperationType)mToServer.operationType;
	return mToServer.dataType;
}


bool App::SendLightInfoToServer()
{
	mToServer.operationType = Protocol::OperationType_Ack;
	mToServer.dataType = Protocol::Switch::dataType;
	mToServer.dataLength = 4;
	if(mLightOn)
		mToServer.data[0] = 1;
	else
		mToServer.data[0] = 0;
	mToServer.data[1]=0;
	mToServer.data[2]=1;
	mToServer.data[3]=1;
	if(Write(mToServer))
	{
		mIsAlive=true;
		mTimeReceivedKeepAlive=TaskManager::Time();
		return true;
	}
	mIsAlive = false;
	return false;
}
bool App::SendCurtainInfoToServer(bool isOn)
{
	mToServer.operationType = Protocol::OperationType_Ack;
	mToServer.dataType = Protocol::Switch::dataType;
	mToServer.dataLength = 4;
	if(isOn)
		mToServer.data[0] = 1;
	else
		mToServer.data[0] = 0;
	mToServer.data[1]=0;
	mToServer.data[2]=1;
	mToServer.data[3]=2;
	if(Write(mToServer))
	{
		mIsAlive=true;
		mTimeReceivedKeepAlive=TaskManager::Time();
		return true;
	}
	mIsAlive = false;
	return false;
}

bool App::SendSensorInfoToServer()
{
	mToServer.operationType = Protocol::OperationType_Ack;
	mToServer.dataType = Protocol::Sensor::dataType;
	mToServer.dataLength = 8;
	memcpy(mToServer.data,mSensorName,4);
	mToServer.data[4] = 1;
	mToServer.data[5] = mLightSensor;
	mToServer.data[6]=0;
	mToServer.data[7]=0;
	if(Write(mToServer))
	{
		mIsAlive=true;
		mTimeReceivedKeepAlive=TaskManager::Time();
		return true;
	}
	mIsAlive = false;
	return false;
}


bool App::SendKeepAliveToServer()
{
	mToServer.operationType = Protocol::OperationType_Ack;
	mToServer.dataType = Protocol::KeepAlive::dataType;
	mToServer.dataLength = 1;
	mToServer.data[0] = 1;
	if(Write(mToServer))
	{
		mIsAlive=true;
		mTimeReceivedKeepAlive=TaskManager::Time();
		return true;
	}
	mIsAlive = false;
	return false;
}

bool App::Write(Protocol::ToServer &toServer)
{
	LittleEndianToBigEndian(mDataTemp,toServer.head);//起始标志
	LittleEndianToBigEndian(mDataTemp+2,toServer.dataType);//数据类型
	mDataTemp[4]=toServer.operationType;//操作类型
	memcpy(mDataTemp+5,toServer.deviceNumber,6);
	memcpy(mDataTemp+11,toServer.UTC,6);
	LittleEndianToBigEndian(mDataTemp+17,toServer.dataLength);//数据长度
	memcpy(mDataTemp+19,toServer.data,toServer.dataLength);
	toServer.crc16 = CRC16Calculate((const uint8_t*)mDataTemp,19+toServer.dataLength);
	LittleEndianToBigEndian(mDataTemp+19+toServer.dataLength,toServer.crc16);
	return wifi.Write(mDataTemp,21+toServer.dataLength);
}

bool App::CloseCurtain()
{
	stepMotor.SetDirection(true);
	stepMotor.Enable();
	TaskManager::DelayS(5);
	stepMotor.Disable();
	mCurtainOn = false;
	return true;
}

bool App::OpenCurtain()
{
	stepMotor.SetDirection(false);
	stepMotor.Enable();
	TaskManager::DelayS(5);
	stepMotor.Disable();
	mCurtainOn = true;
	return true;
}

//将小端模式的short类型转换为大端的数组，及short的高位在数组的低字节
void App::LittleEndianToBigEndian(char* array,short bigEndian)
{
	char temp = bigEndian;
	bigEndian>>=8;
	bigEndian&=0x00ff;
	bigEndian|=(short)temp<<8;
	memcpy(array,(char*)&bigEndian,2);
}

