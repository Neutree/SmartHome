#ifndef _F103_ADC_S_H_
#define _F103_ADC_S_H_


#include "stm32f10x.h"
#include "stm32f10x_gpio.h"
#include "stm32f10x_rcc.h"
#include "stm32f10x_tim.h"
#include "stm32f10x_adc.h"
#include "stm32f10x_dma.h"
/**
 *
 *                        ADC端口 和 STM32f103c8t6板子的对应引脚
 *                      （stm32f10x系列板若有以下引脚也可兼容使用）
 *                          PA0---ADC12_IN0   PB0---ADC12_IN8
 *                          PA1---ADC12_IN1   PB1---ADC12_IN9
 *                          PA2---ADC12_IN2   PC0---ADC12_IN10
 *                          PA3---ADC12_IN3   PC1---ADC12_IN11
 *                          PA4---ADC12_IN4   PC2---ADC12_IN12
 *                          PA5---ADC12_IN5   PC3---ADC12_IN13
 *                          PA6---ADC12_IN6   PC4---ADC12_IN14
 *                          PA7---ADC12_IN7   PC5---ADC12_IN15
 *
 */

class ADC
{
	public:

	/**
	 *@brief 获得电池电压值的函数
	 *@param channel	  选择哪个通道；例如GetPowerAdc(1);//选择的通道1
	 */
		void InitAdcDma(uint8_t* channelArr);
	
	/**
	 *@brief 从DMA获得的ADC原始值值存放的数组
	 */
	
		uint16_t  mAdcPrimordialValue[10];//先存储10个通道的值，后面再增加
	
	/**
	 *@brief 构造函数
	 *@param channelv*	  选择哪个通道；例如ADC adc(0);//初始化通道0
	 *																		 ADC adc2(1,4,7,9)//初始化通道1,4,7,9
	 */
		ADC(uint8_t channelv0=255,
				uint8_t channelv1=255,
				uint8_t channelv2=255,
				uint8_t channelv3=255,
				uint8_t channelv4=255,
				uint8_t channelv5=255,
				uint8_t channelv6=255,
				uint8_t channelv7=255,
				uint8_t channelv8=255,
				uint8_t channelv9=255)
			{
				
			/*初始化成员变量区域*/
				
				maxAdcChannel = 10;
				mGpio					= mGpioArr;
				mPin					= mPinArr;
				mAdcChannel		= mAdcChannelArr;
				
			/*******************/
				ChannelToGpio(channelv0,
											channelv1,
											channelv2,
											channelv3,
											channelv4,
											channelv5,
											channelv6,
											channelv7,
											channelv8,
											channelv9);
				InitAdcDma(mAdcChannel);
			}

	private:

		uint8_t						 maxAdcChannel;		//最多的通道数
		GPIO_TypeDef**	 	 mGpio;						//存储GPIO组数组
		uint16_t* 				 mPin;						//存储GPIO组相应的Pin数组
		u8*								 mAdcChannel;			//存储需要初始化的通道，以255标志结束；
		u8 								 mAdcTotal;				//存储一共有多少个有效通道
	
		GPIO_TypeDef* 		 mGpioArr[10];
		uint16_t					 mPinArr[10];
		uint8_t						 mAdcChannelArr[10];
	/**
	 *@brief 将选择的通道对应到真实的GPIO和ADC通道
	 *@param channelv*	  选择哪个通道,范围在0~9；例如		ChannelToGpio(1,3,4)
	 */	
		void ChannelToGpio(					 uint8_t channelv0=255,
																 uint8_t channelv1=255,
																 uint8_t channelv2=255,
																 uint8_t channelv3=255,
																 uint8_t channelv4=255,
																 uint8_t channelv5=255,
																 uint8_t channelv6=255,
																 uint8_t channelv7=255,
																 uint8_t channelv8=255,
																 uint8_t channelv9=255);						//将通道号对应到GPIO
};

#endif

