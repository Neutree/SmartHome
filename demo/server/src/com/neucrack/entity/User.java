package com.neucrack.entity;

public class User {
	String mName;          //ÓÃ»§Ãû
	String mPasswd;        //ÃÜÂë
	
	String mNickName;      //êÇ³Æ
	String mHeadPicture;   //Í·Ïñ
	
	String session;        //µÇÂ¼session

	public User(){
		
	}
	public User(String mName, String mPasswd) {
		this.mName = mName;
		this.mPasswd = mPasswd;
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public String getmPasswd() {
		return mPasswd;
	}

	public void setmPasswd(String mPasswd) {
		this.mPasswd = mPasswd;
	}

	public String getmHeadPicture() {
		return mHeadPicture;
	}

	public void setmHeadPicture(String mHeadPicture) {
		this.mHeadPicture = mHeadPicture;
	}

	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}
	public String getmNickName() {
		return mNickName;
	}
	public void setmNickName(String mNickName) {
		this.mNickName = mNickName;
	}
	
}
