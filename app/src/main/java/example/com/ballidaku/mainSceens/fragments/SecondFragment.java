package example.com.ballidaku.mainSceens.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import example.com.ballidaku.R;
import example.com.ballidaku.commonClasses.CommonMethods;
import example.com.ballidaku.commonClasses.Model;
import example.com.ballidaku.commonClasses.MyConstants;
import example.com.ballidaku.databinding.FragmentSecondBinding;
import example.com.ballidaku.mainSceens.MainActivity;
import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;

public class SecondFragment extends Fragment
{
    FragmentSecondBinding fragmentSecondBinding;
    View view;
    Context context;

    int childrenPerCost = 0;
    int adultPerCost = 0;
    int childrenBatteryOperatorPerCost = 0;
    int adultBatteryOperatorPerCost = 0;
    int boatingTwoSeaterPerCost = 0;
    int boatingFourSeaterPerCost = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        fragmentSecondBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_second, container, false);
        view = fragmentSecondBinding.getRoot();
        context = getContext();
        fragmentSecondBinding.setHandler(this);

        setUpViews();

        return view;
    }

    void setUpViews()
    {
        ((MainActivity) context).updateToolbarTitle(getString(R.string.van_vihar_manali));

        childrenPerCost = MyConstants.VAN_VIHAR_MANALI_CHILDREN_TICKET;
        adultPerCost = MyConstants.VAN_VIHAR_MANALI_ADULT_TICKET;

        childrenBatteryOperatorPerCost = MyConstants.VAN_VIHAR_MANALI_CHILDREN_TICKET_BATTERY_OPERATOR;
        adultBatteryOperatorPerCost = MyConstants.VAN_VIHAR_MANALI_ADULT_TICKET_BATTERY_OPERATOR;

        boatingTwoSeaterPerCost = MyConstants.VAN_VIHAR_MANALI_BOATING_TWO_SEATER;
        boatingFourSeaterPerCost = MyConstants.VAN_VIHAR_MANALI_BOATING_FOUR_SEATER;

        fragmentSecondBinding.setViewModel(getModel());


        //******************************************************************************************
        // Entry ticket
        //******************************************************************************************

        fragmentSecondBinding.numberPickerChildren.setOnValueChangedListener((picker, oldVal, newVal) ->
        {
            int value = picker.getValue() * childrenPerCost;
            fragmentSecondBinding.textViewChildrenTotal.setText(String.valueOf(value));
            updateCountTotal();
        });

        fragmentSecondBinding.numberPickerAdult.setOnValueChangedListener((picker, oldVal, newVal) ->
        {
            int value = picker.getValue() * adultPerCost;
            fragmentSecondBinding.textViewAdultTotal.setText(String.valueOf(value));
            updateCountTotal();
        });

        //******************************************************************************************
        // Battery Operator
        //******************************************************************************************

        fragmentSecondBinding.numberPickerChildrenBatteryOperator.setOnValueChangedListener((picker, oldVal, newVal) ->
        {
            int value = picker.getValue() * childrenBatteryOperatorPerCost;
            fragmentSecondBinding.textViewChildrenTotalBatteryOperator.setText(String.valueOf(value));
            updateCountTotal();
        });

        fragmentSecondBinding.numberPickerAdultBatteryOperator.setOnValueChangedListener((picker, oldVal, newVal) ->
        {
            int value = picker.getValue() * adultBatteryOperatorPerCost;
            fragmentSecondBinding.textViewAdultTotalBatteryOperator.setText(String.valueOf(value));
            updateCountTotal();
        });

        //******************************************************************************************
        // Boating
        //******************************************************************************************

        fragmentSecondBinding.numberPickerTwoSeaterBoating.setOnValueChangedListener((picker, oldVal, newVal) ->
        {
            int value = picker.getValue() * boatingTwoSeaterPerCost;
            fragmentSecondBinding.textViewTwoSeaterBoatingTotal.setText(String.valueOf(value));
            updateCountTotal();
        });

        fragmentSecondBinding.numberPickerFourSeaterBoating.setOnValueChangedListener((picker, oldVal, newVal) ->
        {
            int value = picker.getValue() * boatingFourSeaterPerCost;
            fragmentSecondBinding.textViewFourSeaterBoatingTotal.setText(String.valueOf(value));
            updateCountTotal();
        });
    }

    Model getModel()
    {
        Model model = new Model();
        model.setAdult("Adult\n@Rs. " + adultPerCost + "/-");
        model.setChildren("Children\n@Rs. " + childrenPerCost + "/-");

        model.setAdultBatteryOperator("Adult\n@Rs. " + adultBatteryOperatorPerCost + "/-");
        model.setChildrenBatteryOperator("Children\n@Rs. " + childrenBatteryOperatorPerCost + "/-");

        model.setTwoSeaterBoating("Two Seater\n@Rs. " + boatingTwoSeaterPerCost + "/-");
        model.setFourSeaterBoating("Four Seater\n@Rs. " + boatingFourSeaterPerCost + "/-");

        return model;
    }


    void updateCountTotal()
    {

        int totalPersonCount = fragmentSecondBinding.numberPickerAdult.getValue()
                + fragmentSecondBinding.numberPickerChildren.getValue()
                + fragmentSecondBinding.numberPickerChildrenBatteryOperator.getValue()
                + fragmentSecondBinding.numberPickerAdultBatteryOperator.getValue()
                + fragmentSecondBinding.numberPickerTwoSeaterBoating.getValue()
                + fragmentSecondBinding.numberPickerFourSeaterBoating.getValue();


        int totalAmount = (fragmentSecondBinding.numberPickerAdult.getValue() * adultPerCost)
                + (fragmentSecondBinding.numberPickerChildren.getValue() * childrenPerCost)
                + (fragmentSecondBinding.numberPickerChildrenBatteryOperator.getValue() * childrenBatteryOperatorPerCost)
                + (fragmentSecondBinding.numberPickerAdultBatteryOperator.getValue() * adultBatteryOperatorPerCost)
                + (fragmentSecondBinding.numberPickerTwoSeaterBoating.getValue() * boatingTwoSeaterPerCost)
                + (fragmentSecondBinding.numberPickerFourSeaterBoating.getValue() * boatingFourSeaterPerCost);


        fragmentSecondBinding.textViewCountTotal.setText(String.valueOf(totalPersonCount));
        fragmentSecondBinding.textViewTotalRs.setText(String.valueOf(totalAmount));
    }

    public void onPrintClicked()
    {
        //******************************************************************************************
        // Entry ticket
        //******************************************************************************************

        int adultCount = fragmentSecondBinding.numberPickerAdult.getValue();
        int adultTotal = fragmentSecondBinding.numberPickerAdult.getValue() * adultPerCost;

        int childrenCount = fragmentSecondBinding.numberPickerChildren.getValue();
        int childrenTotal = fragmentSecondBinding.numberPickerChildren.getValue() * childrenPerCost;

        int entryCountTotal = adultCount + childrenCount;
        int totalEntryAmount = adultTotal + childrenTotal;

        String childrenCountString = CommonMethods.getInstance().fixedLengthString(childrenCount, 3);
        String adultCountString = CommonMethods.getInstance().fixedLengthString(adultCount, 3);

        String childrenTotalString = CommonMethods.getInstance().fixedLengthString(childrenTotal, 4);
        String adultTotalString = CommonMethods.getInstance().fixedLengthString(adultTotal, 4);


        //******************************************************************************************
        // Battery Operator
        //******************************************************************************************

        int childrenBatteryCount = fragmentSecondBinding.numberPickerChildrenBatteryOperator.getValue();
        int childrenBatteryTotal = childrenBatteryCount * childrenBatteryOperatorPerCost;

        int adultBatteryCount = fragmentSecondBinding.numberPickerAdultBatteryOperator.getValue();
        int adultBatteryTotal = adultBatteryCount * adultBatteryOperatorPerCost;

        int batteryCountTotal = adultBatteryCount + childrenBatteryCount;
        int totalBatteryAmount = adultBatteryTotal + childrenBatteryTotal;

        String childrenBatteryCountString = CommonMethods.getInstance().fixedLengthString(childrenBatteryCount, 3);
        String adultBatteryCountString = CommonMethods.getInstance().fixedLengthString(adultBatteryCount, 3);

        String childrenBatteryTotalString = CommonMethods.getInstance().fixedLengthString(childrenBatteryTotal, 4);
        String adultBatteryTotalString = CommonMethods.getInstance().fixedLengthString(adultBatteryTotal, 4);




        //******************************************************************************************
        // Boating
        //******************************************************************************************

        int twoSeaterBoatingCount = fragmentSecondBinding.numberPickerTwoSeaterBoating.getValue();
        int twoSeaterBoatingTotal = twoSeaterBoatingCount * boatingTwoSeaterPerCost;

        int fourSeaterBoatingCount = fragmentSecondBinding.numberPickerFourSeaterBoating.getValue();
        int fourSeaterBoatingTotal = fourSeaterBoatingCount * boatingFourSeaterPerCost;

        int boatingCountTotal = twoSeaterBoatingCount + fourSeaterBoatingCount;
        int totalBoatingAmount = twoSeaterBoatingTotal + fourSeaterBoatingTotal;

        String twoSeaterBoatingCountString = CommonMethods.getInstance().fixedLengthString(twoSeaterBoatingCount, 3);
        String fourSeaterBoatingCountString = CommonMethods.getInstance().fixedLengthString(fourSeaterBoatingCount, 3);

        String twoSeaterBoatingTotalString = CommonMethods.getInstance().fixedLengthString(twoSeaterBoatingTotal, 4);
        String fourSeaterBoatingTotalString = CommonMethods.getInstance().fixedLengthString(fourSeaterBoatingTotal, 4);

        //******************************************************************************************
        //******************************************************************************************

        int countTotal=entryCountTotal+batteryCountTotal+boatingCountTotal;
        String countTotalString = CommonMethods.getInstance().fixedLengthString(countTotal, 3);

        int total=totalEntryAmount+totalBatteryAmount+totalBoatingAmount;
        String totalAmountString = CommonMethods.getInstance().fixedLengthString(total, 4);

        printTicket(childrenCountString, childrenTotalString, adultCountString, adultTotalString,
                childrenBatteryCountString,childrenBatteryTotalString,adultBatteryCountString,adultBatteryTotalString,
                twoSeaterBoatingCountString,twoSeaterBoatingTotalString,fourSeaterBoatingCountString,fourSeaterBoatingTotalString,
                countTotalString, totalAmountString);
    }


    void printTicket(String childrenCountString, String childrenTotalString, String adultCountString, String adultTotalString,
                     String childrenBatteryCountString,String childrenBatteryTotalString, String adultBatteryCountString, String adultBatteryTotalString,
                     String twoSeaterBoatingCountString,String twoSeaterBoatingTotalString, String fourSeaterBoatingCountString, String fourSeaterBoatingTotalString,
                     String countTotalString, String totalAmountString)
    {
        IWoyouService woyouService = ((MainActivity) context).getWoyouService();
        ICallback callback = ((MainActivity) context).getCallback();

        try
        {
            String date = CommonMethods.getInstance().getDate();
            String time = CommonMethods.getInstance().getTime();

            woyouService.printTextWithFont("*************************\n", "", 30, callback);
            woyouService.printTextWithFont("       Van Vihar Manali       \n", "", 24, callback);
            woyouService.printTextWithFont("*************************\n", "", 30, callback);
            woyouService.printTextWithFont("Type           Count      Total\n", "", 24, callback);
            woyouService.printTextWithFont("-------------------------------\n", "", 24, callback);
            woyouService.printTextWithFont("Entry Ticket                   \n", "", 24, callback);
            woyouService.printTextWithFont("-------------------------------\n", "", 24, callback);
            woyouService.printTextWithFont("Children       x" + childrenCountString + "      Rs " + childrenTotalString + "\n", "", 24, callback);
            woyouService.printTextWithFont("Adult          x" + adultCountString + "      Rs " + adultTotalString + "\n\n", "", 24, callback);

            woyouService.printTextWithFont("Battery Operator Vehicle Ticket\n", "", 24, callback);
            woyouService.printTextWithFont("-------------------------------\n", "", 24, callback);
            woyouService.printTextWithFont("Children       x" + childrenBatteryCountString + "      Rs " + childrenBatteryTotalString + "\n", "", 24, callback);
            woyouService.printTextWithFont("Adult          x" + adultBatteryCountString + "      Rs " + adultBatteryTotalString + "\n\n", "", 24, callback);

            woyouService.printTextWithFont("Boating Ticket                 \n", "", 24, callback);
            woyouService.printTextWithFont("-------------------------------\n", "", 24, callback);
            woyouService.printTextWithFont("Two  Seater    x" + twoSeaterBoatingCountString + "      Rs " + twoSeaterBoatingTotalString + "\n", "", 24, callback);
            woyouService.printTextWithFont("Four Seater    x" + fourSeaterBoatingCountString + "      Rs " + fourSeaterBoatingTotalString + "\n", "", 24, callback);


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
}
