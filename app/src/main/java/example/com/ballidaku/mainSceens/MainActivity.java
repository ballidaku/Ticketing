package example.com.ballidaku.mainSceens;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.gson.JsonObject;

import java.io.IOException;

import example.com.ballidaku.R;
import example.com.ballidaku.commonClasses.CommonDialogs;
import example.com.ballidaku.commonClasses.CommonInterfaces;
import example.com.ballidaku.commonClasses.CommonMethods;
import example.com.ballidaku.commonClasses.CommonSwitchFragmentsMethods;
import example.com.ballidaku.commonClasses.Constants;
import example.com.ballidaku.commonClasses.MyConstants;
import example.com.ballidaku.commonClasses.MySharedPreference;
import example.com.ballidaku.commonClasses.TicketModel;
import example.com.ballidaku.databinding.ActivityMainBinding;
import example.com.ballidaku.mainSceens.fragments.FirstFragment;
import example.com.ballidaku.mainSceens.fragments.FourthFragment;
import example.com.ballidaku.mainSceens.fragments.HistoryFragment;
import example.com.ballidaku.mainSceens.fragments.MainFragment;
import example.com.ballidaku.mainSceens.fragments.SecondFragment;
import example.com.ballidaku.mainSceens.fragments.ThirdFragment;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;

public class MainActivity extends AppCompatActivity
{
    String TAG = MainActivity.class.getSimpleName();

    public ActivityMainBinding activityMainBinding;
    Context context;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        activityMainBinding.setViewModel(this);

        context = this;
        view = activityMainBinding.getRoot();

        setUpViews();

