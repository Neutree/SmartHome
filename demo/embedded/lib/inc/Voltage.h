#ifndef _VOLTAGE_H_
#define	_VOLTAGE_H_

#include "stm32f10x.h"


class Voltage
{
	private:
		/**
		 *@param  	mPrimordialVoltage     记录原生电压值的地址，方便以后计算
		 *@param  	mToltalChannel     		 记录一共有多少个通道，方便循环
		 */
			uint16_t* mPrimordialVoltageAddr;
			uint8_t   mToltalChannel;
	public:
		
	
		/**
		 *@brief		将原生值转换成一个float的电压值,0~3.3
		 *@param 		primordialVoltage 电压的原始值,需要12位的
		 */
		float 		Converted();
	////////////////////////////////////////////////////////////////////////////////
	///////最多10个值记录原始值转换成float的电压值,范围在0~3.3V,但是必须在调用Converted函数之后才能更新值
	///////////////////////////////////////////////////////////////////////////////
		float 		mConvertedVoltage[10];

		/**
		 *@brief初始化的时候传入ADC原生值
		 *@param primordialVoltage 	电压的原始值,需要12位的
		 *@param channelNum					通道的总数
		 */
		Voltage(uint16_t* primordialVoltage,uint8_t channelNum);

		
};



#endif


