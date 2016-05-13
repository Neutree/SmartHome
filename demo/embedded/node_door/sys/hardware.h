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
#include "Socket_esp8266.h"

//light
extern GPIO ledGpio;
extern LED light;

//door
extern GPIO doorGpio;
extern LED door;


////GPIO motor_gpioa,motor_gpiob(GPIOB,7),motor_gpioc(GPIOB,8),motor_gpiod(GPIOB,9);
//extern StepMotor stepMotor;
extern USART com1;

//extern Timer timer;//定时器2,1ms中断一次

//extern ADC adc;
//extern Voltage sensor;//光强传感器

extern USART com2;
extern Socket_esp8266 wifi;//wifi


#endif


