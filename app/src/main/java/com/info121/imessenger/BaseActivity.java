package com.info121.imessenger;

import android.content.Context;
import android.util.Log;


import androidx.appcompat.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;


public class BaseActivity extends AppCompatActivity {
    final String TAG = BaseActivity.class.getSimpleName();

    @Override
    protected void onStart() {
        super.onStart();

        // event bus register
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        Log.e(TAG, "EventBus Registered on Activity ... " + this.getLocalClassName());
    }

    @Override
    protected void onStop() {

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // event bus unregister
        EventBus.getDefault().unregister(this);
        Log.e(TAG, "EventBus Un-Registered on Activity ... " + this.getLocalClassName());
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        // super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Subscribe
    public void onEvent(Throwable t) {
    }
}
