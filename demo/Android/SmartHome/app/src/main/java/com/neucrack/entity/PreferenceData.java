package com.neucrack.entity;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.prefs.Preferences;

/**
 * Created by neucrack on 2016-05-09.
 */
public class PreferenceData {
    public static boolean mIsSignedIn = false;
    public static boolean SaveUserInfo(Context context,User user){
        SaveData(context,"PreferenceData","userName",user.getmName(),Context.MODE_PRIVATE);
        SaveData(context,"PreferenceData","userPassword",user.getmPasswd(),Context.MODE_PRIVATE);
        SaveData(context,"PreferenceData","userNickName",user.getmmNickName(),Context.MODE_PRIVATE);
        SaveData(context,"PreferenceData","userHeadImage",user.getmHeadPicture(),Context.MODE_PRIVATE);
        SaveData(context,"PreferenceData","userSession",user.getmSession(),Context.MODE_PRIVATE);
        return true;
    }
    public static User GetUserInfo(Context context){

        String name =  GetData(context,"PreferenceData","userName","",Context.MODE_PRIVATE);
        String passwd =  GetData(context,"PreferenceData","userPassword","",Context.MODE_PRIVATE);
        String nickName = GetData(context,"PreferenceData","userNickName","",Context.MODE_PRIVATE);
        String headPicAddr = GetData(context,"PreferenceData","userHeadImage","",Context.MODE_PRIVATE);
        String session =  GetData(context,"PreferenceData","userSession","",Context.MODE_PRIVATE);
        if(name.equals("")||passwd.equals(""))
            return null;
        User user = new User(name,passwd,nickName,headPicAddr,session);
        return user;
    }

    public static boolean SaveIsRememberUser(Context context,boolean isRememberUser){
        SaveData(context,"PreferenceData","isRememberUser",isRememberUser,Context.MODE_PRIVATE);
        return true;
    }

    public static boolean GetIsRememberUser(Context context){
        return GetDataBoolean(context,"PreferenceData","isRememberUser",false,Context.MODE_PRIVATE);
    }


    public static String GetData(Context context,String preferenceName,String key,String defaultValue,int mode){
        /*获取SharedPreferences实例。如果不存在将新建一个  */
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceName,mode);
        /*读取SharedPreferences中保存的键值:如果文件或键值不在，则用缺省值 */
        return sharedPreferences.getString(key,defaultValue);
    }
    public static boolean GetDataBoolean(Context context,String preferenceName,String key,boolean defaultValue,int mode){
        /*获取SharedPreferences实例。如果不存在将新建一个  */
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceName,mode);
        /*读取SharedPreferences中保存的键值:如果文件或键值不在，则用缺省值 */
        return sharedPreferences.getBoolean(key,defaultValue);
    }

    public static void SaveData(Context context, String preferenceName, String key, Object value, int mode){
        /*获取SharedPreferences实例  */
        SharedPreferences sharedPreferences =  context.getSharedPreferences(preferenceName,mode);
                 /*通过SharedPreferences.Editor类向SharedPreferences中写键值，调用commit()保存修改内容*/
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(value.getClass().toString().equals("class java.lang.Boolean"))
            editor.putBoolean(key, (Boolean) value);
        else if(value.getClass().equals(String.class))
            editor.putString(key, (String) value);
        else
            editor.putString(key, (String) value);
        editor.apply();
    }
}
