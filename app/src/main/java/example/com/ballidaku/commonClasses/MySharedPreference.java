package example.com.ballidaku.commonClasses;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

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


/*    public void saveUser(Context context, HashMap<String, Object> map)
    {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putString(MyConstant.USER_ID, (String) map.get(MyConstant.USER_ID));
        editor.putString(MyConstant.USER_NAME, (String) map.get(MyConstant.USER_NAME));
        editor.putString(MyConstant.USER_EMAIL, (String) map.get(MyConstant.USER_EMAIL));
        editor.putString(MyConstant.USER_PHONE, (String) map.get(MyConstant.USER_PHONE));
        editor.apply();
    }

    public String getUserID(Context context)
    {
        return getPreference(context).getString(MyConstant.USER_ID, "");
    }

    public void clearUserID(Context context)
    {
        getPreference(context).edit().putString(MyConstant.USER_ID, "").apply();
    }

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
