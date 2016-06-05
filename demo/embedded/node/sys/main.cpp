
#include "hardware.h"
#include "app.h"
#include "Interrupt.h"
App app;

void Timer2_IRQ();

// ----------------------------------------------------------------------------

int
main(int argc, char* argv[])
{
	TaskManager::DelayS(3);
	//系统初始化
	app.Init();
	//开启定时器
	timer.Start();
	//循环体
	while (1)
	{
		//将光传感器的值采集出来并且放到app对象的变量中
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
