
# ifndef __STEP_MOTOR_H
# define __STEP_MOTOR_H
# include "GPIO.h"

class StepMotor
{
private:
	bool mEnable;
	static const char mCode[8];
	unsigned short mCount;
	short mSpeed;
	bool mIsDirectionClockwise;
	GPIO& mGPIOa,mGPIOb,mGPIOc,mGPIOd;

public:
	StepMotor(GPIO& gpioa,GPIO& gpiob,GPIO& gpioc,GPIO& gpiod);
	void Enable();
	void Disable();
	void SetSpeed(short speed);
	/**
	 *定时调用
	 *
	 */
	void MotorConfig();
	void Run(bool isDirectionClockwise,short speed);

};



# endif
