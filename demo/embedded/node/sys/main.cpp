/**
  *main.cpp
	*
	*@author neucrack
	*@date 2016-04-21
	*
	*/

#include <stdio.h>
#include <stdlib.h>

#include "LED.h"
#include "GPIO.h"
#include "F103_ADC_S.h"
#include "Voltage.h"
#include "F103_PWM.h"
#include "StepMotor.h"



//GPIO lightGPIO(GPIOA,0);
//LED light(lightGPIO,false);
PWM light(TIM2,true,false,false,false,500);//灯
ADC adc(1);
Voltage lightSensor(adc.mAdcPrimordialValue,1);//光强传感器
GPIO motor_gpioa(GPIOB,6),motor_gpiob(GPIOB,7),motor_gpioc(GPIOB,8),motor_gpiod(GPIOB,9);
StepMotor stepMotor(motor_gpioa,motor_gpiob,motor_gpioc,motor_gpiod);


// ----------------------------------------------------------------------------

int
main(int argc, char* argv[])
{

	//light.Off();
	light.SetDuty(1,0);
	// Infinite loop
	while (1)
	{
//		lightSensor.Converted();
////		float result=lightSensor.mConvertedVoltage[0];
//		int result=0;
//		result=lightSensor.mConvertedVoltage[0]*100;
//		//light.On();
//		light.SetDuty(1,100);
//		TaskManager::DelayMs(500);
//		//light.Off();
//		light.SetDuty(1,0);
//		TaskManager::DelayMs(500);
		stepMotor.MotorConfig();
		TaskManager::DelayMs(3);
	}
}



// ----------------------------------------------------------------------------
