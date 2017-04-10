package com.crazymike;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;

import com.crashlytics.android.Crashlytics;
import com.crazymike.respositories.CookieRepository;
import com.crazymike.util.PreferencesTool;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import io.fabric.sdk.android.Fabric;

public class CrazyMike extends Application {

    private static CrazyMike INSTANCE;
    private Tracker mTracker;

    public static CrazyMike getInstance() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        Fabric.with(this, new Crashlytics());
        PreferencesTool.init(getApplicationContext());
        CookieRepository.getInstance().initial(this);
    }

    synchronized public Tracker getDefaultTracker() {

        // Obtain the shared Tracker instance.
//        AnalyticsApplication application = (AnalyticsApplication) getApplication();
//        mTracker = application.getDefaultTracker();

        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
            mTracker.enableAdvertisingIdCollection(true);
        }
        return mTracker;
    }

    public static String IMEI() {
        return String.format("%s%s", Settings.Secure.getString(CrazyMike.getInstance().getContentResolver(), Settings.Secure.ANDROID_ID), Build.SERIAL);
    }

    public static String version() {
        try {
            PackageInfo pInfo = CrazyMike.getInstance().getPackageManager().getPackageInfo(CrazyMike.getInstance().getPackageName(), 0);
            return String.format("%s(%s)", pInfo.versionName, pInfo.versionCode);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }
}
