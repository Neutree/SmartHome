#include "hardware.h"




//step motor
GPIO motor_gpioa(GPIOB,6),motor_gpiob(GPIOB,7),motor_gpioc(GPIOB,8),motor_gpiod(GPIOB,9);
StepMotor stepMotor(motor_gpioa,motor_gpiob,motor_gpioc,motor_gpiod);
Timer timer(TIM2,0,1,0);//定时器2,1ms中断一次

//light
GPIO ledGpio(GPIOA,0);
LED light(ledGpio,false);//灯


//usart
USART com1(1,115200,true);


//light sensor
ADC adc(1);
Voltage lightSensor(adc.mAdcPrimordialValue,1);//光强传感器


//wifi
USART com2(2,115200,false);
Socket_esp8266 wifi(com2);//wifi

