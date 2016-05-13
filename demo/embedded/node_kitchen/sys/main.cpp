/**
  *main.cpp
  *
  *@author neucrack
  *@date 2016-04-21
  *
  */

#include "hardware.h"
#include "app.h"
#include "Interrupt.h"
App app;


// ----------------------------------------------------------------------------

int
main(int argc, char* argv[])
{
	app.Init();
	// Infinite loop
	while (1)
	{
		//光强传感器值，大概范围0~300
		sensor.Converted();
		app.mFireSensor = sensor.mConvertedVoltage[0]*100;
		app.mSmokeSensor = sensor.mConvertedVoltage[1]*100;
		//循环 
		app.loop();
	}
}


// ----------------------------------------------------------------------------
