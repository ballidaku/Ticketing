package example.com.ballidaku.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import example.com.ballidaku.R;
import example.com.ballidaku.commonClasses.CommonDialogs;
import example.com.ballidaku.commonClasses.CommonInterfaces;
import example.com.ballidaku.commonClasses.CommonMethods;
import example.com.ballidaku.commonClasses.MyConstants;
import example.com.ballidaku.commonClasses.MySharedPreference;
import example.com.ballidaku.mainSceens.MainActivity;


/**
 * Created by sharanpalsingh on 19/02/18.
 */

public class MainFragmentAdapter<T> extends RecyclerView.Adapter<MainFragmentAdapter.ItemViewHolder>
{

    String TAG = MainFragmentAdapter.class.getSimpleName();
    private ArrayList<T> arrayList;
    private Context context;

    public MainFragmentAdapter(Context context, ArrayList<T> arrayList)
    {
        this.arrayList = arrayList;
        this.context = context;
    }



    class ItemViewHolder<T> extends RecyclerView.ViewHolder
    {
        public android.view.View View;
        private final TextView textViewName;

        ItemViewHolder(android.view.View itemView)
        {
            super(itemView);
            View = itemView;
            textViewName = View.findViewById(R.id.textViewName);
        }
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.inflater_main_fragment_item, parent, false);
        return new ItemViewHolder(view);

    }

    @Override
    @SuppressLint("RecyclerView")
    public void onBindViewHolder(@NonNull ItemViewHolder holder, final int position)
    {


        holder.textViewName.setText((String) arrayList.get(position));


        holder.itemView.setOnClickListener(v ->
        {

           String data = MySharedPreference.getInstance().getUserData(context,MyConstants.ZONE_PASSWORD);
           JsonArray  jsonArray=CommonMethods.getInstance().convertStringToJson(data).get(MyConstants.RANGEZONEBASICMODEL).getAsJsonArray();



            for (int i = 0; i <jsonArray.size() ; i++)
            {
                JsonObject jsonObject=jsonArray.get(i).getAsJsonObject();

                if(position==i)
                {
                    Log.e(TAG,"PassCode "+ jsonObject.get(MyConstants.PASSCODE).getAsInt());
                    checkPasscode(jsonObject.get(MyConstants.PASSCODE).getAsInt(),position+1,jsonObject.get(MyConstants.RANGE_ZONE_ID).getAsString());
                }
            }

//

        });


    }

    private void checkPasscode(int passcode, int position,String rangeZoneID)
    {
        CommonDialogs.getInstance().showPasscodeDialog(context, passcode, new CommonInterfaces()
        {
            @Override
            public void onSuccess()
            {
                ((MainActivity)context).changeFragment(position,rangeZoneID, true);
            }

            @Override
            public void onFailure()
            {
                CommonMethods.getInstance().showToast(context, context.getString(R.string.passcode_mismatch));
            }
        });
    }


    public void addData(ArrayList<T> arrayList)
    {
        this.arrayList.clear();
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount()
    {
        return arrayList.size();
    }


}