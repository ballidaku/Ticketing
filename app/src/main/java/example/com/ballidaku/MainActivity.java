package example.com.ballidaku;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.widget.NumberPicker;

import example.com.ballidaku.databinding.ActivityMainBinding;
import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;

public class MainActivity extends AppCompatActivity
{
    String TAG= MainActivity.class.getSimpleName();

    ActivityMainBinding activityMainBinding;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        activityMainBinding.setViewModel(this);

        context = this;

        setUpViews();
    }

    void setUpViews()
    {
        activityMainBinding.toolbarTitle.setText(getString(R.string.van_vihar_dhungri));

        activityMainBinding.numberPickerChildren.setMinValue(0);
        activityMainBinding.numberPickerChildren.setMaxValue(100);

        activityMainBinding.numberPickerAdult.setMinValue(0);
        activityMainBinding.numberPickerAdult.setMaxValue(100);

        Intent intent = new Intent();
        intent.setPackage("woyou.aidlservice.jiuiv5");
        intent.setAction("woyou.aidlservice.jiuiv5.IWoyouService");
        startService(intent);//启动打印服务
        bindService(intent, connService, Context.BIND_AUTO_CREATE);
    }

    public void onChildrenCountChange(NumberPicker numberPicker, int oldValue, int newValue)
    {
        int value = numberPicker.getValue() * 5;
        activityMainBinding.textViewChildrenTotal.setText(String.valueOf(value));
        updateCountTotal();
    }

    public void onAdultCountChange(NumberPicker numberPicker, int oldValue, int newValue)
    {
        int value = numberPicker.getValue() * 10;
        activityMainBinding.textViewAdultTotal.setText(String.valueOf(value));
        updateCountTotal();
    }


    void updateCountTotal()
    {

        int totalCount = activityMainBinding.numberPickerAdult.getValue() + activityMainBinding.numberPickerChildren.getValue();
        int totalAmount = (activityMainBinding.numberPickerAdult.getValue() * 10) + (activityMainBinding.numberPickerChildren.getValue() * 5);
        activityMainBinding.textViewCountTotal.setText(String.valueOf(totalCount));
        activityMainBinding.textViewTotalRs.setText(String.valueOf(totalAmount));
    }

    public void onPrintClicked()
    {
        int adultCount = activityMainBinding.numberPickerAdult.getValue();
        int adultTotal = activityMainBinding.numberPickerAdult.getValue() * 10;

        int childrenCount = activityMainBinding.numberPickerChildren.getValue();
        int childrenTotal = activityMainBinding.numberPickerChildren.getValue() * 5;

        int countTotal = adultCount + childrenCount;
        int totalAmount = adultTotal + childrenTotal;

        String childrenCountString = CommonMethods.getInstance().fixedLengthString(childrenCount, 3);
        String adultCountString =  CommonMethods.getInstance().fixedLengthString(adultCount, 3);
        String countTotalString =  CommonMethods.getInstance().fixedLengthString(countTotal, 3);

        String childrenTotalString =  CommonMethods.getInstance().fixedLengthString(childrenTotal, 4);
        String adultTotalString =  CommonMethods.getInstance().fixedLengthString(adultTotal, 4);
        String totalAmountString =  CommonMethods.getInstance().fixedLengthString(totalAmount, 4);

        printTicket(childrenCountString, childrenTotalString, adultCountString, adultTotalString,countTotalString,totalAmountString);
    }

    void printTicket(String childrenCountString, String childrenTotalString, String adultCountString, String adultTotalString, String countTotalString, String totalAmountString)
    {
        try
        {
            String date=CommonMethods.getInstance().getDate();
            String time=CommonMethods.getInstance().getTime();

            woyouService.printTextWithFont("*************************\n", "", 30, callback);
            woyouService.printTextWithFont("       Van Vihar Dhungri       \n", "", 24, callback);
            woyouService.printTextWithFont("          Entry Ticket         \n", "", 24, callback);
            woyouService.printTextWithFont("*************************\n", "", 30, callback);
            woyouService.printTextWithFont("Type           Count      Total\n", "", 24, callback);
            woyouService.printTextWithFont("-------------------------------\n", "", 24, callback);
            woyouService.printTextWithFont("Children       x" + childrenCountString + "      Rs " + childrenTotalString + "\n", "", 24, callback);
            woyouService.printTextWithFont("Adult          x" + adultCountString + "      Rs " + adultTotalString + "\n", "", 24, callback);
            woyouService.printTextWithFont("-------------------------------\n", "", 24, callback);
            woyouService.printTextWithFont("Total          x" + countTotalString + "      Rs " + totalAmountString + "\n", "", 24, callback);
            woyouService.printTextWithFont("-------------------------------\n", "", 24, callback);
            woyouService.printTextWithFont("Date "+date+"   Time "+time+"\n\n\n\n", "", 24, callback);

        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
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
