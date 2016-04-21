/*
 * StepMotor.cpp
 *
 *  Created on: 2016年4月7日
 *      Author: Neucrack
 */
# include "StepMotor.h"

const char StepMotor::mCode[8] = {0x08,0x0c,0x04,0x06,0x02,0x03,0x01,0x09};

StepMotor::StepMotor(GPIO& gpioa,GPIO& gpiob,GPIO& gpioc,GPIO& gpiod)
:mEnable(false),mCount(0),mGPIOa(gpioa),mGPIOb(gpiob),mGPIOc(gpioc),mGPIOd(gpiod)
{
	mSpeed = 0;
}
void StepMotor::Enable()
{
	mEnable = true;
}
void StepMotor::Disable()
{
	mEnable = false;
}
void StepMotor::SetSpeed(short speed)
{
	mSpeed = speed;
}

/**
 *定时调用
 *
 */
void StepMotor::MotorConfig()
{
	static char count=0;
	++mCount;
	if(mCount>=mSpeed)
	{
		mCount=0;
		mGPIOa.SetLevel((mCode[count]&0x08)>>3);
		mGPIOb.SetLevel((mCode[count]&0x04)>>2);
		mGPIOc.SetLevel((mCode[count]&0x02)>>1);
		mGPIOd.SetLevel(mCode[count]&0x01);
		++count;
		if(count>=8)
			count = 0;
		
	}
}

void StepMotor::Run(bool isDirectionClockwise,short speed)
{
	mSpeed = speed;
	mIsDirectionClockwise = isDirectionClockwise;
}


