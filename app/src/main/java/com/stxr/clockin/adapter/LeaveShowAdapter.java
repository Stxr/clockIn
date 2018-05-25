package com.stxr.clockin.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.stxr.clockin.R;
import com.stxr.clockin.entity.MyUser;
import com.stxr.clockin.entity.NoteForLeave;
import com.stxr.clockin.utils.ToastUtil;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

import static com.stxr.clockin.R2.id.photo;

/**
 * Created by stxr on 2018/4/30.
 */

public class LeaveShowAdapter extends RecyclerView.Adapter<LeaveShowAdapter.ViewHolder> {
    private List<NoteForLeave> leaveList;
    private Context context;
    private OnItemClickListener listener;
    private OnItemLongClickListener longClickListener;
    public LeaveShowAdapter(Context context,List<NoteForLeave> leaveList) {
        this.leaveList = leaveList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leave, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final NoteForLeave note = leaveList.get(position);
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
        //判断审核状态
        if (note.getAdmit() == null) {
            holder.iv_judge.setImageResource(R.drawable.ic_judging);
            holder.note_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(note,holder.iv_judge);
                }
            });
        } else {
            //只有审批完成的才可以删除
            holder.note_layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    longClickListener.onClick(v,note,position);
                    return true;
                }
            });
            if (note.getAdmit()) {
                holder.iv_judge.setImageResource(R.drawable.ic_pass);
            } else {
                holder.iv_judge.setImageResource(R.drawable.ic_failed);
            }
        }

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
        @BindView(R.id.iv_judge)
        ImageView iv_judge;
        @BindView(R.id.note_layout)
        CardView note_layout;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void removeItem(final NoteForLeave noteForLeave, final int position) {
        noteForLeave.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    ToastUtil.show(context, "删除成功");
                    leaveList.remove(noteForLeave);
                    notifyItemRemoved(position);
                    notifyDataSetChanged();
                } else {
                    ToastUtil.show(context, "删除失败"+e.getMessage());
                }
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longClickListener = listener;
    }

    public interface OnItemLongClickListener{
        void onClick (View v,NoteForLeave noteForLeave,int position);
    }

    public interface OnItemClickListener{
        void onClick (NoteForLeave noteForLeave,ImageView photo);
    }
}
