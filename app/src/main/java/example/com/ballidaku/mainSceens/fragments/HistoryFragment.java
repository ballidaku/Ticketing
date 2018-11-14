package example.com.ballidaku.mainSceens.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import example.com.ballidaku.R;
import example.com.ballidaku.adapters.HistoryFragmentAdapter;
import example.com.ballidaku.commonClasses.CommonDialogs;
import example.com.ballidaku.commonClasses.CommonMethods;
import example.com.ballidaku.commonClasses.HistoryModel;
import example.com.ballidaku.commonClasses.MyConstants;
import example.com.ballidaku.commonClasses.MySharedPreference;
import example.com.ballidaku.databinding.FragmentHistoryBinding;
import example.com.ballidaku.mainSceens.MainActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HistoryFragment extends Fragment
{
    String TAG = HistoryFragment.class.getSimpleName();
    FragmentHistoryBinding fragmentHistoryBinding;
    View view;
    Context context;
    HistoryFragmentAdapter historyFragmentAdapter;

    List<HistoryModel.ListReportTicketCollectionModel> historyModelArrayList = new ArrayList<>();
    String userId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragmentHistoryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false);
        fragmentHistoryBinding.setHandler(this);
        view = fragmentHistoryBinding.getRoot();
        context = getActivity();
        setUpViews();

        return view;
    }

    private void setUpViews()
    {
        ((MainActivity) context).updateToolbarTitle(getString(R.string.history));
        ((MainActivity) context).setSupportActionBar(((MainActivity) context).activityMainBinding.toolbar);
        ((MainActivity) context).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((MainActivity) context).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userId = MySharedPreference.getInstance().getUserData(context, MyConstants.USERID);

        historyFragmentAdapter = new HistoryFragmentAdapter(context, historyModelArrayList);
        fragmentHistoryBinding.recycleView.setItemAnimator(new DefaultItemAnimator());
        fragmentHistoryBinding.recycleView.setAdapter(historyFragmentAdapter);

        fragmentHistoryBinding.linearLayoutFromTo.setVisibility(View.GONE);

        fragmentHistoryBinding.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            View radioButton = fragmentHistoryBinding.radioGroup.findViewById(checkedId);
            int index = fragmentHistoryBinding.radioGroup.indexOfChild(radioButton);

            switch (index)
            {
                case 0:
                    fragmentHistoryBinding.linearLayoutFromTo.setVisibility(View.GONE);
                    preApiHit(0);
                    break;
                case 1:
                    fragmentHistoryBinding.linearLayoutFromTo.setVisibility(View.VISIBLE);
                    preApiHit(1);

                    break;
            }

        });

        fragmentHistoryBinding.radioButtonAll.setChecked(true);
    }



    public void datePicker(View view1)
    {
        DatePickerDialog.OnDateSetListener onDateSetListener= (view2, year, monthOfYear, dayOfMonth) ->
        {
            int month=monthOfYear+1;
            ((EditText) view1).setText(dayOfMonth + "-" + month + "-" + year);
        };

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        DatePickerDialog dialog = new DatePickerDialog(context,
                AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, onDateSetListener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    void preApiHit(int i)
    {
        if (!CommonMethods.getInstance().isInternetAvailable())
        {
//            CommonMethods.getInstance().showSnackbar(view, context, context.getString(R.string.internet_not_available));
        }
        else
        {
            if (i == 0)
            {
                getHistoryApiHit( "userId=" + userId + "&fromDate=&toDate=");
            }
        }
    }

    public void onSearch(String fromDate,String toDate)
    {
        if (fromDate.isEmpty())
        {
            CommonMethods.getInstance().showSnackbar(view,context,getString(R.string.from_date_validation));
        }
        else if(toDate.isEmpty())
        {
            CommonMethods.getInstance().showSnackbar(view,context,getString(R.string.to_date_validation));
        }
        else
        {
            getHistoryApiHit( "userId=" + userId + "&fromDate="+fromDate+"&toDate="+toDate);
        }
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu)
    {
        for (int i = 0; i < menu.size(); i++)
        {
            MenuItem item = menu.getItem(i);
            item.setVisible(false);
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


    void getHistoryApiHit(String json)
    {
        CommonDialogs.getInstance().showProgressDialog(context);

        CommonMethods.getInstance().postDataWithAuth(context, MyConstants.GET_TICKET_HISTORY, json, new Callback()
        {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e)
            {
                Log.e(TAG, "onFailure  " + e.toString());
                CommonDialogs.getInstance().dismissDialog();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException
            {
                CommonDialogs.getInstance().dismissDialog();

                if (response.isSuccessful())
                {
                    String responseStr = response.body().string();
                    Log.e(TAG, responseStr);

                    HistoryModel historyModel = CommonMethods.getInstance().convertStringToBean(responseStr);
                    historyFragmentAdapter.addData(historyModel.getListReportTicketCollectionModel());
                    ((MainActivity) context).runOnUiThread(() -> historyFragmentAdapter.notifyDataSetChanged());

                }
                else
                {
                    String errorStr = response.body().string();
//                    JsonObject jsonObject = CommonMethods.getInstance().convertStringToJson(errorStr);
//                    CommonMethods.getInstance().showSnackbar(view, context, jsonObject.get(MyConstants.ERROR_DESCRIPTION).getAsString());
                }
            }
        });
    }

}
