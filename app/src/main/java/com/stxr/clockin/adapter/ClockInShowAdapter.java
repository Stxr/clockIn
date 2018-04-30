package com.stxr.clockin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.stxr.clockin.R;
import com.stxr.clockin.entity.ClockIn;
import com.stxr.clockin.utils.ClockInUtil;

import org.w3c.dom.Text;

import java.time.Clock;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by stxr on 2018/4/30.
 */

public class ClockInShowAdapter extends RecyclerView.Adapter<ClockInShowAdapter.ViewHolder> {
    private List<ClockIn> clockIns;
    private final Context context;
    public ClockInShowAdapter(Context context, List<ClockIn> clockIns) {
        this.clockIns = clockIns;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_clcok, parent, false);
        return new  ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ClockIn clockIn = clockIns.get(position);
        //加载到ui
        ClockInUtil.showOnUi(context, clockIn, holder.tv_name, holder.tv_time, holder.imageView);
    }

    @Override
    public int getItemCount() {
        return clockIns.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_reason)
        ImageView imageView;
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_time)
        TextView tv_time;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
