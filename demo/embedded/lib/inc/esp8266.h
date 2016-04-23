#ifndef __ESP8266_H
#define __ESP8266_H
#include "socket.h"
#include "USART.h"

class esp8266 :Socket
{
public:
	esp8266(USART& usart);
	virtual bool connect(long int ipAddr,short int port,Socket_Type socketType,Socket_Protocol socketProtocol);
	virtual bool Write(char* data,unsigned int num);
	virtual unsigned int Read(char* data);
	virtual bool IsAlive();
	virtual bool Close();
private:
	USART& mUsart;
};

#endif

