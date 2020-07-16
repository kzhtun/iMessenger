package com.info121.imessenger.adapters;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.appcompat.app.AlertDialog;

import com.info121.imessenger.App;
import com.info121.imessenger.R;
import com.info121.imessenger.models.ProfileDetails;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class DeviceListAdapter extends BaseAdapter {
    List<ProfileDetails> devices = new ArrayList<>();
    Context mContext;


    public DeviceListAdapter(List<ProfileDetails> devices, Context context) {
        this.devices = devices;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return devices.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(mContext).inflate(R.layout.cell_device, null);

        TextView user = view.findViewById(R.id.user);
        TextView deviceId = view.findViewById(R.id.device_id);
        ImageView delete = view.findViewById(R.id.delete);
        LinearLayout deleteLayout =  view.findViewById(R.id.delete_layout);

        user.setText(devices.get(i).getProfileName());
        deviceId.setText(devices.get(i).getDeviceID());

        delete.setOnClickListener(view1 -> {
            showConfirmDialog(devices.get(i).getDeviceID());
        });

        deleteLayout.setOnClickListener(view1 ->{
            showConfirmDialog(devices.get(i).getDeviceID());
        });

        return view;
    }

    private void showConfirmDialog(String selectedDeviceId) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        //  alertDialog.setTitle();
        if (selectedDeviceId.equals(App.DeviceID))
            alertDialog.setMessage("You about to remove current device from your list.\nApplication will exit after removing.");
        else
            alertDialog.setMessage("Are you sure want to remove this device from your list?");


        // On pressing Settings button
        alertDialog.setPositiveButton("Yes", (dialog, which) -> {
            EventBus.getDefault().post(selectedDeviceId);
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("No", (dialog, which) -> dialog.cancel());

        // Showing Alert Message
        alertDialog.show();
    }
}
