package com.info121.imessenger.activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.info121.imessenger.App;
import com.info121.imessenger.BaseActivity;
import com.info121.imessenger.R;
import com.info121.imessenger.adapters.DeviceListAdapter;
import com.info121.imessenger.api.RestClient;
import com.info121.imessenger.models.ObjectRes;
import com.info121.imessenger.models.ProfileDetails;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends BaseActivity {
    Context mContext = ProfileActivity.this;

    @BindView(R.id.home)
    ImageView home;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.profile_layout)
    LinearLayout profileLayout;
    @BindView(R.id.name)
    TextView userName;

    @BindView(R.id.phone_no)
    TextView phoneNo;
    @BindView(R.id.list_device)
    ListView listDevice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ButterKnife.bind(this);

        profileLayout.bringToFront();

        callGetUserProfile();

    }


    private void callUnRegisterDevice(String DeviceID) {
        Call<ObjectRes> call = RestClient.CHAT().getApiService().UnRegisterDevice(
                DeviceID,
                App.SecretKey,
                App.MobileKey
        );

        call.enqueue(new Callback<ObjectRes>() {
            @Override
            public void onResponse(Call<ObjectRes> call, Response<ObjectRes> response) {
                Log.e("UnRegister Device : ", "Success");

                if (DeviceID.equals(App.DeviceID)) {
                    System.exit(1);
                }else {
                    Toast.makeText(mContext, "Device is successfully remove from the list", Toast.LENGTH_SHORT).show();
                    callGetUserProfile();
                }
            }

            @Override
            public void onFailure(Call<ObjectRes> call, Throwable t) {
                Log.e("UnRegister Device : ", "Failed");
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
                // displayUserInfo(response.body().getProfileDetails());
                displayUserInfo(response.body().getProfileDetails());

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
                userName.setText(p.getProfileName());
                phoneNo.setText(p.gethPNo());
            }
        }

        DeviceListAdapter deviceListAdapter = new DeviceListAdapter(profileDetails, mContext);
        listDevice.setAdapter(deviceListAdapter);
        setListViewHeightBasedOnItems(listDevice);

    }

    @OnClick(R.id.home)
    public void homeOnClick() {
        finish();
    }

    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }

    }

    @Subscribe
    public void onEvent(String deviceID) {
        callUnRegisterDevice(deviceID);
    }

}