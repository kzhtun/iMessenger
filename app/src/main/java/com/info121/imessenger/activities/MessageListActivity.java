package com.info121.imessenger.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.info121.imessenger.App;
import com.info121.imessenger.BaseActivity;
import com.info121.imessenger.R;
import com.info121.imessenger.adapters.MessageListAdapter;
import com.info121.imessenger.api.RestClient;
import com.info121.imessenger.models.MessageDetails;
import com.info121.imessenger.models.ObjectRes;
import com.info121.imessenger.utils.Util;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageListActivity extends BaseActivity {
    Context mContext;

//    @BindView(R.id.toolbar)
//    Toolbar toolbar;

    @BindView(R.id.rv_messages)
    RecyclerView mRecyclerView;
    @BindView(R.id.pullToRefresh)
    SwipeRefreshLayout pullToRefresh;

    int newCount;
    int position;
    int item_start, item_end;

    List<MessageDetails> mMessageList = new ArrayList();
    MessageListAdapter messageListAdapter;
    @BindView(R.id.welcome)
    TextView welcome;
    @BindView(R.id.last_login)
    TextView lastLogin;
    @BindView(R.id.new_message)
    TextView newMessage;
    @BindView(R.id.profile_image)
    CircularImageView profileImage;

    Boolean firstLoad = true;

    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        ButterKnife.bind(this);
        mContext = MessageListActivity.this;

        Date currentTime = Calendar.getInstance().getTime();

        welcome.setText("Welcome " + App.UserName);
        lastLogin.setText("Last Login : " + Util.convertDateToString(currentTime, "dd/MM/yyyy hh:mm a"));

        callGetUserMessages();

        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        messageListAdapter = new MessageListAdapter(mContext, mMessageList);
        mRecyclerView.setAdapter(messageListAdapter);

        pullToRefresh.setOnRefreshListener(() -> {
            firstLoad = true;
            callGetUserMessages();
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                item_start = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                item_end = linearLayoutManager.findLastCompletelyVisibleItemPosition();

                if(item_start == -1 || item_end == -1) return;

                Log.e("Scroll at", position + "");

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Log.e("Idle at ", item_start + " to " + item_end);

                    for (int i = item_start; i <= item_end; i++) {
                        if (mMessageList.get(i).getMsgStatus().equalsIgnoreCase("NEW")) {
                               callUpdateMessageStatus(mMessageList.get(i).getMessageID());
                        }
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });


    }

    @OnClick(R.id.profile_image)
    public void profileImageOnClick() {
        startActivity(new Intent(mContext, ProfileActivity.class));
    }

    private void callUpdateMessageStatus(String messageid) {
        Call<ObjectRes> call = RestClient.CHAT().getApiService().UpdateMessageStatus(
                messageid,
                App.SecretKey,
                App.MobileKey
        );

        call.enqueue(new Callback<ObjectRes>() {
            @Override
            public void onResponse(Call<ObjectRes> call, Response<ObjectRes> response) {
                Log.e("Update Status : ", "Success");
                handler.postDelayed(() -> MessageListActivity.this.runOnUiThread(() -> callGetUserMessages()), 2000);
            }

            @Override
            public void onFailure(Call<ObjectRes> call, Throwable t) {
                Log.e("Update Status : ", "Failed");
            }
        });
    }

    private void callGetUserMessages() {
        pullToRefresh.setRefreshing(true);

        Call<ObjectRes> call = RestClient.CHAT().getApiService().GetUserMessages(
                App.MobileNo,
                App.SecretKey,
                App.MobileKey
        );

        call.enqueue(new Callback<ObjectRes>() {
            @Override
            public void onResponse(Call<ObjectRes> call, Response<ObjectRes> response) {

                if(response.body().getToken() == null) return;

                if ( response.body().getToken().length() > 0) {
                    mMessageList.clear();

                    mMessageList.addAll(groupByDate(response.body().getMessageDetails()));
                    messageListAdapter.notifyDataSetChanged();


                    pullToRefresh.setRefreshing(false);

                    if (firstLoad) {
                        mRecyclerView.scheduleLayoutAnimation();
                        firstLoad = false;
                    }

                    Log.e("Get User Messages : ", "Success");
                }
            }

            @Override
            public void onFailure(Call<ObjectRes> call, Throwable t) {
                pullToRefresh.setRefreshing(false);
                Log.e("Get User Messages : ", "Failed");
            }
        });
    }

    private List<MessageDetails> groupByDate(List<MessageDetails> rawList) {

        newCount = 0;

        List<MessageDetails> tmpList = new ArrayList<>();

        try {
            String[] dt1 = rawList.get(0).getMsgDate().split(" ");
            String[] dt2 = rawList.get(0).getMsgDate().split(" ");

            tmpList.add(new MessageDetails(dt1[0], "HEADER"));
            tmpList.add(rawList.get(0));

            for (int i = 1; i < rawList.size(); i++) {
                dt1 = rawList.get(i - 1).getMsgDate().split(" ");
                dt2 = rawList.get(i).getMsgDate().split(" ");

                if (!dt1[0].equalsIgnoreCase(dt2[0])) {
                    tmpList.add(new MessageDetails(dt2[0], "HEADER"));
                }

                tmpList.add(rawList.get(i));

                if (tmpList.get(i).getMsgStatus().equalsIgnoreCase("NEW")) {
                    newCount++;
                }
            }
        } catch (Exception e) {
        }


        if (newCount == 0)
            newMessage.setText("You have no new message");
        else
            newMessage.setText("You have " + newCount + " new messages");

        return tmpList;
    }

    @Subscribe
    public void onEvent(String action) {
        if (action.equalsIgnoreCase("REFRESH_MESSAGES")) {
            MessageListActivity.this.runOnUiThread(() -> callGetUserMessages());

        }
    }

}