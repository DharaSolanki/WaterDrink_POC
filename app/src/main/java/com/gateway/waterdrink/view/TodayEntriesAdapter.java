package com.gateway.waterdrink.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gateway.waterdrink.R;
import com.gateway.waterdrink.db.TodayEntry;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;



public class TodayEntriesAdapter extends RecyclerView.Adapter<TodayEntriesAdapter.TodayEntriesVH> {
    @NonNull
    private List<TodayEntry> entryList = new ArrayList<>();
   // Context mcontext;

  /*  public TodayEntriesAdapter(Context context,List<TodayEntry> list) {
        mcontext = context;
        entryList=list;
    }*/

    @NonNull
    @Override
    public TodayEntriesVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_today_entries, parent, false);
        return new TodayEntriesVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TodayEntriesVH holder, int position) {
        boolean isLast = (position == (entryList.size() - 1));
        holder.bindData(entryList.get(position), isLast);
    }

    @Override
    public int getItemCount() {
        return entryList.size();
    }

    public void setEntryList(@Nullable List<TodayEntry> entryList) {
        if(entryList==null)
        {
            entryList=new ArrayList<>();
        }

        this.entryList = entryList;
        notifyDataSetChanged();
    }


    class TodayEntriesVH extends RecyclerView.ViewHolder {
        TextView tvQtyTitle, tvTime;
        ImageView ivQty;

        TodayEntriesVH(@NonNull View itemView) {
            super(itemView);
            initViews(itemView);
        }

        private void initViews(@NonNull View v) {
            tvQtyTitle = v.findViewById(R.id.tv_amount);
            tvTime = v.findViewById(R.id.tv_time);
            ivQty = v.findViewById(R.id.iv_qty_icon);

        }

        //todo : use [isLast]
        void bindData(final TodayEntry data, final boolean isLast) {
            String ml = tvTime.getContext().getString(R.string.text_ml);
            tvQtyTitle.setText(String.valueOf(data.getAmountInMilliLitres())+" "+ml);
            tvTime.setText(String.format("at: %s", data.getTime()));
            ivQty.setImageResource(R.drawable.ic_200);

        }


    }
}
