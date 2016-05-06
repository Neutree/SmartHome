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
#include "stdlib.h"
#include "CRC.h"
App app;

void Timer2_IRQ();

// ----------------------------------------------------------------------------

int
main(int argc, char* argv[])
{
	app.Init();
	// Infinite loop
	while (1)
	{
		//光强传感器值，大概范围0~300
		lightSensor.Converted();
		app.mLightSensor = lightSensor.mConvertedVoltage[0]*100;
		
		//循环 
		//app.loop();
		char a[]="123456789";
		short b=0,c=0;
		b = CRC16Calculate((const uint8_t*)a,9);
		c = CRC16Calculate((const uint8_t*)a,9);
		com1<<(int)(b>>8&0x00ff)<<"\t"<<(b&0x00ff)<<"\t\t"<<(int)(c>>8&0x00ff)<<"\t"<<(c&0x00ff)<<"\n";
		TaskManager::DelayS(3);
	}
}

//定时器2 中断，1ms中断一次，用来放步进电机的中断处理函数
void Timer2_IRQ()
{
	app.TimerInterrupt();
}


// ----------------------------------------------------------------------------
