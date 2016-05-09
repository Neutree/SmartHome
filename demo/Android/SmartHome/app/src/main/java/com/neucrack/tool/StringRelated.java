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
	 public static String MD5_32_BytesToString(byte[] md5){
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
	 
	 public static byte[] MD5_32_StringToBytes(String md5){
		 byte[] r=new byte[16];
		 String temp;
		 for(int i=0;i<16;++i){
			 temp = md5.substring(i*2,i*2+2);
			 r[i]= (byte) (Integer.valueOf(temp,16)&0xff); 
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
	 public static byte[] SessionTokenToBytes32(String session) {
		byte[] r = new byte[32];
		if(session==null)
			return r;
		byte[] sessionbytes = session.getBytes();
		System.arraycopy(sessionbytes, 0, r, 0, sessionbytes.length);
		for(int i=0;i<32-sessionbytes.length;++i){
			r[sessionbytes.length+i]='\0';
		}
		return r;
	}
	 public static String SessionTokenBytes32ToString(byte[] session){
		 String rString = "";
		 for(int i=0;i<32;++i){
			 if(session[i]==0)
				 break;
			 rString += (char)session[i];
		 }
		 return rString;
	 }
}
