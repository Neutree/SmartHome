#ifndef __SOCKET_H
#define __SOCKET_H
#include "stdbool.h"

class Socket
{
public :
	enum Socket_Type{Socket_Stream,Socket_Dgram};
	enum Socket_Protocol{Socket_Protocol_IPV4,Socket_Protocol_IPV6};
	
	Socket(){}
	virtual bool connect(long int ipAddr,short int port,Socket_Type socketType,Socket_Protocol socketProtocol) = 0;
	virtual bool Write(char* data,unsigned int num) = 0;
	virtual unsigned int Read(char* data) = 0;
	virtual bool IsAlive() = 0;
	virtual bool Close() = 0;
private:
	long int mIPAddr;
	short int mPort;
	
};

#endif


