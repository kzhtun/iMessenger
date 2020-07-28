package com.info121.imessenger.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.info121.imessenger.R;
import com.info121.imessenger.models.MessageDetails;
import com.info121.imessenger.utils.Util;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.inflationx.calligraphy3.CalligraphyTypefaceSpan;
import io.github.inflationx.calligraphy3.CalligraphyUtils;
import io.github.inflationx.calligraphy3.TypefaceUtils;

public class MessageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private int lastPosition = -1;
    Context mContext;
    List<MessageDetails> mMessageList;
    final int sdk = android.os.Build.VERSION.SDK_INT;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_NO_DATA = 2;


    public void updateMessageList(List<MessageDetails> messageList) {
        this.mMessageList.clear();
        this.mMessageList.addAll(messageList);
        notifyDataSetChanged();
    }

    public MessageListAdapter(Context mContext, List<MessageDetails> messageList) {
        this.mContext = mContext;
        this.mMessageList = messageList;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);

        if (viewType == TYPE_NO_DATA) {
            View view = inflater.inflate(R.layout.cell_no_data, parent, false);
            return new NoDataViewHolder(view);
        }

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
        NoDataViewHolder noDataViewHolder;

        if (holder instanceof NoDataViewHolder) {
            noDataViewHolder = (NoDataViewHolder) holder;
            noDataViewHolder.message.setText("No data for this day");
        }

        if (holder instanceof DateViewHolder) {
            dateHolder = (DateViewHolder) holder;
            dateHolder.date.setText(mMessageList.get(i).getMsgDate());
        }

        if (holder instanceof MessageViewHolder) {
            messageHolder = (MessageViewHolder) holder;

            if (mMessageList.get(i).getMsgStatus().equalsIgnoreCase("NEW")) {
                //setBackgroundDrawable(messageHolder.mainLayout, R.drawable.rounded_layout_new);

                //  messageHolder.message.setTextAppearance(mContext, R.style.message_text_bold);
                setTextColor(messageHolder.message, R.color.dark_cyan);
                setTextStyle(messageHolder.message, mMessageList.get(i).getMessages(), true);

                //  messageHolder.message.setTypeface(messageHolder.message.getTypeface(), Typeface.BOLD);
            } else {

                // setBackgroundDrawable(messageHolder.mainLayout, R.drawable.rounded_layout);
                //   messageHolder.message.setTextAppearance(mContext, R.style.message_text);
                setTextColor(messageHolder.message, R.color.monsoon);
                setTextStyle(messageHolder.message, mMessageList.get(i).getMessages(), false);
                //  messageHolder.message.setTypeface(messageHolder.message.getTypeface(), Typeface.NORMAL);
            }


            // messageHolder.message.setText(mMessageList.get(i).getMessages());
            messageHolder.sender.setText(mMessageList.get(i).getSender());
            //   messageHolder.sender.setText(i + "");
            Date date = Util.convertDateStringToDate(mMessageList.get(i).getMsgDate(), "dd/MM/yyyy hh:mm:ss a");
            messageHolder.dateTime.setText(Util.convertDateToString(date, "hh:mm a"));
        }


        //  setAnimation(holder.itemView, i);

    }


    private void setTextStyle(TextView textView, String message, Boolean bold) {

        SpannableStringBuilder sBuilder = new SpannableStringBuilder();
        sBuilder.append(message);

// Create the Typeface you want to apply to certain text
        CalligraphyTypefaceSpan typefaceBold = new CalligraphyTypefaceSpan(TypefaceUtils.load(mContext.getAssets(), "fonts/Lato-Bold.ttf"));
        CalligraphyTypefaceSpan typefaceNormal = new CalligraphyTypefaceSpan(TypefaceUtils.load(mContext.getAssets(), "fonts/Lato-Regular.ttf"));
        // Apply typeface to the Spannable 0 - 6 "Hello!" This can of course by dynamic.
        if (bold)
            sBuilder.setSpan(typefaceBold, 0, message.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        else
            sBuilder.setSpan(typefaceNormal, 0, message.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(sBuilder, TextView.BufferType.SPANNABLE);
    }

    private void setTextColor(TextView textView, int color) {
        if (sdk < 23)
            textView.setTextColor(mContext.getResources().getColor(color));
        else
            textView.setTextColor(ContextCompat.getColor(mContext, color));
    }

    private void setBackgroundDrawable(CardView layout, int id) {

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
        else {
            if (mMessageList.get(i).getMessageID().equals("0"))
                return TYPE_NO_DATA;
            else
                return TYPE_ITEM;
        }
//
//            if (mMessageList.get(i).getMessageID().equals("0"))
//                return TYPE_NO_DATA;
//            else


    }


    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {

            lastPosition = position;
            viewToAnimate.setTranslationY(Util.getScreenHeight(mContext));
            viewToAnimate.animate()
                    .translationY(0)
                    .setInterpolator(new DecelerateInterpolator(3.f))
                    .setDuration(1000)
                    .start();


            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    public class NoDataViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.message)
        TextView message;

        public NoDataViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
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

//        @BindView(R.id.main_layout)
//        LinearLayout mainLayout;

        @BindView(R.id.main_layout)
        CardView mainLayout;

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
