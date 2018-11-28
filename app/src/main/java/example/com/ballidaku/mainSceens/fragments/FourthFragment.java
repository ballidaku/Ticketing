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
import example.com.ballidaku.commonClasses.MySharedPreference;
import example.com.ballidaku.commonClasses.TicketModel;
import example.com.ballidaku.databinding.FragmentFourthBinding;
import example.com.ballidaku.mainSceens.MainActivity;
import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;

public class FourthFragment extends Fragment
{
    FragmentFourthBinding fragmentFourthBinding;
    View view;
    Context context;

    String rangeZoneID = "";
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
        Bundle arguments = getArguments();
        if (arguments != null)
        {
            rangeZoneID = arguments.getString(MyConstants.RANGE_ZONE_ID, "");
        }

        ((MainActivity) context).updateToolbarTitle(getString(R.string.mini_zoo_rewalsar));

        ((MainActivity) context).setSupportActionBar(((MainActivity) context).activityMainBinding.toolbar);
        ((MainActivity) context).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((MainActivity) context).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                + fragmentFourthBinding.numberPickerForeigners.getValue();

        int totalAmount = (fragmentFourthBinding.numberPickerAdult.getValue() * adultPerCost)
                + (fragmentFourthBinding.numberPickerChildren.getValue() * childrenPerCost)
                + (fragmentFourthBinding.numberPickerForeigners.getValue() * foreignerPerCost);

