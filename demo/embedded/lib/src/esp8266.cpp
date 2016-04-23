#include "esp8266.h"

esp8266::esp8266(USART& usart)
:mUsart(usart)
{
	
}
bool esp8266::connect(long int ipAddr,short int port,Socket_Type socketType,Socket_Protocol socketProtocol)
{
	return false;
}
bool esp8266::Write(char* data,unsigned int num)
{
	return false;
}
unsigned int esp8266::Read(char* data)
{
	return 0;
}
bool esp8266::IsAlive()
{
	return false;
}
bool esp8266::Close()
{
	return false;	
}
	


