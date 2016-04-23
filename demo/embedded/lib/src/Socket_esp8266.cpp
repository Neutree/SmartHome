#include "Socket_esp8266.h"

Socket_esp8266::Socket_esp8266(USART& usart)
:esp8266(usart),mUsart(usart),mApSetName("SmartHome"),mApSetPasswd("1208077207"),mApJoinName("ICanHearYou"),mApJoinPasswd("1208077207")
{
	
	
}
bool Socket_esp8266::Init()
{
	if(!Kick())//检查连接
		return false;
	SetEcho(false);//关闭回响
	
	SetMode(esp8266_MODE_STATION_AP,esp8266_PATTERN_DEF);//设置为station+ap模式
	SetMUX(true);
	SetApParam(mApSetName,mApSetPasswd,esp8266_PATTERN_DEF);
	JoinAP(mApJoinName,mApJoinPasswd,esp8266_PATTERN_DEF);//加入AP
	
	return true;
}
bool Socket_esp8266::Connect(char* ipAddr,short port,Socket_Type socketType,Socket_Protocol socketProtocol)
{
	return CreateTCPMutipleMode(4,ipAddr,port,socketType);
}
bool Socket_esp8266::Write(char* data,unsigned int num)
{
	return SendMultipleMode(4,data,num);
}
unsigned int Socket_esp8266::Read(char* data)
{
	return 0;
}
bool Socket_esp8266::IsAlive()
{
	return false;
}
bool Socket_esp8266::Close()
{
	return false;	
}
	


