package com.neucrack.tool;

import java.io.UnsupportedEncodingException;

public class StringRelated {

	 public static String Byte6ToMac(byte[] c) {
	        return Integer.toHexString(((int)c[0]&0xff))+":"+Integer.toHexString(((int)c[1]&0xff))+":"+Integer.toHexString(((int)c[2]&0xff))+":"
	        		+((short)c[3]&0xff)+":"+Integer.toHexString(((int)c[4]&0xff))+":"+Integer.toHexString(((int)c[5]&0xff)); 
	 }
	 public static String Byte6ToMac(byte[] c,int startIndex) {
	        return Integer.toHexString(((int)c[0+startIndex]&0xff))+":"+Integer.toHexString(((int)c[1+startIndex]&0xff))+":"+Integer.toHexString(((int)c[2+startIndex]&0xff))+":"
	        		+((short)c[3+startIndex]&0xff)+":"+Integer.toHexString(((int)c[4+startIndex]&0xff))+":"+Integer.toHexString(((int)c[5+startIndex]&0xff)); 
	 }
	 
	 
	 public static byte[] MacToBytes(String mac) {
		byte []macBytes = new byte[6];
		String [] strArr = mac.split(":"); 
		for(int i = 0;i < strArr.length; i++){
			int value = Integer.parseInt(strArr[i],16);
			macBytes[i] = (byte) value;
		}
		return macBytes;
	 }
	 
	 public static String BytesToString(byte[] b,int size){
		 String str= "";
		 for(int i=0;i<size;++i){
			 str+=b[i]-'0';
		 }
		 return str;
	 }
	 public static String MD5_32BytesToString(byte[] md5){
		 String r="";
		 String temp=null;
		 for(int i=0;i<16;++i){
			 temp = Integer.toHexString(((int)md5[i]&0xff));
			 if(temp.length()<2)
				 temp = "0"+temp;
			 r += temp;
		 }
		 return r;
	 }
	 
	 public static String newString_UTF_8(String str) {
		 try {
			return new String(str.getBytes("utf-8"),"gbk");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
		
	}
}
