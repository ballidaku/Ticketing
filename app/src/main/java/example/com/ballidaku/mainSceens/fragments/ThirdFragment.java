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
import example.com.ballidaku.databinding.FragmentThirdBinding;
import example.com.ballidaku.mainSceens.MainActivity;
import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;

public class ThirdFragment  extends Fragment
{
    FragmentThirdBinding fragmentThirdBinding;
    View view;
    Context context;

    int allPerCost = 0;

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

        allPerCost = MyConstants.ALL_NATURE_PARK_MANALI;

        fragmentThirdBinding.setViewModel(getModel());

        fragmentThirdBinding.numberPickerAllCount.setOnValueChangedListener((picker, oldVal, newVal) ->
        {
            int value = picker.getValue() * allPerCost;
            fragmentThirdBinding.textViewAllTotal.setText(String.valueOf(value));
            updateCountTotal();
        });

    }

    Model getModel()
    {
        Model model = new Model();
        model.setAll("All\n@Rs. " + allPerCost + "/-");
        return model;
    }
    

    void updateCountTotal()
    {

        int totalCount = fragmentThirdBinding.numberPickerAllCount.getValue();
        int totalAmount = (totalCount * allPerCost) ;
        fragmentThirdBinding.textViewTotalCount.setText(String.valueOf(totalCount));
        fragmentThirdBinding.textViewTotalAmount.setText(String.valueOf(totalAmount));
    }

    public void onPrintClicked()
    {
        int allTotalCount = fragmentThirdBinding.numberPickerAllCount.getValue();
        int allTotalAmount = allTotalCount * allPerCost;

        String allCountString = CommonMethods.getInstance().fixedLengthString(allTotalCount, 3);
        String allTotalString = CommonMethods.getInstance().fixedLengthString(allTotalAmount, 4);

        printTicket(allCountString, allTotalString);
    }


    void printTicket(String allCountString, String allTotalString)
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
            woyouService.printTextWithFont("All            x" + allCountString + "      Rs " + allTotalString + "\n", "", 24, callback);
            woyouService.printTextWithFont("-------------------------------\n", "", 24, callback);
            woyouService.printTextWithFont("Total          x" + allCountString + "      Rs " + allTotalString + "\n", "", 24, callback);
            woyouService.printTextWithFont("-------------------------------\n", "", 24, callback);
            woyouService.printTextWithFont("Date " + date + "   Time " + time + "\n\n\n\n", "", 24, callback);

        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }
}