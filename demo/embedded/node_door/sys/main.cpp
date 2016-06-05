

#include "hardware.h"
#include "app.h"
#include "Interrupt.h"
App app;


// ----------------------------------------------------------------------------

int
main(int argc, char* argv[])
{
	TaskManager::DelayS(3);
	app.Init();
	// Infinite loop
	while (1)
	{
		//循环 
		app.loop();
	}
}


// ----------------------------------------------------------------------------
