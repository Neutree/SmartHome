package com.neucrack.entity;

/**
 * Created by neucrack on 2016-05-09.
 */
public class User {
    String mName;          //用户名
    String mPasswd;        //密码

    String mNickName;      //昵称
    String mHeadPicture;   //头像

    String mSession;        //登录session

    public User(){

    }
    public User(User user){
        mName = user.getmName();
        mPasswd = user.getmPasswd();
        mNickName = user.getmmNickName();
        mHeadPicture = user.getmHeadPicture();
        mSession = user.getmSession();
    }
    public User(String mName, String mPasswd,String nickName,String HeadImageAddress) {
        this.mName = mName;
        this.mPasswd = mPasswd;
        mNickName = nickName;
        mHeadPicture = HeadImageAddress;
    }
    public User(String mName, String mPasswd,String nickName,String HeadImageAddress,String session) {
        this.mName = mName;
        this.mPasswd = mPasswd;
        mNickName = nickName;
        mHeadPicture = HeadImageAddress;
        mSession = session;
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

    public String getmSession() {
        return mSession;
    }

    public void setmSession(String session) {
        this.mSession = session;
    }
    public String getmmNickName() {
        return mNickName;
    }
    public void setmNickName(String mNickName) {
        this.mNickName = mNickName;
    }
}
