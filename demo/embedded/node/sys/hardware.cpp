#include "hardware.h"




//步进电机
GPIO motor_gpioa(GPIOB,6),motor_gpiob(GPIOB,7),motor_gpioc(GPIOB,8),motor_gpiod(GPIOB,9);
StepMotor stepMotor(motor_gpioa,motor_gpiob,motor_gpioc,motor_gpiod);
Timer timer(TIM2,0,1,0);//定时器2,1ms中断一次，用来定时改变步进电机的输出

//light
GPIO ledGpio(GPIOA,0);
LED light(ledGpio,false);//灯


//串口
USART com1(1,115200,true);


//光感
ADC adc(1,6,8,9);
Voltage lightSensor(adc.mAdcPrimordialValue,4);//光强传感器


//wifi
USART com2(2,115200,false);
Socket_esp8266 wifi(com2);//wifi

