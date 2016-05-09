package com.neucrack.tool;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Date_TimeStamp {
	/**
     * ʱ??ת?????ڸ???????
     * @param seconds ??ȷ?????ַ???
     * @param formatStr
     * @return
     */
	public static String timeStamp2Date(String seconds,String format) {
		if(seconds == null || seconds.isEmpty() || seconds.equals("null")){
			return "";
		}
		if(format == null || format.isEmpty()) format = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date(Long.valueOf(seconds+"000")));
	}
	/**
	 * ??????????ת???????
	 * @param date ???????
	 * @param format ???yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String date2TimeStamp(String date_str,String format){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return String.valueOf(sdf.parse(date_str).getTime()/1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * ȡ?õ?ǰʱ??????ȷ???????
	 * @return
	 */
	public static long timeStamp(){
		return System.currentTimeMillis()/1000;
	}
	
	/**
	 * ȡ?õ?ǰʱ??????ȷ???????
	 * @return
	 */
	public static long timeStampMs(){
		return System.currentTimeMillis();
	}
	
	
	//  ???????
	//	timeStamp=1417792627
	//	date=2014-12-05 23:17:07
	//	1417792627
	public static void main(String[] args) {
		String timeStamp = timeStamp()/1000+"";
		System.out.println("timeStamp="+timeStamp);
		
		String date = timeStamp2Date(timeStamp, "yyyy-MM-dd HH:mm:ss");
		System.out.println("date="+date);
		
		String timeStamp2 = date2TimeStamp(date, "yyyy-MM-dd HH:mm:ss");
		System.out.println(timeStamp2);
	}
}