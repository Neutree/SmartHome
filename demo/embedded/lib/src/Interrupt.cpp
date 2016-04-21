/**
  *@file Interrupt.h
  *@date 2015-10-27
  *@author 
  *@breif  interrupt management file
  *
  */

#include "Interrupt.h"

USART *pUSART1 = 0;
USART *pUSART2 = 0;
USART *pUSART3 = 0;

void USART1_IRQHandler(void)
{
	if(pUSART1)
		pUSART1->Irq();
}
void USART2_IRQHandler(void)
{
	if(pUSART2)
		pUSART2->Irq();
}
void USART3_IRQHandler(void)
{
	if(pUSART3)
		pUSART3->Irq();
}

void DMA1_Channel4_IRQHandler()
{	
	if(pUSART1)
		pUSART1->DmaIrq();
}

void DMA1_Channel7_IRQHandler()
{	
	if(pUSART2)
		pUSART2->DmaIrq();
}


void DMA1_Channel2_IRQHandler()
{	
	if(pUSART3)
		pUSART3->DmaIrq();
}




/*定时器中断*****************************************************/

#if USE_CAPTURE
Capture *pTimer1 = 0;
Capture *pTimer2 = 0;
Capture *pTimer3 = 0;
Capture *pTimer4 = 0;
#endif

#if USE_TIM1_IRQ
void TIM1_UP_IRQHandler(void)
{ 
	TIM_ClearITPendingBit(TIM1, TIM_FLAG_Update); //清除中断标识
	#if USE_SERVE
	Timer1_IRQ(); //中断服务程序，用户自行编写
	#endif
}


	#if 	USE_CAPTURE 
	void TIM1_CC_IRQHandler(void)		//tim1的捕获比较中断
	{
		pTimer1->IRQ();
	}
	#endif

	
	void TIM1_TRG_COM_IRQHandler(void)	//tim1的触发
	{}
	void TIM1_BRK_IRQHandler(void)		//tim1的暂停中断
	{}
#endif


#if USE_TIM2_IRQ
void TIM2_IRQHandler(void)  
{
	if (TIM_GetITStatus(TIM2, TIM_IT_Update) != RESET)  //检查TIM2更新中断发生与否
		{
			TIM_ClearITPendingBit(TIM2, TIM_IT_Update);  //清除TIM2更新中断标志 
			#if USE_SERVE
			Timer2_IRQ();
			#endif
		}
	#if USE_CAPTURE
	 if(pTimer2)
	 pTimer2->IRQ();
	#endif
		
}
#endif

#if USE_TIM3_IRQ
void TIM3_IRQHandler(void)  
{
	if (TIM_GetITStatus(TIM3, TIM_IT_Update) != RESET)  //检查TIM3更新中断发生与否
	{
		TIM_ClearITPendingBit(TIM3, TIM_IT_Update);  //清除TIM3更新中断标志 
		#if USE_SERVE
		Timer3_IRQ();
		#endif
	}
	
	#if USE_CAPTURE
	 if(pTimer3)
	 pTimer3->IRQ();
	#endif
	 
}
#endif

#if USE_TIM4_IRQ
void TIM4_IRQHandler(void)  
{
	if (TIM_GetITStatus(TIM4, TIM_IT_Update) != RESET)  //检查TIM4更新中断发生与否
	{
	 TIM_ClearITPendingBit(TIM4, TIM_IT_Update);  //清除TIM4更新中断标志
		
	 #if USE_SERVE
	 Timer4_IRQ();
	 #endif		
	}
	
	#if USE_CAPTURE
	 if(pTimer4)
	 pTimer4->IRQ();
	#endif	
}
#endif	
