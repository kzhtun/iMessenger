package com.info121.imessenger;

import android.app.Application;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;


import androidx.annotation.Nullable;
import androidx.core.provider.FontRequest;
import androidx.emoji.text.EmojiCompat;
import androidx.emoji.text.FontRequestEmojiCompatConfig;

import com.info121.imessenger.utils.PrefDB;
import com.info121.imessenger.utils.Util;

import java.util.TimeZone;




public class App extends Application {
    public static String DEVICE_TYPE = "ANDROID";
    String TAG = "Application";

    public static Runnable mRunnable;
    public static final Handler mHandler = new Handler();

    //DEV
    public static String CONST_REST_API_URL = "http://info121.sytes.net:84/RestAPIInfoMessage/MyLimoService.svc/";


    public static String DEFAULT_STATUS_MESSAGE = "Hi, I'm using iMessenger by Info121";


    public static String UserName = "";
    public static String SecretKey = "";
    public static String MobileKey = "";
    public static String DeviceID = "";
    public static String AuthToken = "";
    public static String MobileNo = "";


    public static PrefDB prefDB = null;
    public static String FCM_TOKEN = "";


    public static Uri DEFAULT_SOUND_URI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    public static Uri NOTIFICATION_SOUND_URI = null;
    public static Uri PROMINENT_SOUND_URI = null;

    public static String CONST_NOTIFICATION_TONE = "NOTIFICATION_TONE";
    public static String CONST_PROMINENT_TONE = "PROMINENT_TONE";

    public static TimeZone timeZone = TimeZone.getTimeZone("GMT+08:00");

    public static final String[] SONG_PROJECTION = new String[]{
            MediaStore.Audio.Media._ID
            , MediaStore.Audio.Media.TITLE
            , MediaStore.Audio.Media.ARTIST
            , MediaStore.Audio.Albums.ALBUM
            , MediaStore.Audio.Media.DURATION
            , MediaStore.Audio.Media.TRACK
            , MediaStore.Audio.Media.ARTIST_ID
            , MediaStore.Audio.Media.ALBUM_ID
            , MediaStore.Audio.Media.DATA
            , MediaStore.Audio.Media.ALBUM_KEY
    };

    @Override
    public void onCreate() {
        super.onCreate();

        App.DeviceID = Util.getDeviceID(getApplicationContext());


        final FontRequest fontRequest = new FontRequest(
                "com.google.android.gms.fonts",
                "com.google.android.gms",
                "Noto Color Emoji Compat",
                R.array.com_google_android_gms_fonts_certs);


        EmojiCompat.Config config = new FontRequestEmojiCompatConfig(this, fontRequest);
        EmojiCompat.init(config);


//        ViewPump.init(ViewPump.builder()
//                .addInterceptor(new CalligraphyInterceptor(
//                        new CalligraphyConfig.Builder()
//                                .setDefaultFontPath("fonts/Lato-Regular.ttf")
//                                .setFontAttrId(R.attr.fontPath)
//                                .build()))
//                .build());

//        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                .setDefaultFontPath("fonts/Lato-Regular.ttf")
//                // .setFontAttrId(R.attr.fontPath)
//                .build());


        // Firebase
//        FirebaseOptions options = new FirebaseOptions.Builder()
//                .setApplicationId("com.info121.imessenger") // Required for Analytics.
//                .setProjectId("imessenger-df3fc") // Required for Firebase Installations.
//                .setApiKey("AIzaSyCKdpQMfBUimBDr0VoTfa6tV08593qPaQ8") // Required for Auth.
//                .build();

//        FirebaseApp.initializeApp(this, options, "iMessenger");


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        // for notification tone
        prefDB = new PrefDB(getApplicationContext());

        if (prefDB.getString("OLD_CH_ID").length() == 0)
            prefDB.putString("OLD_CH_ID", "DEFAULT_OLD");

        if (prefDB.getString("NEW_CH_ID").length() == 0)
            prefDB.putString("NEW_CH_ID", "DEFAULT_NEW");

        if (prefDB.getString("OLD_CH_ID_P").length() == 0)
            prefDB.putString("OLD_CH_ID_P", "DEFAULT_OLD_P");

        if (prefDB.getString("NEW_CH_ID_P").length() == 0)
            prefDB.putString("NEW_CH_ID_P", "DEFAULT_NEW_P");


    }


    public static Uri getProminentSoundUri() {
        if (prefDB.getString(CONST_NOTIFICATION_TONE) == "")
            return App.DEFAULT_SOUND_URI;
        else
            return Uri.parse(prefDB.getString(CONST_PROMINENT_TONE));
    }

    public static Uri getNotificationSoundUri() {
        if (prefDB.getString(CONST_NOTIFICATION_TONE) == "")
            return App.DEFAULT_SOUND_URI;
        else
            return Uri.parse(prefDB.getString(CONST_NOTIFICATION_TONE));
    }


    public static String getNewChannelId() {
        return prefDB.getString("NEW_CH_ID");
    }

    public static String getOldChannelId() {
        return prefDB.getString("OLD_CH_ID");
    }

    public static String getNewChannelIdP() {
        return prefDB.getString("NEW_CH_ID_P");
    }

    public static String getOldChannelIdP() {
        return prefDB.getString("OLD_CH_ID_P");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        mHandler.removeCallbacks(App.mRunnable);
        mRunnable = null;

    }
}
