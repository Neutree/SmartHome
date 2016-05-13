#ifndef __APP_H
#define __APP_H

#include "hardware.h"
#include "MD5.h"
#include "Protocol.h"
#include "stdlib.h"
#include "CRC.h"

class App
{
public:
	//光感值,大概范围0~300，值越小光越强
	u8 mFireSensor;
	u8 mSmokeSensor;

	App();
//	void TimerInterrupt();
	void Init();
	void loop();

	void WifiInit();

	bool SignIn();

	//硬件健康状态检查
	bool CheckHardware();
	
	
	//连接状态检查
	bool CheckConnectionToServer();
	
	//接收来自服务器的数据并处理
	void ReceiveAndDeal();
	
	bool SendDoorInfoToServer();
	bool SendSensorInfoToServer(char* sensorNumber,char value);
	bool SendKeepAliveToServer();
	
	
	bool Write(Protocol::ToServer&);
	
	
	//开启窗帘
//	bool CloseCurtain();
//	//关闭窗帘
//	bool OpenCurtain();
	
	//将小端模式的short类型转换为大端的数组，及short的高位在数组的低字节
	void LittleEndianToBigEndian(char* , short);
	
	
private:
	
	char mApSetName[15];
	char mApSetPasswd[18];
	char mApJoinName[15];
	char mApJoinPasswd[18];


	char mUserName[12];
	char mUserPasswd[16];


	static const char mDeviceNumber[6];
	char mToServerData[50];//通讯帧的数据部分
	char mToServerSensorData[2];//传感器数据，模拟数据值
	char mToServerComment[10];//通讯帧注释部分
	char mDataTemp[1024];//接收的wifi信息、发送信息时的存放处

	Protocol::ToServer mToServer;
	Protocol::Switch mSwitch;
	Protocol::Sensor mSensor;
	Protocol::Door mDoor;
	Protocol::SignIn mSignIn;
	Protocol::KeepAlive mKeepAlive;
	
	short Decode(void* *data,Protocol::OperationType* );
	
	bool mIsAlive;
	float mTimeReceivedKeepAlive;
	
	
	bool mDoorOn;
};


#endif
