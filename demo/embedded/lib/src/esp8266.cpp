#include "esp8266.h"


esp8266::esp8266(USART &usart, uint32_t baudRate )
:mUsart(usart)
{
	if(baudRate!=0)
	{
		usart.SetBaudRate(baudRate);
	}
}

bool esp8266::Kick(void)
{
	mUsart<<"AT\r\n";
	return RecvFind("OK");
}
bool esp8266::Restart(void)
{
	mUsart<<"AT+RST\r\n";
	return RecvFind("OK");
}
bool esp8266::GetVersion(char* version)
{
	mUsart<<"AT+GMR\r\n";
	return RecvFindAndFilter("OK", "\r\r\n", "\r\n\r\nOK", version);
}
bool esp8266::SetEcho(bool echo)
{
	mUsart<<"ATE"<<(echo?1:0)<<"\r\n";
	return RecvFind("OK");
}
bool esp8266::Restore(void)
{
	mUsart<<"AT+RESTORE"<<"\r\n";
	return RecvFind("OK");
}
bool esp8266::SetUart(uint32_t baudrate,esp8266_pattern pattern)
{
	if(pattern>esp8266_PATTERN_DEF)
		return false;
	if(pattern == esp8266_PATTERN_CUR)
	{
		mUsart<<"AT+UART_CUR=";
	}
	else if(pattern == esp8266_PATTERN_DEF)
	{
		mUsart<<"AT+UART_DEF=";
	}
	else
	{
		mUsart<<"AT+UART=";
	}
	mUsart<<(int)baudrate<<",8,1,0,0\r\n";
	if(RecvFind("OK"))
		return true;
	return false;
}
bool esp8266::SetMode(esp8266_MODE mode,esp8266_pattern pattern)
{
	if(pattern>esp8266_PATTERN_DEF)
		return false;
		
	if(pattern == esp8266_PATTERN_CUR)
	{
		mUsart<<"AT+CWMODE_CUR=";
	}
	else if(pattern == esp8266_PATTERN_DEF)
	{
		mUsart<<"AT+CWMODE_DEF=";
	}
	else
	{
		mUsart<<"AT+CWMODE=";
	}
	mUsart<<(int)mode<<"\r\n";
	if(!ReceiveAndWait("OK", "ERROR"))
		return false;
	if(strstr(mReceiveBuffer,"OK"))
		return true;
	return false;
}
bool esp8266::SetMUX(bool isEnableMUX)
{
	mUsart<<"AT+CIPMUX="<<(isEnableMUX?1:0)<<"\r\n";							
	return RecvFind("OK");
}

bool esp8266::SetApParam(char* apSetSsid,char* apSetPasswd,unsigned char channel,esp8266_ENCRYPTION encryption,esp8266_pattern pattern)
{
	if(pattern>esp8266_PATTERN_DEF)
		return false;
		
	if(pattern == esp8266_PATTERN_CUR)
	{
		mUsart<<"AT+CWJAP_CUR=";
	}
	else if(pattern == esp8266_PATTERN_DEF)
	{
		mUsart<<"AT+CWJAP_DEF=";
	}
	else
	{
		mUsart<<"AT+CWJAP=";
	}
	mUsart<<"\""<<apSetSsid<<"\",\""<<apSetPasswd<<"\","<<channel<<","<<(int)encryption<<"\r\n";
	if(!ReceiveAndWait("OK", "ERROR",10))
		return false;
	if(strstr(mReceiveBuffer,"OK"))
		return true;
	return false;
}
bool esp8266::JoinAP(char* apJoinSsid,char* apJoinPasswd,esp8266_pattern pattern)
{
	if(pattern>esp8266_PATTERN_DEF)
		return false;
		
	if(pattern == esp8266_PATTERN_CUR)
	{
		mUsart<<"AT+CWJAP_CUR=";
	}
	else if(pattern == esp8266_PATTERN_DEF)
	{
		mUsart<<"AT+CWJAP_DEF=";
	}
	else
	{
		mUsart<<"AT+CWJAP=";
	}
	mUsart<<"\""<<apJoinSsid<<"\",\""<<apJoinPasswd<<"\"\r\n";
	if(!ReceiveAndWait("OK", "ERROR",20))
		return false;
	if(strstr(mReceiveBuffer,"OK"))
		return true;
	return false;
}

bool esp8266::CreateTCPMutipleMode(unsigned char muxID,char* ipAddr,short port,Socket_Type socketType)
{
	char type[4]="TCP";
	if(socketType==Socket_Dgram)
	{
		type[0]='U';
		type[1]='D';
	}
	mUsart<<"AT+CIPSTART="<<muxID<<",\""<<type<<"\",\""<<ipAddr<<"\","<<port<<"\r\n";							
	return RecvFind("OK");
}
bool esp8266::SendMultipleMode(unsigned char muxID,char* data, unsigned int num)
{
	mUsart<<"AT+CIPSEND="<<muxID<<","<<(int)num<<"\r\n";
	if(!RecvFind("OK\r\n> "))
		return false;
	mUsart.SendData((uint8_t*)data,num);
	return RecvFind("OK\r\n");
}










bool esp8266::ReceiveAndWait(const char* targetString,unsigned char timeOut)
{
		u8 temp;
		mReceiveBufferIndex = 0;
		double tartTime=TaskManager::Time();
		while((TaskManager::Time()-tartTime)<timeOut)
		{
			while(mUsart.ReceiveBufferSize()>0)
			{
				mUsart.GetReceivedData(&temp,1);//从串口接收缓冲区接收数据
				if(temp=='\0')
					continue;
				mReceiveBuffer[mReceiveBufferIndex++] = temp;//放入esp的缓冲区
				
			}
			
			if(strstr(mReceiveBuffer,targetString))
				return true;	
				
		}
		if(mReceiveBufferIndex>0)//接收到了数据，加上结束标志
		{
			mReceiveBuffer[mReceiveBufferIndex] = '\0';
		}
		return false;
}
bool esp8266::ReceiveAndWait(char const* targetString,const char* targetString2,unsigned char timeOut)
{
		u8 temp;
		mReceiveBufferIndex = 0;
		double tartTime=TaskManager::Time();
		while((TaskManager::Time()-tartTime)<timeOut)
		{
			while(mUsart.ReceiveBufferSize()>0)
			{
				mUsart.GetReceivedData(&temp,1);//从串口接收缓冲区接收数据
				if(temp=='\0')
					continue;
				mReceiveBuffer[mReceiveBufferIndex++] = temp;//放入esp的缓冲区
				
			}
			
			if(strstr(mReceiveBuffer,targetString) ||
			   strstr(mReceiveBuffer,targetString2))
				return true;	
				
		}
		if(mReceiveBufferIndex>0)//接收到了数据，加上结束标志
		{
			mReceiveBuffer[mReceiveBufferIndex] = '\0';
		}
		return false;
}
bool esp8266::RecvFind(char const *target, unsigned char timeout)
{
	if(!ReceiveAndWait((char*)target,timeout))
		return false;
	return true;
}
bool esp8266::RecvFindAndFilter(char const *target, char const * begin, char const * end, char* Data, float timeout)
{
	if(!ReceiveAndWait((char*)target,timeout))
		return false;
	char* index1=strstr(mReceiveBuffer,begin);
	char* index2=strstr(mReceiveBuffer,end);

	if(index1&&index2)
	{
		index1+=strlen(begin);
		*index2='\0';
		strcpy(Data,index1);
		return true;
	}
	return false;
}





