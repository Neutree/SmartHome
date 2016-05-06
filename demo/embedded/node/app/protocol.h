#ifndef __PROTOCOL_H
#define __PROTOCOL_H

class Protocol
{
public:
	enum OperationType{OperationType_Control=1,OperationType_Ack=2,OperationType_Ask=3};

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
		static short dataType;
		char status;
		short commentLength;
		char* comment;
	};
	struct Sensor
	{
		static short dataType;
		long int name;
		char datalength;
		char* data;
		short commentLength;
		char* comment;
	};
	struct Door
	{
		static short dataType;
		char userName[11];
		char passwd[16];
		short commentLength;
		char* comment;
	};
	struct SignIn
	{
		static short dataType;
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
*/	struct KeepAlive
	{
		static short dataType;
		char type;
	};
	
public:
	Protocol();
	
private:

};


#endif 

