package com.crazymike.receiver;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.PowerManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.crazymike.R;
import com.crazymike.main.MainActivity;
import com.crazymike.models.CueSheet;
import com.crazymike.notification.NotificationTool;
import com.crazymike.notification.ShowNotificationActivity;
import com.crazymike.util.PreferencesKey;
import com.crazymike.util.PreferencesTool;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;


public class LocalPushReceiver extends BroadcastReceiver {

    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "Wake Lock");
        wakeLock.acquire();

        /* Get Intent Data */
        CueSheet cueSheet = intent.getParcelableExtra("CUE_SHEET");

        /* Delete Local Data */
        List<CueSheet> cueSheetList = PreferencesTool.getInstance().get(PreferencesKey.CUE_SHEET, new TypeToken<ArrayList<CueSheet>>() {
        }.getType());

        Observable.from(cueSheetList).filter(sheet -> !sheet.getId().equals(cueSheet.getId())).toList().subscribe(cueSheets -> {
            PreferencesTool.getInstance().put(PreferencesKey.CUE_SHEET, new Gson().toJson(cueSheets));
        }, Throwable::printStackTrace);

        if (cueSheet.getDialogType().equals("0")) showNotification(cueSheet);
        else
            ShowNotificationActivity.startActivity(context, cueSheet.getId(), cueSheet.getMessage(), cueSheet.getImagePath(), cueSheet.getHyperLink());

        wakeLock.release();
    }

    private void showNotification(CueSheet cueSheet) {

        if (cueSheet.getImagePath() == null || cueSheet.getImagePath().equals("")) {
            showNotification(cueSheet, null);
            return;
        }

        Glide.with(context).load(cueSheet.getImagePath()).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                showNotification(cueSheet, resource);
            }
        });

    }

    private void showNotification(CueSheet cueSheet, Bitmap bitmap) {
        Intent clickIntent = new Intent(context, MainActivity.class);
        clickIntent.setAction(MainActivity.PUSH);
        clickIntent.putExtra(MainActivity.HYPER_LINK, cueSheet.getHyperLink());
        clickIntent.putExtra(MainActivity.MSG_ID, cueSheet.getId());
        PendingIntent pendingIntent = PendingIntent.getActivity(context.getApplicationContext(), 0, clickIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationTool.show(context, Integer.valueOf(cueSheet.getId()), context.getString(R.string.app_name), cueSheet.getMessage(), bitmap, pendingIntent);
    }
}
