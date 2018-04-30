package com.stxr.clockin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stxr.clockin.R;
import com.stxr.clockin.entity.MyUser;
import com.stxr.clockin.entity.NoteForLeave;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by stxr on 2018/4/30.
 */

public class LeaveShowAdapter extends RecyclerView.Adapter<LeaveShowAdapter.ViewHolder> {
    private List<NoteForLeave> leaveList;
    private Context context;

    public LeaveShowAdapter(Context context,List<NoteForLeave> leaveList) {
        this.leaveList = leaveList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leave, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        NoteForLeave note = leaveList.get(position);
        holder.tv_time.setText(String.format("%s   到   %s", note.getStartTime(), note.getEndTime()));
        holder.tv_reason.setText(note.getReason());
        BmobQuery<MyUser> query = new BmobQuery<>();
        query.getObject(note.getUser().getObjectId(), new QueryListener<MyUser>() {
            @Override
            public void done(MyUser myUser, BmobException e) {
                if (e == null) {
                    holder.tv_name.setText(myUser.getUsername());
                } else {
                    holder.tv_name.setText("error");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return leaveList.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_time)
        TextView tv_time;
        @BindView(R.id.tv_reason)
        TextView tv_reason;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
