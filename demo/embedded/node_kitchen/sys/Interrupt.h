/**
  *@file Interrupt.h
  *@date 2015-10-27
  *@author 
  *@breif  interrupt management file
  *
  */

#ifndef __INTERRUPT_H
#define __INTERRUPT_H

#include "USART.h"


extern USART *pUSART1;
extern USART *pUSART2;
extern USART *pUSART3;

extern "C"{
	
void USART1_IRQHandler(void);
void USART2_IRQHandler(void);
void USART3_IRQHandler(void);
void DMA1_Channel4_IRQHandler(void);
void DMA1_Channel7_IRQHandler(void);
void DMA1_Channel2_IRQHandler(void);

}
	
/***************定时器中断函数****************************************/

/*宏定义-----0关----- 1开---------------------------*/
#define USE_TIM1_IRQ   	0		//使用定时器1
#define USE_TIM2_IRQ	0
#define USE_TIM3_IRQ	0	
#define USE_TIM4_IRQ	0
#define USE_CAPTURE   	0	    //是否使用捕获
#define USE_SERVE    	1		//是否准备自己编写服务函数

/*中断服务函数------------------------------------------*/
#if USE_TIM1_IRQ	
  #if USE_SERVE
	void Timer1_IRQ();//定时器1更新中断服务程序，在主函数由用户自己编写类容	
  #endif
#endif

#if USE_TIM2_IRQ	
  #if USE_SERVE
	void Timer2_IRQ();
  #endif
#endif

#if USE_TIM3_IRQ	
  #if USE_SERVE
	void Timer3_IRQ();
  #endif
#endif

#if USE_TIM4_IRQ	
  #if USE_SERVE
	void Timer4_IRQ();
  #endif
#endif

/*捕获------------------------------------------*/
#if USE_CAPTURE
#include "Capture.h"
#endif

#if USE_CAPTURE
extern Capture *pTimer1;
extern Capture *pTimer2;
extern Capture *pTimer3;
extern Capture *pTimer4;	
#endif


/*中断入口函数-----------------------------------*/
extern "C"{

#if USE_TIM1_IRQ
	void TIM1_UP_IRQHandler(void);		//tim1的刷新中断和通讯中断。

	void TIM1_BRK_IRQHandler(void); 	//tim1的暂停中断
	void TIM1_TRG_COM_IRQHandler(void);	//tim1的触发

	#if USE_CAPTURE
		void TIM1_CC_IRQHandler(void);		//tim1的捕获比较中断
	#endif
#endif

//定时器2中断函数
#if USE_TIM2_IRQ
	void TIM2_IRQHandler(void);
#endif

//定时器三中断函数
#if USE_TIM3_IRQ
	void TIM3_IRQHandler(void);
#endif

//定时器四中断函数
#if USE_TIM4_IRQ
	void TIM4_IRQHandler(void);
#endif

}



#endif

