package com.info121.imessenger;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


public class App extends Application {
    public static String DEVICE_TYPE = "ANDROID";
    String TAG = "Application";

    @Override
    public void onCreate() {
        super.onCreate();


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Lato-Regular.ttf")
                       // .setFontAttrId(R.attr.fontPath)
                        .build());


    }
}
