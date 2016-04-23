#include "hardware.h"


PWM light(TIM2,true,false,false,false,500);//灯


GPIO motor_gpioa(GPIOB,6),motor_gpiob(GPIOB,7),motor_gpioc(GPIOB,8),motor_gpiod(GPIOB,9);
StepMotor stepMotor(motor_gpioa,motor_gpiob,motor_gpioc,motor_gpiod);
USART com1(1,115200,true);

Timer timer(TIM2,0,1,0);//定时器2,1ms中断一次

ADC adc(1);
Voltage lightSensor(adc.mAdcPrimordialValue,1);//光强传感器


