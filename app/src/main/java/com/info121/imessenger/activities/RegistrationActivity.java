package com.info121.imessenger.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.info121.imessenger.App;
import com.info121.imessenger.BaseActivity;
import com.info121.imessenger.R;
import com.info121.imessenger.api.RestClient;
import com.info121.imessenger.models.ObjectRes;
import com.info121.imessenger.utils.Util;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends BaseActivity {

    @BindView(R.id.user_name)
    EditText userName;
    @BindView(R.id.phone_no)
    EditText phoneNo;
    @BindView(R.id.login_layout)
    LinearLayout loginLayout;
    @BindView(R.id.accept)
    CheckBox accept;
    @BindView(R.id.signin)
    Button signin;

    Context mContext = RegistrationActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);

        accept.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                signin.setBackgroundResource(R.drawable.rounded_button);
            } else {
                signin.setBackgroundResource(R.drawable.rounded_button_disable);
            }
        });

        signin.setOnClickListener(v -> {
            callRegisterUser();
        });

    }

    private void callRegisterUser() {
        Call<ObjectRes> call = RestClient.CHAT().getApiService().RegisterUser(
                userName.getText().toString(),
                phoneNo.getText().toString(),
                App.DeviceID,
                App.SecretKey,
                App.MobileKey
        );

        call.enqueue(new Callback<ObjectRes>() {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "",
                    Toast.LENGTH_SHORT);

            @Override
            public void onResponse(Call<ObjectRes> call, Response<ObjectRes> response) {
                toast.setText("User has been registered.");
                toast.show();

                finish();
                EventBus.getDefault().post("VALIDATEUSER");
                //startActivity(new Intent(mContext, SplashActivity.class)), 3000);

            }

            @Override
            public void onFailure(Call<ObjectRes> call, Throwable t) {
                toast.setText("There was an error while registering the user.");
                toast.show();
            }
        });
    }
}