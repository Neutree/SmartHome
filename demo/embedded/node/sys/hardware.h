#ifndef __HARDWARE_H_
#define __HARDWARE_H_

#include "LED.h"
#include "GPIO.h"
#include "F103_ADC_S.h"
#include "Voltage.h"
#include "F103_PWM.h"
#include "StepMotor.h"
#include "USART.h"
#include "Timer.h"

extern PWM light;//灯


//GPIO motor_gpioa,motor_gpiob(GPIOB,7),motor_gpioc(GPIOB,8),motor_gpiod(GPIOB,9);
extern StepMotor stepMotor;
extern USART com1;

extern Timer timer;//定时器2,1ms中断一次

extern ADC adc;
extern Voltage lightSensor;//光强传感器


#endif


