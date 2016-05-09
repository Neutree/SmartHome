package com.neucrack.DAO;

import com.neucrack.tool.Encrypt;

public class DAOConnectionInfo {
	public static String mUrl = "https://api.leancloud.cn/1.1/";
	public static String X_LC_Id="Y79QEXdCKF4RTDqKqaFIdLH2-gzGzoHsz";
	public static String X_LC_Key="nkc8xpxnhjR9KGCXJF5BaRkB";
	public static String Content_Type="application/json";
	
	public static String adminName="15023490062";
	public static String adminPassword = Encrypt.md5("1208077207");
	public static String session = "";
}
