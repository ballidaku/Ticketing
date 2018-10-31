package example.com.ballidaku.mainSceens;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import example.com.ballidaku.R;
import example.com.ballidaku.commonClasses.CommonMethods;
import example.com.ballidaku.databinding.ActivityMainBinding;
import example.com.ballidaku.mainSceens.fragments.FirstFragment;
import example.com.ballidaku.mainSceens.fragments.FourthFragment;
import example.com.ballidaku.mainSceens.fragments.MainFragment;
import example.com.ballidaku.mainSceens.fragments.SecondFragment;
import example.com.ballidaku.mainSceens.fragments.ThirdFragment;
import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;

public class MainActivity extends AppCompatActivity
{
    String TAG = MainActivity.class.getSimpleName();

    public ActivityMainBinding activityMainBinding;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        activityMainBinding.setViewModel(this);

        context = this;

        setUpViews();

        changeFragment(0);
    }

    public void changeFragment(int v)
    {
        Fragment fragment = null;
        boolean haveToAddBackStack=true;
        switch (v)
        {
            case 0:
                haveToAddBackStack=false;
                fragment = new MainFragment<>();
                break;

            case 1:
                fragment = new FirstFragment();
                break;

            case 2:
                fragment = new SecondFragment();
                break;

            case 3:
                fragment = new ThirdFragment();
                break;

            case 4:
                fragment = new FourthFragment();
                break;

        }

        CommonMethods.getInstance().switchfragment(context, fragment, haveToAddBackStack);

    }

    void setUpViews()
    {


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
        public void onRunResult(boolean success) throws RemoteException
        {
        }

        @Override
        public void onReturnString(final String value) throws RemoteException
        {
        }

        @Override
        public void onRaiseException(int code, final String msg)
                throws RemoteException
        {
        }

        @Override
        public void onPrintResult(int code, String msg) throws RemoteException
        {

        }
    };
}
