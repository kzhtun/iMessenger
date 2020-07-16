package com.info121.imessenger.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.info121.imessenger.R;
import com.info121.imessenger.models.MessageDetails;
import com.info121.imessenger.utils.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    List<MessageDetails> mMessageList;
    final int sdk = android.os.Build.VERSION.SDK_INT;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public MessageListAdapter(Context mContext, List<MessageDetails> mMessageList) {
        this.mContext = mContext;
        this.mMessageList = mMessageList;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);

        if (viewType == TYPE_HEADER) {
            View view = inflater.inflate(R.layout.cell_date, parent, false);
            return new DateViewHolder(view);
        }

        if (viewType == TYPE_ITEM) {
            View view = inflater.inflate(R.layout.cell_message, parent, false);
            return new MessageViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {

        MessageViewHolder messageHolder;
        DateViewHolder dateHolder;

        if (holder instanceof DateViewHolder) {
            dateHolder = (DateViewHolder) holder;

            dateHolder.date.setText(mMessageList.get(i).getMsgDate());
        }

        if (holder instanceof MessageViewHolder) {
            messageHolder = (MessageViewHolder) holder;

            if (mMessageList.get(i).getMsgStatus().equalsIgnoreCase("NEW")) {
             //   setBackgroundDrawable(messageHolder.mainLayout, R.drawable.rounded_layout_new);
                setTextColor(messageHolder.message, R.color.new_text);
            } else {
            //    setBackgroundDrawable(messageHolder.mainLayout, R.drawable.rounded_layout);
                setTextColor(messageHolder.message, R.color.monsoon);
            }

            messageHolder.message.setText(mMessageList.get(i).getMessages());
            messageHolder.sender.setText(mMessageList.get(i).getSender());
            messageHolder.dateTime.setText(mMessageList.get(i).getMsgDate());
        }

    }

    private void setTextColor(TextView textView, int color) {
        if (sdk < 23)
            textView.setTextColor(mContext.getResources().getColor(color));
        else
            textView.setTextColor(ContextCompat.getColor(mContext, color));
    }

    private void setBackgroundDrawable(LinearLayout layout, int id) {

        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            layout.setBackgroundDrawable(ContextCompat.getDrawable(mContext, id));
        } else {
            layout.setBackground(ContextCompat.getDrawable(mContext, id));
        }

//        layout.setPadding((int) Util.convertDpToPixel( 16, mContext),
//                (int)  Util.convertDpToPixel(16, mContext),
//                (int)  Util.convertDpToPixel(16, mContext),
//                (int) Util.convertDpToPixel(16, mContext));
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    @Override
    public int getItemViewType(int i) {
        if (mMessageList.get(i).getMsgStatus().equalsIgnoreCase("HEADER"))
            return TYPE_HEADER;
        else
            return TYPE_ITEM;
    }

    public class DateViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.date)
        TextView date;

        public DateViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.main_layout)
        LinearLayout mainLayout;

        @BindView(R.id.sender)
        TextView sender;

        @BindView(R.id.message)
        TextView message;

        @BindView(R.id.date_time)
        TextView dateTime;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
