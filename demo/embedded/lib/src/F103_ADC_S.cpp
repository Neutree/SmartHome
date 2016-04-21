#include "F103_ADC_S.h"


#define ADC1_DR_Address ((u32)0x40012400+0x4c)/*对ADC地址进行宏定义*/
static GPIO_TypeDef* gpioArr[] = {GPIOA,GPIOB,GPIOC};
													/*Pin0,  Pin1,  Pin2,  Pin3,  Pin4,  Pin5,  Pin6,  Pin7,  Pin0,  Pin1,  Pin0,  Pin1,  Pin2,  Pin3,  Pin4,  Pin5*/
static uint16_t pinArr[] = {0x0001,0x0002,0x0004,0x0008,0x0010,0x0020,0x0040,0x0080,0x0001,0x0002,0x0001,0x0002,0x0004,0x0008,0x0010,0x0020};




/**
 *@brief 将通道与引脚对引起来
 *@param channel 用户选择的通道
 */
void ADC::ChannelToGpio(				uint8_t channelv0,
																uint8_t channelv1,
																uint8_t channelv2,
																uint8_t channelv3,
																uint8_t channelv4,
																uint8_t channelv5,
																uint8_t channelv6,
																uint8_t channelv7,
																uint8_t channelv8,
																uint8_t channelv9)						//将通道号对应到GPIO
{
	/*先确定GPIO的ABC组*/
	
	uint8_t 	i = 0;																																	//记录计数次数
	uint8_t		j = 0;																																	//记录成员变量mAdcChannel里面的值
	uint8_t		a[10];
	uint8_t*	channelArr = a;																															//通道数组
	GPIO_InitTypeDef GPIO_InitStructure;																							//定义用于初始化GPIO的结构体
	uint16_t pinA = 0x0000;																																		//存储A组GPIO的引脚
	uint16_t pinB = 0x0000;																																		//存储B组GPIO的引脚
	
	*channelArr 		= channelv0;
	*(channelArr+1) = channelv1;
	*(channelArr+2) = channelv2;
	*(channelArr+3) = channelv3;
	*(channelArr+4) = channelv4;
	*(channelArr+5) = channelv5;
	*(channelArr+6) = channelv6;
	*(channelArr+7) = channelv7;
	*(channelArr+8) = channelv8;
	*(channelArr+9) = channelv9;																											//将传入的虚拟通道进行存储
	
	while(i++ != 10)																																	//进行10次判断是否传入的通道是有效的
	{
		if(*(channelArr+i-1) <= maxAdcChannel || *(channelArr+i-1) >= 0)								//如果是0~maxAdcChannel以外的值，则不是有效值
		{
			
			*(mAdcChannel+j) = *(channelArr+i-1);																					//将记录的通道值存储在成员变量指针中
			if(*(mAdcChannel+j) <8 )
			{
				*(mGpio+j) = gpioArr[0];																										//存储Pin对应的GPIO组
				*(mPin+j)  = pinArr[*(mAdcChannel+j)];																			//存储Pin
				pinA |=  *(mPin+j);																													//将每个GPIO组的pin确定下来
				
			}else if(*(mAdcChannel+j) == 8 || *(mAdcChannel+j) == 9)
				{
					*(mGpio+j) = gpioArr[1];																									//存储Pin对应的GPIO组
					*(mPin+j) = pinArr[*(mAdcChannel+j)];																			//存储Pin
					pinB |=  *(mPin+j);																												//将每个GPIO组的pin确定下来
				}
			j++;
		}
	}
	mAdcTotal = j;																																		//存储有效的通道数
	i = 0;
	j = 0;
	
	if(pinA != 0)
	{
		RCC_APB2PeriphClockCmd(RCC_APB2Periph_GPIOA,ENABLE);															//open the PinA Timer
		GPIO_InitStructure.GPIO_Pin = pinA;																								//传入Pin
		GPIO_InitStructure.GPIO_Mode = GPIO_Mode_AIN;																			//选择模拟输入方式
		GPIO_Init(GPIOA,&GPIO_InitStructure);																							//选择具体的GPIO组进行初始化
	}
	if(pinB != 0)
	{
		RCC_APB2PeriphClockCmd(RCC_APB2Periph_GPIOB,ENABLE);															//open the PinA Timer
		GPIO_InitStructure.GPIO_Pin = pinB;																								//传入Pin
		GPIO_InitStructure.GPIO_Mode = GPIO_Mode_AIN;																			//选择模拟输入方式
		GPIO_Init(GPIOB,&GPIO_InitStructure);																							//选择具体的GPIO组进行初始化
	}
	
}



