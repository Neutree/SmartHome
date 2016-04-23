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
	
};


#endif
