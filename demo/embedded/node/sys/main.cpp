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

void Timer2_IRQ();

// ----------------------------------------------------------------------------

int
main(int argc, char* argv[])
{
	app.Init();
	timer.Start();
	// Infinite loop
	while (1)
	{
		//光强传感器值，大概范围0~300
		lightSensor.Converted();//ADC转换
		app.mLightSensor = lightSensor.mConvertedVoltage[3]*100;//获取光感值
		//循环 
		app.loop();
	}
}

//定时器2 中断，1ms中断一次，用来放步进电机的中断处理函数
void Timer2_IRQ()
{
	app.TimerInterrupt();
}


// ----------------------------------------------------------------------------
