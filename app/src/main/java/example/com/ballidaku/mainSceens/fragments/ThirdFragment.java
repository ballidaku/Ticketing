package example.com.ballidaku.mainSceens.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import example.com.ballidaku.R;
import example.com.ballidaku.commonClasses.CommonMethods;
import example.com.ballidaku.commonClasses.Model;
import example.com.ballidaku.commonClasses.MyConstants;
import example.com.ballidaku.databinding.FragmentThirdBinding;
import example.com.ballidaku.mainSceens.MainActivity;
import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;

public class ThirdFragment extends Fragment
{
    FragmentThirdBinding fragmentThirdBinding;
    View view;
    Context context;

    int allPerCost = 0;
    int packagePerCost = 0;
    int weeklyPerCost = 0;
    int fortnightlyPerCost = 0;
    int monthlyPerCost = 0;
    int parking1PerCost = 0;
    int parking2PerCost = 0;
    int parking3PerCost = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        fragmentThirdBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_third, container, false);
        view = fragmentThirdBinding.getRoot();
        context = getContext();
        fragmentThirdBinding.setHandler(this);


        setUpViews();

        return view;
    }

    void setUpViews()
    {
        ((MainActivity) context).updateToolbarTitle(getString(R.string.nature_park_manali));

        ((MainActivity) context).setSupportActionBar(((MainActivity) context).activityMainBinding.toolbar);
        ((MainActivity) context).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((MainActivity) context).getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        allPerCost = MyConstants.ALL_NATURE_PARK_MANALI;
        packagePerCost = MyConstants.PACKAGE;
        weeklyPerCost = MyConstants.WEEKLY;
        fortnightlyPerCost = MyConstants.FORTNIGHTLY;
        monthlyPerCost = MyConstants.MONTHLY;
        parking1PerCost = MyConstants.WL_PARKING1;
        parking2PerCost = MyConstants.WL_PARKING2;
        parking3PerCost = MyConstants.WL_PARKING3;

        fragmentThirdBinding.setViewModel(getModel());

        fragmentThirdBinding.numberPickerAllCount.setOnValueChangedListener((picker, oldVal, newVal) ->
        {
            int value = picker.getValue() * allPerCost;
            fragmentThirdBinding.textViewAllTotal.setText(String.valueOf(value));
            updateCountTotal();
        });

        fragmentThirdBinding.numberPickerPackage.setOnValueChangedListener((picker, oldVal, newVal) ->
        {
            int value = picker.getValue() * packagePerCost;
            fragmentThirdBinding.textViewPackageTotal.setText(String.valueOf(value));
            updateCountTotal();
        });

        fragmentThirdBinding.numberPickerWeekly.setOnValueChangedListener((picker, oldVal, newVal) ->
        {
            int value = picker.getValue() * weeklyPerCost;
            fragmentThirdBinding.textViewWeeklyTotal.setText(String.valueOf(value));
            updateCountTotal();
        });

        fragmentThirdBinding.numberPickerFornightly.setOnValueChangedListener((picker, oldVal, newVal) ->
        {
            int value = picker.getValue() * fortnightlyPerCost;
            fragmentThirdBinding.textViewFornightlyTotal.setText(String.valueOf(value));
            updateCountTotal();
        });

        fragmentThirdBinding.numberPickerMonthly.setOnValueChangedListener((picker, oldVal, newVal) ->
        {
            int value = picker.getValue() * monthlyPerCost;
            fragmentThirdBinding.textViewMonthlyTotal.setText(String.valueOf(value));
            updateCountTotal();
        });


        fragmentThirdBinding.numberPickerParking1.setOnValueChangedListener((picker, oldVal, newVal) ->
        {
            int value = picker.getValue() * parking1PerCost;
            fragmentThirdBinding.textViewParking1Total.setText(String.valueOf(value));
            updateCountTotal();
        });

        fragmentThirdBinding.numberPickerParking2.setOnValueChangedListener((picker, oldVal, newVal) ->
        {
            int value = picker.getValue() * parking2PerCost;
            fragmentThirdBinding.textViewParking2Total.setText(String.valueOf(value));
            updateCountTotal();
        });

        fragmentThirdBinding.numberPickerParking3.setOnValueChangedListener((picker, oldVal, newVal) ->
        {
            int value = picker.getValue() * parking3PerCost;
            fragmentThirdBinding.textViewParking3Total.setText(String.valueOf(value));
            updateCountTotal();
        });


    }

    Model getModel()
    {
        Model model = new Model();
        model.setAll("All\n@Rs. " + allPerCost + "/-");
        model.setPackageValue("Package per day\n@Rs. " + packagePerCost + "/-");
        model.setWeekly("Weekly\n@Rs. " + weeklyPerCost + "/-");
        model.setFortNightly("Fortnightly\n@Rs. " + fortnightlyPerCost + "/-");
        model.setMonthly("Monthly\n@Rs. " + monthlyPerCost + "/-");
        model.setParking1("Upto 4 Hours\n@Rs. " + parking1PerCost + "/-");
        model.setParking2("Upto 8 Hours\n@Rs. " + parking2PerCost + "/-");
        model.setParking3("Upto 12 Hours\n@Rs. " + parking3PerCost + "/-");
        return model;
    }


    void updateCountTotal()
    {

        int totalCount = fragmentThirdBinding.numberPickerAllCount.getValue()
                + fragmentThirdBinding.numberPickerPackage.getValue()
                + fragmentThirdBinding.numberPickerWeekly.getValue()
                + fragmentThirdBinding.numberPickerFornightly.getValue()
                + fragmentThirdBinding.numberPickerMonthly.getValue()
                + fragmentThirdBinding.numberPickerParking1.getValue()
                + fragmentThirdBinding.numberPickerParking2.getValue()
                + fragmentThirdBinding.numberPickerParking3.getValue();


        int totalAmount = (fragmentThirdBinding.numberPickerAllCount.getValue() * allPerCost)
                + (fragmentThirdBinding.numberPickerPackage.getValue() * packagePerCost)
                + (fragmentThirdBinding.numberPickerWeekly.getValue() * weeklyPerCost)
                + (fragmentThirdBinding.numberPickerFornightly.getValue() * fortnightlyPerCost)
                + (fragmentThirdBinding.numberPickerMonthly.getValue() * monthlyPerCost)
                + (fragmentThirdBinding.numberPickerParking1.getValue() * parking1PerCost)
                + (fragmentThirdBinding.numberPickerParking2.getValue() * parking2PerCost)
                + (fragmentThirdBinding.numberPickerParking3.getValue() * parking3PerCost);

        fragmentThirdBinding.textViewTotalCount.setText(String.valueOf(totalCount));
        fragmentThirdBinding.textViewTotalAmount.setText(String.valueOf(totalAmount));
    }

    public void onPrintClicked()
    {
        int allCount = fragmentThirdBinding.numberPickerAllCount.getValue();
        int allTotal = allCount * allPerCost;

        String allCountString = CommonMethods.getInstance().fixedLengthString(allCount, 3);
        String allTotalString = CommonMethods.getInstance().fixedLengthString(allTotal, 4);

        //******************************************************************************************
        // Other
        //******************************************************************************************
        int packageCount = fragmentThirdBinding.numberPickerPackage.getValue();
        int packageTotal = packageCount * packagePerCost;

        int weeklyCount = fragmentThirdBinding.numberPickerWeekly.getValue();
        int weeklyTotal = weeklyCount * weeklyPerCost;

        int fortnightlyCount = fragmentThirdBinding.numberPickerFornightly.getValue();
        int fornightlyTotal = fortnightlyCount * fortnightlyPerCost;

        int monthlyCount = fragmentThirdBinding.numberPickerMonthly.getValue();
        int monthlyTotal = monthlyCount * monthlyPerCost;

        String packageCountString = CommonMethods.getInstance().fixedLengthString(packageCount, 3);
        String weeklyCountString = CommonMethods.getInstance().fixedLengthString(weeklyCount, 3);
        String fortnightlyCountString = CommonMethods.getInstance().fixedLengthString(fortnightlyCount, 3);
        String monthlyCountString = CommonMethods.getInstance().fixedLengthString(monthlyCount, 3);

        String packageTotalString = CommonMethods.getInstance().fixedLengthString(packageTotal, 4);
        String weeklyTotalString = CommonMethods.getInstance().fixedLengthString(weeklyTotal, 4);
        String fortnightlyTotalString = CommonMethods.getInstance().fixedLengthString(fornightlyTotal, 4);
        String monthlyTotalString = CommonMethods.getInstance().fixedLengthString(monthlyTotal, 4);

        //******************************************************************************************
        // Parking
        //******************************************************************************************


        int parking1Count = fragmentThirdBinding.numberPickerParking1.getValue();
        int parking1Total = parking1Count * parking1PerCost;

        int parking2Count = fragmentThirdBinding.numberPickerParking2.getValue();
        int parking2Total = parking2Count * parking2PerCost;

        int parking3Count = fragmentThirdBinding.numberPickerParking3.getValue();
        int parking3Total = parking3Count * parking3PerCost;


        String parking1CountString = CommonMethods.getInstance().fixedLengthString(parking1Count, 3);
        String parking2CountString = CommonMethods.getInstance().fixedLengthString(parking2Count, 3);
        String parking3CountString = CommonMethods.getInstance().fixedLengthString(parking3Count, 3);


        String parking1TotalString = CommonMethods.getInstance().fixedLengthString(parking1Total, 4);
        String parking2TotalString = CommonMethods.getInstance().fixedLengthString(parking2Total, 4);
        String parking3TotalString = CommonMethods.getInstance().fixedLengthString(parking3Total, 4);


        //******************************************************************************************
        //******************************************************************************************

        int countTotal = allCount + packageCount + weeklyCount + fortnightlyCount + monthlyCount + parking1Count + parking2Count + parking3Count;
        String countTotalString = CommonMethods.getInstance().fixedLengthString(countTotal, 3);

        int total = allTotal + packageTotal + weeklyTotal + fornightlyTotal + monthlyTotal + parking1Total + parking2Total + parking3Total;
        String totalAmountString = CommonMethods.getInstance().fixedLengthString(total, 4);

        printTicket(allCountString, allTotalString,
                packageCountString, packageTotalString,
                weeklyCountString, weeklyTotalString,
                fortnightlyCountString, fortnightlyTotalString,
                monthlyCountString, monthlyTotalString,
                parking1CountString,parking1TotalString,
                parking2CountString,parking2TotalString,
                parking3CountString,parking3TotalString,
                countTotalString, totalAmountString);
    }


    void printTicket(String allCountString, String allTotalString,
                     String packageCountString, String packageTotalString,
                     String weeklyCountString, String weeklyTotalString,
                     String fortnightlyCountString, String fortnightlyTotalString,
                     String monthlyCountString, String monthlyTotalString,
                     String parking1CountString, String parking1TotalString,
                     String parking2CountString, String parking2TotalString,
                     String parking3CountString, String parking3TotalString,
                     String countTotalString, String totalAmountString)
    {
        IWoyouService woyouService = ((MainActivity) context).getWoyouService();
        ICallback callback = ((MainActivity) context).getCallback();

        try
        {
            String date = CommonMethods.getInstance().getDate();
            String time = CommonMethods.getInstance().getTime();

            woyouService.printTextWithFont("*************************\n", "", 30, callback);
            woyouService.printTextWithFont("          Nature Park          \n", "", 24, callback);
            woyouService.printTextWithFont(" Near WL Interpretation Centre \n", "", 24, callback);
            woyouService.printTextWithFont("            Manali             \n", "", 24, callback);
            woyouService.printTextWithFont("*************************\n", "", 30, callback);
            woyouService.printTextWithFont("Type           Count      Total\n", "", 24, callback);
            woyouService.printTextWithFont("-------------------------------\n", "", 24, callback);
            woyouService.printTextWithFont("Entry Ticket         \n", "", 24, callback);
            woyouService.printTextWithFont("-------------------------------\n", "", 24, callback);
            woyouService.printTextWithFont("All            x" + allCountString + "      Rs " + allTotalString + "\n\n", "", 24, callback);

            woyouService.printTextWithFont("Other                 \n", "", 24, callback);
            woyouService.printTextWithFont("-------------------------------\n", "", 24, callback);
            woyouService.printTextWithFont("Package        x" + packageCountString + "      Rs " + packageTotalString + "\n", "", 24, callback);
            woyouService.printTextWithFont("Weekly         x" + weeklyCountString + "      Rs " + weeklyTotalString + "\n", "", 24, callback);
            woyouService.printTextWithFont("Fortnightly    x" + fortnightlyCountString + "      Rs " + fortnightlyTotalString + "\n", "", 24, callback);
            woyouService.printTextWithFont("Monthly        x" + monthlyCountString + "      Rs " + monthlyTotalString + "\n\n", "", 24, callback);

            woyouService.printTextWithFont("Parking                 \n", "", 24, callback);
            woyouService.printTextWithFont("-------------------------------\n", "", 24, callback);
            woyouService.printTextWithFont("Upto 4 Hours   x" + parking1CountString + "      Rs " + parking1TotalString + "\n", "", 24, callback);
            woyouService.printTextWithFont("Upto 8 Hours   x" + parking2CountString + "      Rs " + parking2TotalString + "\n", "", 24, callback);
            woyouService.printTextWithFont("Upto 12 Hours  x" + parking3CountString + "      Rs " + parking3TotalString + "\n", "", 24, callback);

            woyouService.printTextWithFont("-------------------------------\n", "", 24, callback);
            woyouService.printTextWithFont("Total          x" + countTotalString + "      Rs " + totalAmountString + "\n", "", 24, callback);
            woyouService.printTextWithFont("-------------------------------\n", "", 24, callback);
            woyouService.printTextWithFont("Date " + date + "   Time " + time + "\n\n\n\n", "", 24, callback);

        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {

            case android.R.id.home:
                ((MainActivity) context).onBackPressed();
                return true;
        }
        return false;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        ((MainActivity) context).getSupportActionBar().setDisplayShowHomeEnabled(false);
        ((MainActivity) context).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((MainActivity) context).getSupportActionBar().setTitle("");
    }
}