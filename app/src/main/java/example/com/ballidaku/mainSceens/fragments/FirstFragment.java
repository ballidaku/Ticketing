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
import example.com.ballidaku.databinding.FragmentFirstBinding;
import example.com.ballidaku.mainSceens.MainActivity;
import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;

public class FirstFragment extends Fragment
{
    FragmentFirstBinding fragmentFirstBinding;
    View view;
    Context context;

    int childrenPerCost = 0;
    int adultPerCost = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        fragmentFirstBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_first, container, false);
        view = fragmentFirstBinding.getRoot();
        context = getContext();
        fragmentFirstBinding.setHandler(this);


        setUpViews();

        return view;
    }

    void setUpViews()
    {
        ((MainActivity) context).updateToolbarTitle(getString(R.string.van_vihar_dhungri));

        childrenPerCost = MyConstants.VAN_VIHAR_DHUNGRI_CHILDREN_TICKET;
        adultPerCost = MyConstants.VAN_VIHAR_DHUNGRI_ADULT_TICKET;

        fragmentFirstBinding.setViewModel(getModel());

        fragmentFirstBinding.numberPickerChildren.setOnValueChangedListener((picker, oldVal, newVal) ->
        {
            int value = picker.getValue() * childrenPerCost;
            fragmentFirstBinding.textViewChildrenTotal.setText(String.valueOf(value));
            updateCountTotal();
        });

        fragmentFirstBinding.numberPickerAdult.setOnValueChangedListener((picker, oldVal, newVal) ->
        {
            int value = picker.getValue() * adultPerCost;
            fragmentFirstBinding.textViewAdultTotal.setText(String.valueOf(value));
            updateCountTotal();
        });
    }

    Model getModel()
    {
        Model model = new Model();
        model.setAdult("Adult\n@Rs. " + adultPerCost + "/-");
        model.setChildren("Children\n@Rs. " + childrenPerCost + "/-");
        return model;
    }

    void updateCountTotal()
    {

        int totalCount = fragmentFirstBinding.numberPickerAdult.getValue() + fragmentFirstBinding.numberPickerChildren.getValue();
        int totalAmount = (fragmentFirstBinding.numberPickerAdult.getValue() * adultPerCost) + (fragmentFirstBinding.numberPickerChildren.getValue() * childrenPerCost);
        fragmentFirstBinding.textViewCountTotal.setText(String.valueOf(totalCount));
        fragmentFirstBinding.textViewTotalRs.setText(String.valueOf(totalAmount));
    }

    public void onPrintClicked()
    {
        int adultCount = fragmentFirstBinding.numberPickerAdult.getValue();
        int adultTotal = fragmentFirstBinding.numberPickerAdult.getValue() * adultPerCost;

        int childrenCount = fragmentFirstBinding.numberPickerChildren.getValue();
        int childrenTotal = fragmentFirstBinding.numberPickerChildren.getValue() * childrenPerCost;

        int countTotal = adultCount + childrenCount;
        int totalAmount = adultTotal + childrenTotal;

        String childrenCountString = CommonMethods.getInstance().fixedLengthString(childrenCount, 3);
        String adultCountString = CommonMethods.getInstance().fixedLengthString(adultCount, 3);
        String countTotalString = CommonMethods.getInstance().fixedLengthString(countTotal, 3);

        String childrenTotalString = CommonMethods.getInstance().fixedLengthString(childrenTotal, 4);
        String adultTotalString = CommonMethods.getInstance().fixedLengthString(adultTotal, 4);
        String totalAmountString = CommonMethods.getInstance().fixedLengthString(totalAmount, 4);

        printTicket(childrenCountString, childrenTotalString, adultCountString, adultTotalString, countTotalString, totalAmountString);
    }


    void printTicket(String childrenCountString, String childrenTotalString, String adultCountString, String adultTotalString, String countTotalString, String totalAmountString)
    {
        IWoyouService woyouService = ((MainActivity) context).getWoyouService();
        ICallback callback = ((MainActivity) context).getCallback();

        try
        {
            String date = CommonMethods.getInstance().getDate();
            String time = CommonMethods.getInstance().getTime();

            woyouService.printTextWithFont("*************************\n", "", 30, callback);
            woyouService.printTextWithFont("       Van Vihar Dhungri       \n", "", 24, callback);
            woyouService.printTextWithFont("*************************\n", "", 30, callback);
            woyouService.printTextWithFont("Type           Count      Total\n", "", 24, callback);
            woyouService.printTextWithFont("-------------------------------\n", "", 24, callback);
            woyouService.printTextWithFont("Entry Ticket         \n", "", 24, callback);
            woyouService.printTextWithFont("-------------------------------\n", "", 24, callback);
            woyouService.printTextWithFont("Children       x" + childrenCountString + "      Rs " + childrenTotalString + "\n", "", 24, callback);
            woyouService.printTextWithFont("Adult          x" + adultCountString + "      Rs " + adultTotalString + "\n", "", 24, callback);
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
