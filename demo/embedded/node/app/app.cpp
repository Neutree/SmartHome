#include "app.h"

App::App()
:mApSetName("SmartHome"),mApSetPasswd("1208077207"),
mApJoinName("ICanHearYou"),mApJoinPasswd("1208077207")
{
	
}

void App::TimerInterrupt()
{
	stepMotor.MotorConfig();
}


void App::Init()
{
	//关闭LED
	light.SetDuty(1,5,true);
	//步进电机使能
	stepMotor.Disable();
	//设置步进电机速度，值越小速度越大
	stepMotor.Run(true,2);
	
	//WIFI initialize
	if(!wifi.Kick())//检查连接
	{
		com1<<"no wifi!\n\n\n";
		return;
	}
	else
		com1<<"wifi is healthy\n";
	wifi.SetEcho(false);//关闭回响
//	wifi.SetMode(esp8266::esp8266_MODE_STATION_AP,esp8266::esp8266_PATTERN_DEF);//设置为station+ap模式
	wifi.SetMUX(false);//单连接模式
//	wifi.SetApParam(mApSetName,mApSetPasswd,esp8266::esp8266_PATTERN_DEF);//设置热点信息
//	wifi.JoinAP(mApJoinName,mApJoinPasswd,esp8266::esp8266_PATTERN_DEF);//加入AP
	light.SetDuty(1,0,true);
}
char writeData[]="test text\n\n";
char readData[1024];
void App::loop()
{
//	com1<<"light:"<<mLightSensor<<"\n";
//	com1<<"motor:";
//	if(stepMotor.IsEnable())
//		com1<<"enable, speed:"<<stepMotor.GetSpeed()<<"  Is direction clockwise:"<<stepMotor.GetDirection()<<"\n";
//	else
//		com1<<"disable\n";
	if(!wifi.Kick())
		com1<<"\n\nwifi error!!!!!!!!!!!!\n\n";
	
	wifi.Connect((char*)"192.168.191.1",8090,Socket_Type_Stream,Socket_Protocol_IPV4);
	wifi.Write(writeData,sizeof(writeData));
	unsigned short length = 0;
	length=wifi.Read(readData);
	readData[length]=0;
	com1<<"length:"<<length<<"\n";
	if(length>0)
		com1<<readData<<"\n";
	else
		com1<<"read fail\n";
	wifi.Close();
	
	TaskManager::DelayMs(500);
}

