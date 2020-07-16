package com.info121.imessenger.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        ButterKnife.bind(this);
        mContext = MessageListActivity.this;

        welcome.setText("Welcome " + App.UserName);

        callGetUserMessages();

        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        messageListAdapter = new MessageListAdapter(mContext, mMessageList);
        mRecyclerView.setAdapter(messageListAdapter);

        pullToRefresh.setOnRefreshListener(() -> callGetUserMessages());

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                //  position = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                position = linearLayoutManager.findFirstCompletelyVisibleItemPosition();

                if (mMessageList.get(position).getMsgStatus().equalsIgnoreCase("HEADER"))
                    position++;

                Log.e("New State : ", position + "");
                if (mMessageList.get(position).getMsgStatus().equalsIgnoreCase("NEW")) {
                    //  if (mMessageList.get(position).getMessageID().equals("194"))
                    callUpdateMessageStatus(mMessageList.get(position).getMessageID());

                    try {
                        callUpdateMessageStatus(mMessageList.get(position - 1).getMessageID());
                        callUpdateMessageStatus(mMessageList.get(position + 1).getMessageID());
                    } catch (Exception e) {
                        Log.e("New State : ", position + 1 + "");
                        Log.e("New State : ", position - 1 + "");
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
                if (response.body().getToken().length() > 0) {
                    mMessageList = groupByDate(response.body().getMessageDetails());
                    messageListAdapter = new MessageListAdapter(mContext, mMessageList);
                    mRecyclerView.setAdapter(messageListAdapter);

                    pullToRefresh.setRefreshing(false);

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

        if (newCount == 0)
            newMessage.setText("You have " + newCount + " new messages");
        else
            newMessage.setText("You have no new message");

        return tmpList;
    }
}