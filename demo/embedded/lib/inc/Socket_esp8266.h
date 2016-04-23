#ifndef __SOCKET_ESP8266_H
#define __SOCKET_ESP8266_H
#include "socket.h"
#include "esp8266.h"
#include "USART.h"

class Socket_esp8266 :esp8266,Socket
{
public:
	Socket_esp8266(USART& usart);
	bool Init();
	virtual bool Connect(char* ipAddr,short port,Socket_Type socketType,Socket_Protocol socketProtocol=Socket_Protocol_IPV4);
	virtual bool Write(char* data,unsigned int num);
	virtual unsigned int Read(char* data);
	virtual bool IsAlive();
	virtual bool Close();
private:
	USART& mUsart;
	char mApSetName[15];
	char mApSetPasswd[18];
	char mApJoinName[15];
	char mApJoinPasswd[18];

};

#endif

