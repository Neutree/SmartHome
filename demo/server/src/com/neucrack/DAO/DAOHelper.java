package com.neucrack.DAO;

import com.neucrack.entity.User;

public class DAOHelper {
	
	/**
	 * 
	 * @param user 要查找的用户呢
	 * @return 1:成功  0：没有该用户   <0:发生错误
	 */
	public int QueryUser(User user) {
		return -1;
	}
	
	/**
	 * 验证用户用户名和密码是否正确
	 * @param user 要查找的用户呢
	 * @return 1:成功  0：没有该用户   <0:发生错误
	 */
	public int VerifyUser(User user) {
		return -1;
	}
	
	public int AddUser(User user){
		return -1;
	}
	
	public int DropUser(User user){
		return -1;
	}
	
	public int EditUser(User oldOne,User newOne){
		return -1;
	}
	public String QueryOwnerOfDevice(String deviceName){
		String userName = "";
		return userName;
	}
	
}
