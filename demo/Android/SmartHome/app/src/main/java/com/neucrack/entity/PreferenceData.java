package com.neucrack.entity;

import java.util.prefs.Preferences;

/**
 * Created by neucrack on 2016-05-09.
 */
public class PreferenceData {
    public static boolean mIsSignedIn = false;
    public static boolean SaveUserInfo(User user){
        Preferences prefs;
        prefs = Preferences.userRoot().node("PreferenceData");
        prefs.put("userName",user.getmName());
        prefs.put("userPassword",user.getmPasswd());
        prefs.put("userNickName",user.getmmNickName());
        prefs.put("userHeadImage",user.getmHeadPicture());
        prefs.put("userSession",user.getmSession());
        return true;
    }
    public static User GetUserInfo(){
        Preferences prefs;
        prefs = Preferences.userRoot().node("PreferenceData");
        String name = prefs.get("userName","");
        String passwd = prefs.get("userPassword","");
        String nickName = prefs.get("userNickName","");
        String headPicAddr = prefs.get("userHeadImage","");
        String session = prefs.get("userSession","");
        if(name.equals("")||passwd.equals(""))
            return null;
        User user = new User(name,passwd,nickName,headPicAddr,session);

        return user;
    }

    public static boolean SaveIsRememberUser(boolean isRememberUser){
        Preferences prefs;
        prefs = Preferences.userRoot().node("PreferenceData");
        prefs.putBoolean("isRememberUser",isRememberUser);
        return true;
    }

    public static boolean GetIsRememberUser(){
        Preferences prefs;
        prefs = Preferences.userRoot().node("PreferenceData");
        return prefs.getBoolean("isRememberUser",false);

    }
}