        fragmentFourthBinding.textViewTotalCount.setText(String.valueOf(totalCount));
        fragmentFourthBinding.textViewTotalAmount.setText(String.valueOf(totalAmount));
    }

    public void onPrintClicked()
    {
        if (CommonMethods.getInstance().isInternetAvailable(context))
        {
            int adultCount = fragmentFourthBinding.numberPickerAdult.getValue();
            int adultTotal = adultCount * adultPerCost;

            int childrenCount = fragmentFourthBinding.numberPickerChildren.getValue();
            int childrenTotal = childrenCount * childrenPerCost;

            int foreignerCount = fragmentFourthBinding.numberPickerForeigners.getValue();
            int foreignerTotal = foreignerCount * foreignerPerCost;

            int countTotal = adultCount + childrenCount + foreignerCount;
            int totalAmount = adultTotal + childrenTotal + foreignerTotal;

            String childrenCountString = CommonMethods.getInstance().fixedLengthString(childrenCount, 3);
            String adultCountString = CommonMethods.getInstance().fixedLengthString(adultCount, 3);
            String foreignerCountString = CommonMethods.getInstance().fixedLengthString(foreignerCount, 3);

            String countTotalString = CommonMethods.getInstance().fixedLengthString(countTotal, 3);

            String childrenTotalString = CommonMethods.getInstance().fixedLengthString(childrenTotal, 5);
            String adultTotalString = CommonMethods.getInstance().fixedLengthString(adultTotal, 5);
            String foreignerTotalString = CommonMethods.getInstance().fixedLengthString(foreignerTotal, 5);


            String totalAmountString = CommonMethods.getInstance().fixedLengthString(totalAmount, 5);

       /* printTicket(childrenCountString, childrenTotalString,
                adultCountString, adultTotalString,
                foreignerCountString,foreignerTotalString,
                countTotalString, totalAmountString);*/

            TicketModel ticketModel = new TicketModel();

            ticketModel.setUserId(MySharedPreference.getInstance().getUserData(context, MyConstants.USERID));
            ticketModel.setRangeZoneId(rangeZoneID);

            ticketModel.setChildrenEntryTicketCount(childrenCountString);
            ticketModel.setChildrenEntryTicketTotal(childrenTotalString);

            ticketModel.setChildrenBatteryVehicleTicketCount("");
            ticketModel.setChildrenBatteryVehicleTicketTotal("");

            ticketModel.setAdultEntryTicketCount(adultCountString);
            ticketModel.setAdultEntryTicketTotal(adultTotalString);

            ticketModel.setForeignerEntryTicketCount(foreignerCountString);
            ticketModel.setForeignerEntryTicketTotal(foreignerTotalString);

            ticketModel.setAdultBatteryVehicleTicketCount("");
            ticketModel.setAdultBatteryVehicleTicketTotal("");

            ticketModel.setBoatingTwoSeaterCount("");
            ticketModel.setBoatingTwoSeaterTotal("");

            ticketModel.setBoatingFourSeaterCount("");
            ticketModel.setBoatingFourSeaterTotal("");

            ticketModel.setPackagePerDayCount("");
            ticketModel.setPackagePerDayTotal("");

            ticketModel.setWeeklyCount("");
            ticketModel.setWeeklyTotal("");

            ticketModel.setFortnightlyCount("");
            ticketModel.setFortnightlyTotal("");

            ticketModel.setMonthlyCount("");
            ticketModel.setMonthlyTotal("");

            ticketModel.setAllEntryTicketCount("");
            ticketModel.setAllEntryTicketTotal("");

            ticketModel.setParkingUpto4hoursCount("");
            ticketModel.setParkingUpto4hoursTotal("");

            ticketModel.setParkingUpto8hoursCount("");
            ticketModel.setParkingUpto8hoursTotal("");

            ticketModel.setParkingUpto12hoursCount("");
            ticketModel.setParkingUpto12hoursTotal("");

            ticketModel.setTotalCount(countTotalString);
            ticketModel.setTotalAmount(totalAmountString);

            ((MainActivity) context).saveTicketApi(ticketModel);
        }
        else
        {
            CommonMethods.getInstance().showSnackbar(view, context, context.getString(R.string.internet_not_available));
        }

    }


    //    void printTicket(String childrenCountString, String childrenTotalString, String adultCountString, String adultTotalString, String foreignerCountString,String foreignerTotalString,String countTotalString, String totalAmountString)
    public void printTicket(TicketModel ticketModel)
    {
        IWoyouService woyouService = ((MainActivity) context).getWoyouService();
        ICallback callback = ((MainActivity) context).getCallback();

        try
        {
            String firstName = MySharedPreference.getInstance().getUserData(context, MyConstants.FIRSTNAME);
            String lastName = MySharedPreference.getInstance().getUserData(context, MyConstants.LASTNAME);
            String userId = MySharedPreference.getInstance().getUserData(context, MyConstants.USERID);


            String date = CommonMethods.getInstance().getDate();
            String time = CommonMethods.getInstance().getTime();

            woyouService.printTextWithFont("*************************\n", "", 30, callback);
            woyouService.printTextWithFont("       Mini Zoo Rewalsar       \n", "", 24, callback);
            woyouService.printTextWithFont("*************************\n", "", 30, callback);
            woyouService.printTextWithFont("Type           Count      Total\n", "", 24, callback);
            woyouService.printTextWithFont("-------------------------------\n", "", 24, callback);
            woyouService.printTextWithFont("Entry Ticket         \n", "", 24, callback);
            woyouService.printTextWithFont("-------------------------------\n", "", 24, callback);
            woyouService.printTextWithFont("Children       x" + ticketModel.getChildrenEntryTicketCount() + "       " + ticketModel.getChildrenEntryTicketTotal() + "\n", "", 24, callback);
            woyouService.printTextWithFont("Adult          x" + ticketModel.getAdultEntryTicketCount() + "       " + ticketModel.getAdultEntryTicketTotal() + "\n", "", 24, callback);
            woyouService.printTextWithFont("Foreigners     x" + ticketModel.getForeignerEntryTicketCount() + "       " + ticketModel.getForeignerEntryTicketTotal() + "\n", "", 24, callback);
            woyouService.printTextWithFont("-------------------------------\n", "", 24, callback);
            woyouService.printTextWithFont("Total          x" + ticketModel.getTotalCount() + "    Rs " + ticketModel.getTotalAmount() + "\n", "", 24, callback);
            woyouService.printTextWithFont("-------------------------------\n", "", 24, callback);
            woyouService.printTextWithFont("Date " + date + "   Time " + time + "\n", "", 24, callback);
            woyouService.printTextWithFont("Ticket Number : " + ticketModel.getTicketId() + "\n", "", 24, callback);
            woyouService.printTextWithFont("Ticket By : " + firstName + " " + lastName + " (" + userId + ")" + "\n\n\n\n", "", 24, callback);

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
