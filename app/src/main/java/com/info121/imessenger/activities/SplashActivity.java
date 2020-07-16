package com.info121.imessenger.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.emoji.widget.EmojiEditText;
import androidx.emoji.widget.EmojiTextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.info121.imessenger.App;
import com.info121.imessenger.BaseActivity;
import com.info121.imessenger.R;
import com.info121.imessenger.api.RestClient;
import com.info121.imessenger.models.ObjectRes;
import com.info121.imessenger.models.ProfileDetails;
import com.info121.imessenger.utils.PrefDB;
import com.info121.imessenger.utils.Util;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends BaseActivity {
    String TAG = SplashActivity.this.getClass().getSimpleName();
    Context mContext = SplashActivity.this;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    PrefDB prefDB;
//    @BindView(R.id.emoji_text_view)
//    EmojiTextView emojiTextView;
//    @BindView(R.id.emoji_edit_text)
//    EmojiEditText emojiEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        prefDB = new PrefDB(mContext);

        App.UserName = prefDB.getString("USER_NAME");
        App.SecretKey = Util.getSecretKey(mContext);
        App.MobileKey = Util.convertToSpecial(mContext);

        Log.e("Device ID : ", App.DeviceID);
        Log.e("Secret Key : ", App.SecretKey);
        Log.e("Mobile Key : ", App.MobileKey);


        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "getInstanceId failed", task.getException());
                        return;
                    }

                    // Get new Instance ID token
                    App.FCM_TOKEN = task.getResult().getToken();
                    Log.e("NEW FCM Token: ", App.FCM_TOKEN);

                    callValidateUser();
                });


//        emojiEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                emojiTextView.setText(s);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
    }


    private void callValidateUser() {
        Call<ObjectRes> call = RestClient.CHAT().getApiService().ValidateUser(
                App.DeviceID,
                App.FCM_TOKEN,
                App.SecretKey,
                App.MobileKey
        );

        call.enqueue(new Callback<ObjectRes>() {
            @Override
            public void onResponse(Call<ObjectRes> call, Response<ObjectRes> response) {
                Log.e("Validate User : ", "Success");

                if (response.body().getResponsemessage().equalsIgnoreCase("Invalid")) {
                    // NOT register yet
                    startActivity(new Intent(mContext, RegistrationActivity.class));
                } else {
                    // exist
                    App.AuthToken = response.body().getToken();
                    RestClient.Refresh();
                    callGetUserHP();
                }
            }

            @Override
            public void onFailure(Call<ObjectRes> call, Throwable t) {
                Log.e("Validate User : ", "Failed");
            }
        });
    }

    private void callGetUserHP() {
        Call<ObjectRes> call = RestClient.CHAT().getApiService().GetUserHP(
                App.DeviceID,
                App.SecretKey,
                App.MobileKey
        );

        call.enqueue(new Callback<ObjectRes>() {
            @Override
            public void onResponse(Call<ObjectRes> call, Response<ObjectRes> response) {
                Log.e("Get User HP : ", "Success");
                App.MobileNo = response.body().getUserhp().replace("+65", "");

                //startActivity(new Intent(mContext, ProfileActivity.class));
                callGetUserProfile();
            }

            @Override
            public void onFailure(Call<ObjectRes> call, Throwable t) {
                Log.e("Get User HP : ", "Failed");
            }
        });
    }

    private void callGetUserProfile() {
        Call<ObjectRes> call = RestClient.CHAT().getApiService().GetUserProfile(
                App.MobileNo,
                App.SecretKey,
                App.MobileKey
        );

        call.enqueue(new Callback<ObjectRes>() {
            @Override
            public void onResponse(Call<ObjectRes> call, Response<ObjectRes> response) {
                Log.e("Get User Profile : ", "Success");
                displayUserInfo(response.body().getProfileDetails());

                startActivity(new Intent(SplashActivity.this, MessageListActivity.class));
                finish();
            }

            @Override
            public void onFailure(Call<ObjectRes> call, Throwable t) {
                Log.e("Get User Profile : ", "Failed");
            }
        });
    }

    private void displayUserInfo(List<ProfileDetails> profileDetails) {
        for (ProfileDetails p : profileDetails) {
            if (p.getDeviceID().equals(App.DeviceID)) {
                App.UserName = p.getProfileName();
            }
        }
    }

    @Subscribe
    public void onEvent(String action) {
        if (action.equalsIgnoreCase("VALIDATEUSER")) {
            callValidateUser();
        }
    }
}