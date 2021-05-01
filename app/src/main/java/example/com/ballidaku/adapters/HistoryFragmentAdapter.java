package example.com.ballidaku.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import example.com.ballidaku.R;
import example.com.ballidaku.commonClasses.CommonMethods;
import example.com.ballidaku.commonClasses.HistoryModel;

public class HistoryFragmentAdapter extends RecyclerView.Adapter<HistoryFragmentAdapter.ItemViewHolder>
{

    String TAG = HistoryFragmentAdapter.class.getSimpleName();
    private List<HistoryModel.ListReportTicketCollectionModel> arrayList;
    private Context context;

    public HistoryFragmentAdapter(Context context, List<HistoryModel.ListReportTicketCollectionModel> arrayList)
    {
        this.arrayList = arrayList;
        this.context = context;
    }


    class ItemViewHolder extends RecyclerView.ViewHolder
    {
        public android.view.View View;
        private final TextView textViewDate;
        private final TextView textViewTicketCount;
        private final TextView textViewTotalAmount;

        ItemViewHolder(android.view.View itemView)
        {
            super(itemView);
            View = itemView;
            textViewDate = View.findViewById(R.id.textViewDate);
            textViewTicketCount = View.findViewById(R.id.textViewTicketCount);
            textViewTotalAmount = View.findViewById(R.id.textViewTotalAmount);
        }
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.inflater_history_fragment_item, parent, false);
        return new ItemViewHolder(view);

    }

    @Override
    @SuppressLint("RecyclerView")
    public void onBindViewHolder(@NonNull ItemViewHolder holder, final int position)
    {

        holder.textViewTicketCount.setText(""+arrayList.get(position).getTotalTicket());
        holder.textViewTotalAmount.setText("Rs "+arrayList.get(position).getTotalAmount());
        holder.textViewDate.setText(CommonMethods.getInstance().getFormattedDateTime(arrayList.get(position).getCreatedDate()));


    }

    public void addData(List<HistoryModel.ListReportTicketCollectionModel> arrayList)
    {
        this.arrayList.clear();
        this.arrayList = arrayList;
    }

    @Override
    public int getItemCount()
    {
        return arrayList.size();
    }


}