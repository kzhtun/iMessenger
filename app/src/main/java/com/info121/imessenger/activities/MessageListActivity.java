package com.info121.imessenger.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    @BindView(R.id.root_view)
    CoordinatorLayout rootView;

    Boolean firstLoad = true;

    final Handler handler = new Handler();

    Calendar c = Calendar.getInstance();
    Date today;
    Date yesterday;
    SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        ButterKnife.bind(this);
        mContext = MessageListActivity.this;

        Date currentTime = Calendar.getInstance().getTime();

        today = c.getTime();

        welcome.setText("Welcome " + App.UserName);
        lastLogin.setText("Last Login : " + Util.convertDateToString(currentTime, "dd/MM/yyyy hh:mm a"));

        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        messageListAdapter = new MessageListAdapter(mContext, mMessageList);
        mRecyclerView.setAdapter(messageListAdapter);

        callGetUserMessagesDate(df.format(today));

        pullToRefresh.setOnRefreshListener(() -> {
            firstLoad = true;
            c = Calendar.getInstance();
            mMessageList.clear();
            callGetUserMessagesDate(df.format(today));
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                item_start = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                item_end = linearLayoutManager.findLastCompletelyVisibleItemPosition();

                if (item_start == -1 || item_end == -1) return;

                Log.e("Item End ", item_end + " : " + mMessageList.size());

                if (item_end == mMessageList.size() - 1) {
                    c.add(Calendar.DATE, -1);

//                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a", Locale.getDefault());
//                    String d = df.format(c.getTime());

                    Log.e("Call Get User Message ", "");
                    callGetUserMessagesDate(df.format(c.getTime()));
                }

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

        newMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // MessageListActivity.this.runOnUiThread(() -> callGetUpdateMessageToday());



//                Snackbar.make(getWindow().getDecorView().getRootView(), "Text label", Snackbar.LENGTH_LONG)
//                        .setAction("View", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//                            }
//                        })
//                        .setBackgroundTint(ContextCompat.getColor(mContext, R.color.colorPrimary))
//                        .setActionTextColor(ContextCompat.getColor(mContext, R.color.white))
//                        .show();
            }
        });

    }

    private void showNewMessageSnackBar(){
        Snackbar snackbar = Snackbar.make(rootView, "You have new message", Snackbar.LENGTH_LONG);
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)
                snackbarLayout.getLayoutParams();
        layoutParams.setMargins(32, 32, 32, 32);

        snackbarLayout.setLayoutParams(layoutParams);

        snackbar.setBackgroundTint(ContextCompat.getColor(mContext, R.color.colorPrimary));
        snackbar.setActionTextColor(ContextCompat.getColor(mContext, R.color.white));
        snackbar.setAction("SHOW" , v1 -> {
            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    mRecyclerView.smoothScrollToPosition(0);
                }
            });
        });
        snackbar.show();
    }

    private void callGetUpdateMessageToday() {
        pullToRefresh.setRefreshing(true);

        Call<ObjectRes> call = RestClient.CHAT().getApiService().GetUserMessagesDate(
                App.MobileNo,
                df.format(today),
                App.SecretKey,
                App.MobileKey
        );

        call.enqueue(new Callback<ObjectRes>() {
            @Override
            public void onResponse(Call<ObjectRes> call, Response<ObjectRes> response) {
                if (response.body().getToken() == null) return;
                if (response.body().getToken().length() > 0) {

                    // matching the date format with response json
                    SimpleDateFormat sdf = new SimpleDateFormat("M/dd/yyyy", Locale.getDefault());
                    String strToday = sdf.format(today);

                    List<MessageDetails> tmpMessages = new ArrayList<>();

                    // list all today messages in list first
                    for (int i = 0; i < mMessageList.size(); i++) {
                        if (mMessageList.get(i).getMsgDate().indexOf(strToday) == 0)
                            tmpMessages.add(mMessageList.get(i));
                        else
                            break;
                    }

                    // delete today messages in list
                    for (int i = 0; i < tmpMessages.size(); i++) {
                        mMessageList.remove(tmpMessages.get(i));
                    }

                    // re-arrange the list sequence
                    tmpMessages.clear();
                    tmpMessages.addAll(groupByDate(response.body().getMessageDetails()));
                    tmpMessages.addAll(mMessageList);

                    // replace to original list
                    mMessageList.clear();
                    mMessageList.addAll(tmpMessages);

                    messageListAdapter.notifyDataSetChanged();
                    pullToRefresh.setRefreshing(false);

                    Log.e("Update Today Messages", "Success");
                }
            }

            @Override
            public void onFailure(Call<ObjectRes> call, Throwable t) {
                Log.e("Update Today Messages", "Failed");
            }
        });

    }

    private List<MessageDetails> replaceTodayMessages(List<MessageDetails> messageList) {
        List<MessageDetails> messages = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        String strToday = sdf.format(today);

        for (int i = 0; i < messageList.size(); i++) {
            if (messageList.get(i).getMsgDate().indexOf(strToday) > 0)
                messageList.remove(messageList.get(i));
        }

        return messages;
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
                handler.postDelayed(() -> MessageListActivity.this.runOnUiThread(() -> callGetUpdateMessageToday()), 2000);

                //   List<MessageDetails> updateMessage = getUpdateMessageToday(mMessageList);

            }

            @Override
            public void onFailure(Call<ObjectRes> call, Throwable t) {
                Log.e("Update Status : ", "Failed");
            }
        });
    }

    private void callGetUserMessagesDate(String messageDate) {
        pullToRefresh.setRefreshing(true);

        Call<ObjectRes> call = RestClient.CHAT().getApiService().GetUserMessagesDate(
                App.MobileNo,
                messageDate,
                App.SecretKey,
                App.MobileKey
        );

        call.enqueue(new Callback<ObjectRes>() {
            @Override
            public void onResponse(Call<ObjectRes> call, Response<ObjectRes> response) {

                if (response.body().getToken() == null) return;

                if (response.body().getToken().length() > 0) {
                    if (firstLoad) {
                        mRecyclerView.scheduleLayoutAnimation();
                        firstLoad = false;

                        // load yesterday
                        c.add(Calendar.DATE, -1);
                        callGetUserMessagesDate(df.format(c.getTime()));

                        Toast.makeText(mContext, "Scroll up to load more ...", Toast.LENGTH_LONG).show();
                    }

                    mMessageList.addAll(groupByDate(response.body().getMessageDetails()));
                    messageListAdapter.notifyDataSetChanged();

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

//    private void callGetUserMessages() {
//        pullToRefresh.setRefreshing(true);
//
//        Call<ObjectRes> call = RestClient.CHAT().getApiService().GetUserMessages(
//                App.MobileNo,
//                App.SecretKey,
//                App.MobileKey
//        );
//
//        call.enqueue(new Callback<ObjectRes>() {
//            @Override
//            public void onResponse(Call<ObjectRes> call, Response<ObjectRes> response) {
//
//                if (response.body().getToken() == null) return;
//
//                if (response.body().getToken().length() > 0) {
//                    mMessageList.clear();
//
//                    mMessageList.addAll(groupByDate(response.body().getMessageDetails()));
//                    messageListAdapter.notifyDataSetChanged();
//
//
//                    pullToRefresh.setRefreshing(false);
//
//                    if (firstLoad) {
//                        mRecyclerView.scheduleLayoutAnimation();
//                        firstLoad = false;
//                    }
//
//                    Log.e("Get User Messages : ", "Success");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ObjectRes> call, Throwable t) {
//                pullToRefresh.setRefreshing(false);
//                Log.e("Get User Messages : ", "Failed");
//            }
//        });
//    }

    private List<MessageDetails> groupByDate(List<MessageDetails> rawList) {

        newCount = 0;

        List<MessageDetails> tmpList = new ArrayList<>();

        try {
            String[] dt1 = rawList.get(0).getMsgDate().split(" ");
            String[] dt2 = rawList.get(0).getMsgDate().split(" ");

            tmpList.add(new MessageDetails(dt1[0], "HEADER", ""));
            tmpList.add(rawList.get(0));

            for (int i = 1; i < rawList.size(); i++) {
                dt1 = rawList.get(i - 1).getMsgDate().split(" ");
                dt2 = rawList.get(i).getMsgDate().split(" ");

                if (!dt1[0].equalsIgnoreCase(dt2[0])) {
                    tmpList.add(new MessageDetails(dt2[0], "HEADER", ""));
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
            MessageListActivity.this.runOnUiThread(() -> callGetUpdateMessageToday());
            showNewMessageSnackBar();
        }
    }

}