void ADC::InitAdcDma(uint8_t* channelArr)
{
	ADC_InitTypeDef  ADC_InitStructure;
	DMA_InitTypeDef  DMA_InitStructure;
//	u8 adcChaTemp = 0;																															//存储计算的ADC通道
	
	
	//首先初始化时钟
	RCC_AHBPeriphClockCmd(RCC_AHBPeriph_DMA1, ENABLE); 																//打开DMA时钟
	RCC_APB2PeriphClockCmd(RCC_APB2Periph_ADC1,ENABLE);																//ADC1的时钟
	RCC_APB2PeriphClockCmd(RCC_APB2Periph_AFIO,ENABLE);//管脚复用
	RCC_ADCCLKConfig(RCC_PCLK2_Div6);									 																//72M6分频=12M   因为ADC最多只能14M采样频率，72M分频12M最合适
	//进行DMA的配置
	DMA_DeInit(DMA1_Channel1);
	DMA_InitStructure.DMA_PeripheralBaseAddr = ADC1_DR_Address;												//ADC外设地址，ADC数据地址+偏移值
	DMA_InitStructure.DMA_MemoryBaseAddr = (u32)mAdcPrimordialValue;	                  //内存地址，成员变量
	DMA_InitStructure.DMA_DIR = DMA_DIR_PeripheralSRC;																//以外设为DMA源
	DMA_InitStructure.DMA_BufferSize = mAdcTotal;                                     //数据转移量为mAdcTotal（1路AD）
	DMA_InitStructure.DMA_PeripheralInc = DMA_PeripheralInc_Disable;	            		//外设地址不递增
	DMA_InitStructure.DMA_MemoryInc = DMA_MemoryInc_Enable;  			    								//内存地址不递增
	DMA_InitStructure.DMA_PeripheralDataSize = DMA_PeripheralDataSize_HalfWord;	    	//接下来的跟采集一路AD一样
	DMA_InitStructure.DMA_MemoryDataSize = DMA_MemoryDataSize_HalfWord;
	DMA_InitStructure.DMA_Mode = DMA_Mode_Circular;										
	DMA_InitStructure.DMA_Priority = DMA_Priority_High;
	DMA_InitStructure.DMA_M2M = DMA_M2M_Disable;
	DMA_Init(DMA1_Channel1, &DMA_InitStructure);
	/* 使能DMA1通道1 */
	DMA_Cmd(DMA1_Channel1, ENABLE);																										//因为Stm32f103c8只有DMA通道1才有ADC的通道，所以直接初始化DMA1
	
	


		
	ADC_InitStructure.ADC_Mode = ADC_Mode_Independent;																//独立模式
	ADC_InitStructure.ADC_ScanConvMode = ENABLE;																			//扫描模式
	ADC_InitStructure.ADC_ContinuousConvMode = ENABLE; 																//工作方式, 单次转换
	ADC_InitStructure.ADC_ExternalTrigConv = ADC_ExternalTrigConv_None;								//软件触发
	ADC_InitStructure.ADC_DataAlign = ADC_DataAlign_Right;														//右对齐
	ADC_InitStructure.ADC_NbrOfChannel = mAdcTotal;																		//通道数目
	
	ADC_Init(ADC1,&ADC_InitStructure);
	
	
	/*规则组(如果要改引脚，需要改这个函数里面的通道),初始化思路AD通道*/
	while(mAdcTotal-- != 0)
	{
		ADC_RegularChannelConfig(ADC1,*(mAdcChannel+mAdcTotal),mAdcTotal+1,ADC_SampleTime_239Cycles5);								//ADC编号1，通道2(IN2)，规则组的采样顺序为1，指定ADC通道的采样时间值
	}


	
	/*使能ADC1的DMA*/
	ADC_DMACmd(ADC1,ENABLE);
	/*使能ADC*/
	ADC_Cmd(ADC1,ENABLE);
	ADC_ResetCalibration(ADC1);																												//复位ADC1
	while(ADC_GetResetCalibrationStatus(ADC1));																				//复位是否分完成
	ADC_StartCalibration(ADC1);																												//校准 
	while(ADC_GetCalibrationStatus(ADC1));																						//校准是否完成
	ADC_SoftwareStartConvCmd(ADC1,ENABLE);
	

}



