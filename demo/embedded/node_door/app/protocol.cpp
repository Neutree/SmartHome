# include "protocol.h"


Protocol::Protocol()
{

}

short Protocol::Switch::dataType = 0x0001;
short Protocol::Sensor::dataType = 0x0002;
short Protocol::Door::dataType = 0x0003;
short Protocol::SignIn::dataType = 0x0011;
short Protocol::KeepAlive::dataType = 0x0010;

