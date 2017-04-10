package com.crazymike.trace.sender;

import android.util.Log;

import com.crazymike.CrazyMike;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by ChaoJen on 2017/1/6.
 */

public class GASender {

    public static void sendScreenView(String message) {
        Tracker tracker = CrazyMike.getInstance().getDefaultTracker();
        tracker.setScreenName(message);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