        changeFragment(0, "", false);
    }

    public void changeFragment(int v, String rangeZoneID, boolean haveToAddBackStack)
    {
        Fragment fragment = null;
        String fragmentTag = "";
        switch (v)
        {
            case 0:
                fragment = new MainFragment<>();
                fragmentTag = Constants.FragmentTags.MainFragment;
                break;

            case 1:
                fragment = new FirstFragment();
                fragmentTag = Constants.FragmentTags.FirstFragment;
                break;

            case 2:
                fragment = new SecondFragment();
                fragmentTag = Constants.FragmentTags.SecondFragment;
                break;

            case 3:
                fragment = new ThirdFragment();
                fragmentTag = Constants.FragmentTags.ThirdFragment;
                break;

            case 4:
                fragment = new FourthFragment();
                fragmentTag = Constants.FragmentTags.FourthFragment;
                break;

            case 5:
                fragment = new HistoryFragment();
                fragmentTag = Constants.FragmentTags.HistoryFragment;
                break;

        }

        Bundle bundle = new Bundle();
        bundle.putString(MyConstants.RANGE_ZONE_ID, rangeZoneID);
        fragment.setArguments(bundle);

//        CommonMethods.getInstance().switchfragment(context, fragment, haveToAddBackStack);

        CommonSwitchFragmentsMethods.INSTANCE.switchFragmentWithTag(
                context,
                R.id.container_body,
                fragment,
                haveToAddBackStack,
                fragmentTag);

    }

    void setUpViews()
    {
        setSupportActionBar(activityMainBinding.toolbar);

        Intent intent = new Intent();
        intent.setPackage("woyou.aidlservice.jiuiv5");
        intent.setAction("woyou.aidlservice.jiuiv5.IWoyouService");
        startService(intent);//启动打印服务
        bindService(intent, connService, Context.BIND_AUTO_CREATE);
    }

    public void updateToolbarTitle(String title)
    {
        activityMainBinding.toolbarTitle.setText(title);
    }

    public IWoyouService getWoyouService()
    {
        return woyouService;
    }

    public ICallback getCallback()
    {
        return callback;
    }

    private IWoyouService woyouService;

    private ServiceConnection connService = new ServiceConnection()
    {

        @Override
        public void onServiceDisconnected(ComponentName name)
        {
            woyouService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            woyouService = IWoyouService.Stub.asInterface(service);
        }
    };

    ICallback callback = new ICallback.Stub()
    {

        @Override
        public void onRunResult(boolean success)
        {
        }

        @Override
        public void onReturnString(final String value)
        {
        }

        @Override
        public void onRaiseException(int code, final String msg)
        {
        }

        @Override
        public void onPrintResult(int code, String msg)
        {
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_signout:

                MySharedPreference.getInstance().clearAllData(context);

                break;

            case R.id.action_change_password:

                CommonDialogs.getInstance().showChangePasswordDialog(context, new CommonInterfaces()
                {
                    @Override
                    public void onChange(String oldPassword, String newPassword)
                    {
                        if (CommonMethods.getInstance().isInternetAvailable(context))
                        {
                            changePasswordApiHit(oldPassword, newPassword);
                        }
                        else
                        {
                            CommonMethods.getInstance().showSnackbar(view, context, context.getString(R.string.internet_not_available));
                        }
                    }
                });

                break;

            case R.id.acton_send_report:

                if (CommonMethods.getInstance().isInternetAvailable(context))
                {
                    sendReportApiHit();
                }
                else
                {
                    CommonMethods.getInstance().showSnackbar(view, context, context.getString(R.string.internet_not_available));
                }

                break;

            case R.id.action_show_history:
                changeFragment(5, "", true);
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /*API HITTING AREA*/
    public void saveTicketApi(TicketModel ticketModel)
    {
        String ticketModelString = CommonMethods.getInstance().convertBeanToString(ticketModel);
        CommonDialogs.getInstance().showProgressDialog(context);
        CommonMethods.getInstance().postDataWithAuth(context, MyConstants.SAVE_TICKET, ticketModelString, new Callback()
        {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e)
            {
                Log.e(TAG, "onFailure  " + e.toString());
                CommonDialogs.getInstance().dismissDialog();

                //java.net.ConnectException: Failed to connect to ticketing.hpwildlife.gov.in/103.20.214.11:80
//                if (e instanceof ConnectException || e instanceof SocketTimeoutException)
//                {
//                   runOnUiThread(() -> CommonDialogs.getInstance().showMessageDialog(context, context.getString(R.string.internet_not_available)));
                runOnUiThread(() -> CommonDialogs.getInstance().showMessageDialog(context, e.toString()));

//                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException
            {
                CommonDialogs.getInstance().dismissDialog();
                if (response.isSuccessful())
                {
                    String responseStr = response.body().string();
                    Log.e(TAG, responseStr);

                    JsonObject jsonObjectMain = CommonMethods.getInstance().convertStringToJsonObject(responseStr);

                    if (jsonObjectMain.has(MyConstants.STATUS) && jsonObjectMain.get(MyConstants.STATUS).getAsInt() == 200)
                    {
                        JsonObject jsonObject = jsonObjectMain.getAsJsonObject(MyConstants.TICKET_DETAIL_BASIC_MODEL);
                        String ticketId = jsonObject.get(MyConstants.TICKET_ID).getAsString();
                        ticketModel.setTicketId(ticketId);

                        Fragment fragment = CommonSwitchFragmentsMethods.INSTANCE.getVisibleFragment(context, R.id.container_body);

                        if (fragment instanceof FirstFragment)
                        {
                            ((FirstFragment) fragment).printTicket(ticketModel);
                        }
                        else if (fragment instanceof SecondFragment)
                        {
                            ((SecondFragment) fragment).printTicket(ticketModel);
                        }
                        else if (fragment instanceof ThirdFragment)
                        {
                            ((ThirdFragment) fragment).printTicket(ticketModel);
                        }
                        else if (fragment instanceof FourthFragment)
                        {
                            ((FourthFragment) fragment).printTicket(ticketModel);
                        }
                    }
                    else if (jsonObjectMain.has(MyConstants.STATUS) && jsonObjectMain.get(MyConstants.STATUS).getAsInt() == 400)
                    {
                        MySharedPreference.getInstance().clearAllData(context);
                    }
                    else
                    {
                        runOnUiThread(() -> CommonDialogs.getInstance().showMessageDialog(context, "In Success : " + responseStr));
                    }

                }
                else
                {
//                    String errorStr = response.body().string();

                    runOnUiThread(() -> CommonDialogs.getInstance().showMessageDialog(context, "In ELSE : " + response.body().toString()));
//                    JsonObject jsonObject = CommonMethods.getInstance().convertStringToJson(errorStr);
//                    CommonMethods.getInstance().showSnackbar(view, context, jsonObject.get(MyConstants.ERROR_DESCRIPTION).getAsString());
                }
            }
        });
    }

    public void changePasswordApiHit(String oldPassword, String newPassword)
    {
        CommonDialogs.getInstance().showProgressDialog(context);

        String userId = MySharedPreference.getInstance().getUserData(context, MyConstants.USERID);
        String json = "userId=" + userId + "&oldPassword=" + oldPassword + "&newPassword=" + newPassword;

        CommonMethods.getInstance().postDataWithAuth(context, MyConstants.CHANGE_PASSWORD, json, new Callback()
        {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e)
            {
                Log.e(TAG, "onFailure  " + e.toString());
                CommonDialogs.getInstance().dismissDialog();
//                if(e.toString().contains("Failed to connect to"))
//                {
                CommonMethods.getInstance().showSnackbar(view, context, context.getString(R.string.internet_not_available));
//                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException
            {
                CommonDialogs.getInstance().dismissDialog();

                if (response.isSuccessful())
                {
                    String responseStr = response.body().string();
                    Log.e(TAG, responseStr);

//                    JsonObject jsonObject = CommonMethods.getInstance().convertStringToJson(responseStr);

                    JsonObject jsonObjectMain = CommonMethods.getInstance().convertStringToJsonObject(responseStr);

                    if (jsonObjectMain.has(MyConstants.STATUS) && jsonObjectMain.get(MyConstants.STATUS).getAsInt() == 200)
                    {
                        CommonMethods.getInstance().showSnackbar(view, context, jsonObjectMain.get(MyConstants.MESSAGE).getAsString());
                    }
                    else if (jsonObjectMain.has(MyConstants.STATUS) && jsonObjectMain.get(MyConstants.STATUS).getAsInt() == 400)
                    {
                        MySharedPreference.getInstance().clearAllData(context);
                    }
                }
                else
                {
                    String errorStr = response.body().string();
                    JsonObject jsonObject = CommonMethods.getInstance().convertStringToJson(errorStr);
                    CommonMethods.getInstance().showSnackbar(view, context, jsonObject.get(MyConstants.ERROR_DESCRIPTION).getAsString());
                }
            }
        });
    }

    public void sendReportApiHit()
    {
        CommonDialogs.getInstance().showProgressDialog(context);

        String email = MySharedPreference.getInstance().getUserData(context, MyConstants.EMAIL);
        String json = "email=" + email;

        CommonMethods.getInstance().postDataWithAuth(context, MyConstants.SEND_REPORT, json, new Callback()
        {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e)
            {
                Log.e(TAG, "onFailure  " + e.toString());
                CommonDialogs.getInstance().dismissDialog();
//                if(e.toString().contains("Failed to connect to"))
//                {
                CommonMethods.getInstance().showSnackbar(view, context, context.getString(R.string.internet_not_available));
//                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException
            {
                CommonDialogs.getInstance().dismissDialog();

                if (response.isSuccessful())
                {
                    String responseStr = response.body().string();
                    Log.e(TAG, responseStr);

                    JsonObject jsonObjectMain = CommonMethods.getInstance().convertStringToJson(responseStr);

                    if (jsonObjectMain.has(MyConstants.STATUS) && jsonObjectMain.get(MyConstants.STATUS).getAsInt() == 200)
                    {
                        CommonMethods.getInstance().showSnackbar(view, context, jsonObjectMain.get(MyConstants.MESSAGE).getAsString());
                    }
                    else if (jsonObjectMain.has(MyConstants.STATUS) && jsonObjectMain.get(MyConstants.STATUS).getAsInt() == 400)
                    {
                        MySharedPreference.getInstance().clearAllData(context);
                    }

                }
                else
                {
                    String errorStr = response.body().string();
                    JsonObject jsonObject = CommonMethods.getInstance().convertStringToJson(errorStr);
                    CommonMethods.getInstance().showSnackbar(view, context, jsonObject.get(MyConstants.ERROR_DESCRIPTION).getAsString());
                }
            }
        });
    }
}
