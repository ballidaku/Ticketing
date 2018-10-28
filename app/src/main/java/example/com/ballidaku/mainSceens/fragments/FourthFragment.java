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
import example.com.ballidaku.databinding.FragmentFourthBinding;
import example.com.ballidaku.mainSceens.MainActivity;
import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;

public class FourthFragment extends Fragment
{
    FragmentFourthBinding fragmentFourthBinding;
    View view;
    Context context;

    int childrenPerCost = 0;
    int adultPerCost = 0;
    int foreignerPerCost = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        fragmentFourthBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_fourth, container, false);
        view = fragmentFourthBinding.getRoot();
        context = getContext();
        fragmentFourthBinding.setHandler(this);


        setUpViews();

        return view;
    }

    void setUpViews()
    {
        ((MainActivity) context).updateToolbarTitle(getString(R.string.mini_zoo_rewalsar));

        childrenPerCost = MyConstants.MINI_ZOO_REWALSAR_CHILDREN_TICKET;
        adultPerCost = MyConstants.MINI_ZOO_REWALSAR_ADULT_TICKET;
        foreignerPerCost = MyConstants.MINI_ZOO_REWALSAR_FOREIGNER_TICKET;

        fragmentFourthBinding.setViewModel(getModel());

        fragmentFourthBinding.numberPickerChildren.setOnValueChangedListener((picker, oldVal, newVal) ->
        {
            int value = picker.getValue() * childrenPerCost;
            fragmentFourthBinding.textViewChildrenTotal.setText(String.valueOf(value));
            updateCountTotal();
        });

        fragmentFourthBinding.numberPickerAdult.setOnValueChangedListener((picker, oldVal, newVal) ->
        {
            int value = picker.getValue() * adultPerCost;
            fragmentFourthBinding.textViewAdultTotal.setText(String.valueOf(value));
            updateCountTotal();
        });

        fragmentFourthBinding.numberPickerForeigners.setOnValueChangedListener((picker, oldVal, newVal) ->
        {
            int value = picker.getValue() * foreignerPerCost;
            fragmentFourthBinding.textViewForeignersTotal.setText(String.valueOf(value));
            updateCountTotal();
        });

    }

    Model getModel()
    {
        Model model = new Model();
        model.setAdult("Adult\n@Rs. " + adultPerCost + "/-");
        model.setChildren("Children\n@Rs. " + childrenPerCost + "/-");
        model.setForeigner("Foreigners\n@Rs. " + foreignerPerCost + "/-");
        return model;
    }

    void updateCountTotal()
    {

        int totalCount = fragmentFourthBinding.numberPickerAdult.getValue()
                + fragmentFourthBinding.numberPickerChildren.getValue()
                +fragmentFourthBinding.numberPickerForeigners.getValue();

        int totalAmount = (fragmentFourthBinding.numberPickerAdult.getValue() * adultPerCost)
                + (fragmentFourthBinding.numberPickerChildren.getValue() * childrenPerCost)
                +(fragmentFourthBinding.numberPickerForeigners.getValue() * foreignerPerCost);

        fragmentFourthBinding.textViewTotalCount.setText(String.valueOf(totalCount));
        fragmentFourthBinding.textViewTotalAmount.setText(String.valueOf(totalAmount));
    }

    public void onPrintClicked()
    {
        int adultCount = fragmentFourthBinding.numberPickerAdult.getValue();
        int adultTotal = adultCount * adultPerCost;

        int childrenCount = fragmentFourthBinding.numberPickerChildren.getValue();
        int childrenTotal = childrenCount * childrenPerCost;

        int foreignerCount = fragmentFourthBinding.numberPickerForeigners.getValue();
        int foreignerTotal = foreignerCount * foreignerPerCost;

        int countTotal = adultCount + childrenCount +foreignerCount;
        int totalAmount = adultTotal + childrenTotal +foreignerTotal;

        String childrenCountString = CommonMethods.getInstance().fixedLengthString(childrenCount, 3);
        String adultCountString = CommonMethods.getInstance().fixedLengthString(adultCount, 3);
        String foreignerCountString = CommonMethods.getInstance().fixedLengthString(foreignerCount, 3);

        String countTotalString = CommonMethods.getInstance().fixedLengthString(countTotal, 3);

        String childrenTotalString = CommonMethods.getInstance().fixedLengthString(childrenTotal, 4);
        String adultTotalString = CommonMethods.getInstance().fixedLengthString(adultTotal, 4);
        String foreignerTotalString = CommonMethods.getInstance().fixedLengthString(foreignerTotal, 4);


        String totalAmountString = CommonMethods.getInstance().fixedLengthString(totalAmount, 4);

        printTicket(childrenCountString, childrenTotalString, adultCountString, adultTotalString,foreignerCountString,foreignerTotalString, countTotalString, totalAmountString);
    }


    void printTicket(String childrenCountString, String childrenTotalString, String adultCountString, String adultTotalString, String foreignerCountString,String foreignerTotalString,String countTotalString, String totalAmountString)
    {
        IWoyouService woyouService = ((MainActivity) context).getWoyouService();
        ICallback callback = ((MainActivity) context).getCallback();

        try
        {
            String date = CommonMethods.getInstance().getDate();
            String time = CommonMethods.getInstance().getTime();

            woyouService.printTextWithFont("*************************\n", "", 30, callback);
            woyouService.printTextWithFont("       Mini Zoo Rewalsar       \n", "", 24, callback);
            woyouService.printTextWithFont("*************************\n", "", 30, callback);
            woyouService.printTextWithFont("Type           Count      Total\n", "", 24, callback);
            woyouService.printTextWithFont("-------------------------------\n", "", 24, callback);
            woyouService.printTextWithFont("Entry Ticket         \n", "", 24, callback);
            woyouService.printTextWithFont("-------------------------------\n", "", 24, callback);
            woyouService.printTextWithFont("Children       x" + childrenCountString + "      Rs " + childrenTotalString + "\n", "", 24, callback);
            woyouService.printTextWithFont("Adult          x" + adultCountString + "      Rs " + adultTotalString + "\n", "", 24, callback);
            woyouService.printTextWithFont("Foreigners     x" + foreignerCountString + "      Rs " + foreignerTotalString + "\n", "", 24, callback);
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
