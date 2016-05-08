package com.neucrack.entity;

public class DeviceSensor {
	static public final short DEVICE_TYPE = 0X0002;
	
	byte dataSize;
	byte[] data;
	
	public byte getDataSize() {
		return dataSize;
	}
	public void setDataSize(byte dataSize) {
		this.dataSize = dataSize;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	
	
}
