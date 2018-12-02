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
import example.com.ballidaku.databinding.FragmentFirstBinding;
import example.com.ballidaku.mainSceens.MainActivity;
import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;

public class FirstFragment extends Fragment
{
    FragmentFirstBinding fragmentFirstBinding;
    View view;
    Context context;

    String rangeZoneID = "";

    int childrenPerCost = 0;
    int adultPerCost = 0;
    int packagePerCost = 0;
    int weeklyPerCost = 0;
    int fortnightlyPerCost = 0;
    int monthlyPerCost = 0;

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

        Bundle arguments = getArguments();
        if (arguments != null)
        {
            rangeZoneID = arguments.getString(MyConstants.RANGE_ZONE_ID, "");
        }

        ((MainActivity) context).updateToolbarTitle(getString(R.string.van_vihar_dhungri));

        ((MainActivity) context).setSupportActionBar(((MainActivity) context).activityMainBinding.toolbar);
        ((MainActivity) context).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((MainActivity) context).getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        childrenPerCost = MyConstants.VAN_VIHAR_DHUNGRI_CHILDREN_TICKET;
        adultPerCost = MyConstants.VAN_VIHAR_DHUNGRI_ADULT_TICKET;
        packagePerCost = MyConstants.PACKAGE;
        weeklyPerCost = MyConstants.WEEKLY;
        fortnightlyPerCost = MyConstants.FORTNIGHTLY;
        monthlyPerCost = MyConstants.MONTHLY;

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

        fragmentFirstBinding.numberPickerPackage.setOnValueChangedListener((picker, oldVal, newVal) ->
        {
            int value = picker.getValue() * packagePerCost;
            fragmentFirstBinding.textViewPackageTotal.setText(String.valueOf(value));
            updateCountTotal();
        });

        fragmentFirstBinding.numberPickerWeekly.setOnValueChangedListener((picker, oldVal, newVal) ->
        {
            int value = picker.getValue() * weeklyPerCost;
            fragmentFirstBinding.textViewWeeklyTotal.setText(String.valueOf(value));
            updateCountTotal();
        });

        fragmentFirstBinding.numberPickerFornightly.setOnValueChangedListener((picker, oldVal, newVal) ->
        {
            int value = picker.getValue() * fortnightlyPerCost;
            fragmentFirstBinding.textViewFornightlyTotal.setText(String.valueOf(value));
            updateCountTotal();
        });

