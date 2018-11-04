package example.com.ballidaku.commonClasses;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.gson.JsonObject;

import example.com.ballidaku.frontScreens.LoginActivity;
import example.com.ballidaku.mainSceens.MainActivity;

/**
 * Created by sharanpalsingh on 26/03/17.
 */
public class MySharedPreference
{

    private MySharedPreference()
    {
    }

    public static MySharedPreference instance = new MySharedPreference();

    public static MySharedPreference getInstance()
    {
        return instance;
    }

    private SharedPreferences getPreference(Context context)
    {
        String preferenceName = "TicketingPreference";
        return context.getSharedPreferences(preferenceName, Activity.MODE_PRIVATE);
    }


    public void saveUser(Context context, String json)
    {

        JsonObject jsonObject = CommonMethods.getInstance().convertStringToJson(json);


        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putString(MyConstants.ACCESS_TOKEN, jsonObject.get(MyConstants.ACCESS_TOKEN).getAsString());
        editor.putString(MyConstants.TOKEN_TYPE, jsonObject.get(MyConstants.TOKEN_TYPE).getAsString());
        editor.putString(MyConstants.EXPIRES_IN, jsonObject.get(MyConstants.EXPIRES_IN).getAsString());
        editor.putString(MyConstants.USERLOGINOBJECT, jsonObject.get(MyConstants.USERLOGINOBJECT).getAsString());

        JsonObject childJsonObject = CommonMethods.getInstance().convertStringToJson(jsonObject.get(MyConstants.USERLOGINOBJECT).getAsString());

        editor.putString(MyConstants.STATUS, childJsonObject.get(MyConstants.STATUS).getAsString());
        editor.putString(MyConstants.MESSAGE, childJsonObject.get(MyConstants.MESSAGE).getAsString());
        editor.putString(MyConstants.USERID, childJsonObject.get(MyConstants.USERID).getAsString());
        editor.putString(MyConstants.FIRSTNAME, childJsonObject.get(MyConstants.FIRSTNAME).getAsString());
        editor.putString(MyConstants.PROFILEIMAGENAME, childJsonObject.get(MyConstants.PROFILEIMAGENAME).getAsString());
        editor.apply();
    }


    public String getUserData(Context context,String key)
    {
        return getPreference(context).getString(key, "");
    }

    public void saveData(Context context,String key,String value)
    {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putString(key,value);
        editor.apply();
    }

    public void clearAllData(Context context)
    {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.clear();
        editor.apply();

        context.startActivity(new Intent(context,LoginActivity.class));

        ((MainActivity)context).finish();
    }

/*





    public String getUserName(Context context)
    {
        return getPreference(context).getString(MyConstant.USER_NAME, "");
    }

    public String getUserEmail(Context context)
    {
        return getPreference(context).getString(MyConstant.USER_EMAIL, "");
    }

    public String getUserPhone(Context context)
    {
        return getPreference(context).getString(MyConstant.USER_PHONE, "");
    }

    public String getMPIN(Context context)
    {
        return getPreference(context).getString(MyConstant.MPIN, "");
    }

    public void clearMPIN(Context context)
    {
        getPreference(context).edit().putString(MyConstant.MPIN, "").apply();
    }*/


}
