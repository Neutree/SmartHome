package com.neucrack.test;

import static org.junit.Assert.*;

import com.neucrack.tool.CRC;

public class Test {

	@org.junit.Test
	public void test() {
		String data = "1208077207";
		System.out.println( Integer.toHexString((CRC.CRC16Calculate(data.getBytes(), 10) )) );
	}

}
