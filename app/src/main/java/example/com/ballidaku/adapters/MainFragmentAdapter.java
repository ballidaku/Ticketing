package example.com.ballidaku.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import example.com.ballidaku.R;
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

            ((MainActivity)context).changeFragment(position+1);

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