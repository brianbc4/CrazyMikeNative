package com.crazymike.receiver;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.crazymike.api.response.appapi2.GetCueSheetRes;
import com.crazymike.models.CueSheet;
import com.crazymike.respositories.AppApi2Repository;
import com.crazymike.util.PreferencesKey;
import com.crazymike.util.PreferencesTool;
import com.crazymike.util.RxUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class UpdateReceiver extends BroadcastReceiver {

    private static final String TAG = "UpdateReceiver";

    private static final int UPDATE_DURATION = 3 * 60 * 60;
    private Context context;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.TAIWAN);
    private AlarmManager alarmManager;
    private List<CueSheet> cueSheetList = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onUpdateCueSheet");

        this.context = context;
        alarmManager = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);
        cueSheetList = PreferencesTool.getInstance().get(PreferencesKey.CUE_SHEET, new TypeToken<ArrayList<CueSheet>>() {
        }.getType());
        if (null == cueSheetList) cueSheetList = new ArrayList<>();

        AppApi2Repository.getInstance().getCueSheet()
                .compose(RxUtil.mainAsync())
                .subscribe(this::handleCueSheet, Throwable::printStackTrace);

        setNextUpdate();
    }

    private void handleCueSheet(GetCueSheetRes getCueSheetRes) {
        if (getCueSheetRes.getStatus().equals("200")) {

            int times = 0;
            for (CueSheet cueSheet : getCueSheetRes.getEvents()) {
                for (CueSheet sheet : cueSheetList) {
                    if (!cueSheet.getId().equals(sheet.getId())) times++;
                    else cancelAlarm(sheet);
                }
                setAlarm(cueSheet);
            }

            if (times != 0) {
                AppApi2Repository.getInstance().addPushItem(PreferencesTool.getInstance().get(PreferencesKey.GCM_TOKE, String.class), String.valueOf(times))
                        .compose(RxUtil.mainAsync())
                        .subscribe(baseRes -> {/*do nothing*/}, Throwable::printStackTrace);
            }

            cueSheetList = getCueSheetRes.getEvents();
            PreferencesTool.getInstance().put(PreferencesKey.CUE_SHEET, new Gson().toJson(cueSheetList));
        }
    }

    public void setAlarm(CueSheet cueSheet) {
        Intent intent = new Intent(context, LocalPushReceiver.class);
        intent.putExtra("CUE_SHEET", cueSheet);
        PendingIntent startIntent = PendingIntent.getBroadcast(context, Integer.valueOf(cueSheet.getId()), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar = parseToCalendar(cueSheet.getPushDate());
        if (null != calendar) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), startIntent), startIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), startIntent);
            }
        }
    }

    private void setNextUpdate() {
        Intent intent = new Intent(context, UpdateReceiver.class);
        PendingIntent startIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // cancel old alarm
        alarmManager.cancel(startIntent);

        // setup new alarm to UPDATE_DURATION ago
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, UPDATE_DURATION);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), startIntent);
    }

    private void cancelAlarm(CueSheet cueSheet) {
        Intent intent = new Intent(context, LocalPushReceiver.class);
        PendingIntent stopIntent = PendingIntent.getBroadcast(context, Integer.valueOf(cueSheet.getId()), intent, PendingIntent.FLAG_NO_CREATE);
        alarmManager.cancel(stopIntent);
    }

    private Calendar parseToCalendar(String date) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sdf.parse(date));
            return calendar;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
