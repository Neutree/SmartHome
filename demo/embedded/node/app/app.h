#ifndef __APP_H
#define __APP_H

#include "hardware.h"

class App
{
public:
	//光感值,大概范围0~300，值越小光越强
	u8 mLightSensor;

	App();
	void TimerInterrupt();
	void Init();
	void loop();
private:
	
	char mApSetName[15];
	char mApSetPasswd[18];
	char mApJoinName[15];
	char mApJoinPasswd[18];
};


#endif
