/**
  *main.cpp
  *
  *@author neucrack
  *@date 2016-04-21
  *
  */

#include "hardware.h"
#include "app.h"
#include "Interrupt.h"
App app;


// ----------------------------------------------------------------------------

int
main(int argc, char* argv[])
{
	app.Init();
	// Infinite loop
	while (1)
	{
		//循环 
		app.loop();
	}
}


// ----------------------------------------------------------------------------
