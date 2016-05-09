package com.neucrack.tool;

public class CRC {

	static public int CRC16Calculate(byte data[], int size)
	{
	    short out = 0;
	    int bits_read = 0, bit_flag;

	    /* Sanity check: */
	    if(data == null)
	        return 0;

	    int count=0;
	    while(size > 0)
	    {
	        bit_flag = out >> 15;

	        /* Get next bit: */
	        out <<= 1;
	        out |= (data[count] >> bits_read) & 1; // item a) work from the least significant bits

	        /* Increment bit counter: */
	        bits_read++;
	        if(bits_read > 7)
	        {
	            bits_read = 0;
	            ++count;
	            size--;
	        }

	        /* Cycle check: */
	        if(bit_flag!=0)
	            out ^= 0x8005;

	    }

	    // item b) "push out" the last 16 bits
	    int i=0;
	    for (i = 0; i < 16; ++i) {
	        bit_flag = out >> 15;
	        out <<= 1;
	        if(bit_flag!=0)
	            out ^= 0x8005;
	    }

	    // item c) reverse the bits
	    int crc = 0;
	    i = 0x8000;
	    int j = 0x0001;
	    for (; i != 0; i >>=1, j <<= 1) {
	        if (0!=(i & out)) crc |= j;
	    }

	    return crc&0xffff;
	}

}
