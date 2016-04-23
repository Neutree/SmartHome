#include "app.h"

App::App()
{
	
}

void App::TimerInterrupt()
{
	stepMotor.MotorConfig();
}


void App::Init()
{
	//关闭LED
	light.SetDuty(1,0,true);
	//步进电机使能
	stepMotor.Enable();
	//设置步进电机速度，值越小速度越大
	stepMotor.Run(false,1);
}
void App::loop()
{
	com1<<"light:"<<mLightSensor<<"\n";
	com1<<"motor:";
	if(stepMotor.IsEnable())
		com1<<"enable, speed:"<<stepMotor.GetSpeed()<<"  Is direction clockwise:"<<stepMotor.GetDirection()<<"\n";
	else
		com1<<"disable\n";
	
	TaskManager::DelayMs(500);
}

