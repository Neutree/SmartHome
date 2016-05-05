#ifndef __PROTOCOL_H
#define __PROTOCOL_H

class Protocol
{
public:
	struct ToServer
	{
		short head;
		short dataType;
		char operationType;
		unsigned char deviceNumber[6];
		unsigned char UTC[6];
		short dataLength;
		char* data;
		short crc16;
	};
	struct Switch
	{
		char status;
		short commentLength;
		char* comment;
	};
	struct Sensor
	{
		long int name;
		char datalength;
		char* data;
		short commentLength;
		char* comment;
	};
	struct Door
	{
		char userName[11];
		char passwd[16];
		short commentLength;
		char* comment;
	};
	struct SignIn
	{
		char userName[11];
		char passwd[16];
		char type;
	};
/*	struct SignUp
	{
		char userName[11];
		char nickName[10];
		char passwd[4];
	};
*/	struct HeartBeat
	{
		char type;
	};
	
public:
	Protocol();
	
private:

};


#endif 