        fragmentFirstBinding.numberPickerMonthly.setOnValueChangedListener((picker, oldVal, newVal) ->
        {
            int value = picker.getValue() * monthlyPerCost;
            fragmentFirstBinding.textViewMonthlyTotal.setText(String.valueOf(value));
            updateCountTotal();
        });
    }

    Model getModel()
    {
        Model model = new Model();
        model.setAdult("Adult\n@Rs. " + adultPerCost + "/-");
        model.setChildren("Children\n@Rs. " + childrenPerCost + "/-");
        model.setPackageValue("Package per day\n@Rs. " + packagePerCost + "/-");
        model.setWeekly("Weekly\n@Rs. " + weeklyPerCost + "/-");
        model.setFortNightly("Fortnightly\n@Rs. " + fortnightlyPerCost + "/-");
        model.setMonthly("Monthly\n@Rs. " + monthlyPerCost + "/-");
        return model;
    }

    void updateCountTotal()
    {

        int totalCount = fragmentFirstBinding.numberPickerAdult.getValue()
                + fragmentFirstBinding.numberPickerChildren.getValue()
                + fragmentFirstBinding.numberPickerPackage.getValue()
                + fragmentFirstBinding.numberPickerWeekly.getValue()
                + fragmentFirstBinding.numberPickerFornightly.getValue()
                + fragmentFirstBinding.numberPickerMonthly.getValue();


        int totalAmount = (fragmentFirstBinding.numberPickerAdult.getValue() * adultPerCost)
                + (fragmentFirstBinding.numberPickerChildren.getValue() * childrenPerCost)
                + (fragmentFirstBinding.numberPickerPackage.getValue() * packagePerCost)
                + (fragmentFirstBinding.numberPickerWeekly.getValue() * weeklyPerCost)
                + (fragmentFirstBinding.numberPickerFornightly.getValue() * fortnightlyPerCost)
                + (fragmentFirstBinding.numberPickerMonthly.getValue() * monthlyPerCost);


        fragmentFirstBinding.textViewCountTotal.setText(String.valueOf(totalCount));
        fragmentFirstBinding.textViewTotalRs.setText(String.valueOf(totalAmount));
    }

    public void onPrintClicked()
    {
        if (CommonMethods.getInstance().isInternetAvailable(context))
        {
            int adultCount = fragmentFirstBinding.numberPickerAdult.getValue();
            int adultTotal = adultCount * adultPerCost;

            int childrenCount = fragmentFirstBinding.numberPickerChildren.getValue();
            int childrenTotal = childrenCount * childrenPerCost;

            int packageCount = fragmentFirstBinding.numberPickerPackage.getValue();
            int packageTotal = packageCount * packagePerCost;

            int weeklyCount = fragmentFirstBinding.numberPickerWeekly.getValue();
            int weeklyTotal = weeklyCount * weeklyPerCost;

            int fortnightlyCount = fragmentFirstBinding.numberPickerFornightly.getValue();
            int fornightlyTotal = fortnightlyCount * fortnightlyPerCost;

            int monthlyCount = fragmentFirstBinding.numberPickerMonthly.getValue();
            int monthlyTotal = monthlyCount * monthlyPerCost;


            int countTotal = adultCount + childrenCount + packageCount + weeklyCount + fortnightlyCount + monthlyCount;
            int totalAmount = adultTotal + childrenTotal + packageTotal + weeklyTotal + fornightlyTotal + monthlyTotal;

            String childrenCountString = CommonMethods.getInstance().fixedLengthString(childrenCount, 3);
            String adultCountString = CommonMethods.getInstance().fixedLengthString(adultCount, 3);
            String packageCountString = CommonMethods.getInstance().fixedLengthString(packageCount, 3);
            String weeklyCountString = CommonMethods.getInstance().fixedLengthString(weeklyCount, 3);
            String fortnightlyCountString = CommonMethods.getInstance().fixedLengthString(fortnightlyCount, 3);
            String monthlyCountString = CommonMethods.getInstance().fixedLengthString(monthlyCount, 3);
            String countTotalString = CommonMethods.getInstance().fixedLengthString(countTotal, 3);

            String childrenTotalString = CommonMethods.getInstance().fixedLengthString(childrenTotal, 5);
            String adultTotalString = CommonMethods.getInstance().fixedLengthString(adultTotal, 5);
            String packageTotalString = CommonMethods.getInstance().fixedLengthString(packageTotal, 5);
            String weeklyTotalString = CommonMethods.getInstance().fixedLengthString(weeklyTotal, 5);
            String fortnightlyTotalString = CommonMethods.getInstance().fixedLengthString(fornightlyTotal, 5);
            String monthlyTotalString = CommonMethods.getInstance().fixedLengthString(monthlyTotal, 5);
            String totalAmountString = CommonMethods.getInstance().fixedLengthString(totalAmount, 5);


            TicketModel ticketModel = new TicketModel();

            ticketModel.setUserId(MySharedPreference.getInstance().getUserData(context, MyConstants.USERID));
            ticketModel.setRangeZoneId(rangeZoneID);

            ticketModel.setChildrenEntryTicketCount(childrenCountString);
            ticketModel.setChildrenEntryTicketTotal(childrenTotalString);

            ticketModel.setChildrenBatteryVehicleTicketCount("");
            ticketModel.setChildrenBatteryVehicleTicketTotal("");

            ticketModel.setAdultEntryTicketCount(adultCountString);
            ticketModel.setAdultEntryTicketTotal(adultTotalString);

            ticketModel.setForeignerEntryTicketCount("");
            ticketModel.setForeignerEntryTicketTotal("");

            ticketModel.setAdultBatteryVehicleTicketCount("");
            ticketModel.setAdultBatteryVehicleTicketTotal("");

            ticketModel.setBoatingTwoSeaterCount("");
            ticketModel.setBoatingTwoSeaterTotal("");

            ticketModel.setBoatingFourSeaterCount("");
            ticketModel.setBoatingFourSeaterTotal("");

            ticketModel.setPackagePerDayCount(packageCountString);
            ticketModel.setPackagePerDayTotal(packageTotalString);

            ticketModel.setWeeklyCount(weeklyCountString);
            ticketModel.setWeeklyTotal(weeklyTotalString);

            ticketModel.setFortnightlyCount(fortnightlyCountString);
            ticketModel.setFortnightlyTotal(fortnightlyTotalString);

            ticketModel.setMonthlyCount(monthlyCountString);
            ticketModel.setMonthlyTotal(monthlyTotalString);

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

            if(CommonMethods.getInstance().isTicketEmpty(view,context,totalAmountString))
            {
                ((MainActivity) context).saveTicketApi(ticketModel);
            }

        }
        else
        {
            CommonMethods.getInstance().showSnackbar(view, context, context.getString(R.string.internet_not_available));
        }



//        printTicket(childrenCountString, childrenTotalString,
//                adultCountString, adultTotalString,
//                packageCountString, packageTotalString,
//                weeklyCountString, weeklyTotalString,
//                fortnightlyCountString, fortnightlyTotalString,
//                monthlyCountString, monthlyTotalString,
//                countTotalString, totalAmountString);
    }


    /*void printTicket(String childrenCountString, String childrenTotalString,
                     String adultCountString, String adultTotalString,
                     String packageCountString, String packageTotalString,
                     String weeklyCountString, String weeklyTotalString,
                     String fortnightlyCountString, String fortnightlyTotalString,
                     String monthlyCountString, String monthlyTotalString,
                     String countTotalString, String totalAmountString)*/

    public void printTicket(TicketModel ticketModel)
    {
        IWoyouService woyouService = ((MainActivity) context).getWoyouService();
        ICallback callback = ((MainActivity) context).getCallback();

        try
        {
            String date = CommonMethods.getInstance().getDate();
            String time = CommonMethods.getInstance().getTime();

            String firstName = MySharedPreference.getInstance().getUserData(context, MyConstants.FIRSTNAME);
            String lastName = MySharedPreference.getInstance().getUserData(context, MyConstants.LASTNAME);
            String userId = MySharedPreference.getInstance().getUserData(context, MyConstants.USERID);

            /*woyouService.printTextWithFont("*************************\n", "", 30, callback);
            woyouService.printTextWithFont("       Van Vihar Dhungri       \n", "", 24, callback);
            woyouService.printTextWithFont("*************************\n", "", 30, callback);
            woyouService.printTextWithFont("Type           Count      Total\n", "", 24, callback);
            woyouService.printTextWithFont("-------------------------------\n", "", 24, callback);
            woyouService.printTextWithFont("Entry Ticket         \n", "", 24, callback);
            woyouService.printTextWithFont("-------------------------------\n", "", 24, callback);
            woyouService.printTextWithFont("Children       x" + childrenCountString + "      Rs " + childrenTotalString + "\n", "", 24, callback);
            woyouService.printTextWithFont("Adult          x" + adultCountString + "      Rs " + adultTotalString + "\n\n", "", 24, callback);

            woyouService.printTextWithFont("Other                 \n", "", 24, callback);
            woyouService.printTextWithFont("-------------------------------\n", "", 24, callback);
            woyouService.printTextWithFont("Package        x" + packageCountString + "      Rs " + packageTotalString + "\n", "", 24, callback);
            woyouService.printTextWithFont("Weekly         x" + weeklyCountString + "      Rs " + weeklyTotalString + "\n", "", 24, callback);
            woyouService.printTextWithFont("Fortnightly    x" + fortnightlyCountString + "      Rs " + fortnightlyTotalString + "\n", "", 24, callback);
            woyouService.printTextWithFont("Monthly        x" + monthlyCountString + "      Rs " + monthlyTotalString + "\n", "", 24, callback);
            woyouService.printTextWithFont("-------------------------------\n", "", 24, callback);
            woyouService.printTextWithFont("Total          x" + countTotalString + "      Rs " + totalAmountString + "\n", "", 24, callback);
            woyouService.printTextWithFont("-------------------------------\n", "", 24, callback);
            woyouService.printTextWithFont("Date " + date + "   Time " + time + "\n\n\n\n", "", 24, callback);*/

            woyouService.printTextWithFont("*************************\n", "", 30, callback);
            woyouService.printTextWithFont("       Van Vihar Dhungri       \n", "", 24, callback);
            woyouService.printTextWithFont("*************************\n", "", 30, callback);
            woyouService.printTextWithFont("Type           Count      Total\n", "", 24, callback);
            woyouService.printTextWithFont("-------------------------------\n", "", 24, callback);
            woyouService.printTextWithFont("Entry Ticket         \n", "", 24, callback);
            woyouService.printTextWithFont("-------------------------------\n", "", 24, callback);
            woyouService.printTextWithFont("Children       x" + ticketModel.getChildrenEntryTicketCount() + "       " + ticketModel.getChildrenEntryTicketTotal() + "\n", "", 24, callback);
            woyouService.printTextWithFont("Adult          x" + ticketModel.getAdultEntryTicketCount() + "       " + ticketModel.getAdultEntryTicketTotal() + "\n\n", "", 24, callback);

            woyouService.printTextWithFont("Other                 \n", "", 24, callback);
            woyouService.printTextWithFont("-------------------------------\n", "", 24, callback);
            woyouService.printTextWithFont("Package        x" + ticketModel.getPackagePerDayCount() + "       " + ticketModel.getPackagePerDayTotal() + "\n", "", 24, callback);
            woyouService.printTextWithFont("Weekly         x" + ticketModel.getWeeklyCount() + "       " + ticketModel.getWeeklyTotal() + "\n", "", 24, callback);
            woyouService.printTextWithFont("Fortnightly    x" + ticketModel.getFortnightlyCount() + "       " + ticketModel.getFortnightlyTotal() + "\n", "", 24, callback);
            woyouService.printTextWithFont("Monthly        x" + ticketModel.getMonthlyCount() + "       " + ticketModel.getMonthlyTotal() + "\n", "", 24, callback);
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
