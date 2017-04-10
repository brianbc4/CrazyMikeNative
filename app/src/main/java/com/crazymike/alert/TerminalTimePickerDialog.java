package com.crazymike.alert;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import com.crazymike.R;
import com.crazymike.util.PreferencesKey;
import com.crazymike.util.PreferencesTool;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by Elliot on 2017/3/27.
 */

public class TerminalTimePickerDialog extends AlertDialog implements View.OnClickListener {

    private static final String TAG = TerminalTimePickerDialog.class.getSimpleName();
    private Context context;
    private Listener listener;
    private PreferencesKey preferencesKey;

    private TimePicker timePicker;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("HH:mm:ss");

    @RequiresApi(api = Build.VERSION_CODES.M)
    public TerminalTimePickerDialog(@NonNull Context context, Listener listener, PreferencesKey preferencesKey) {
        super(context);
        this.context = context;
        this.listener = listener;
        this.preferencesKey = preferencesKey;
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_terminal_time_picker, null);
        setView(view);

        timePicker = (TimePicker) view.findViewById(R.id.timePicker);
        Button buttonConfirm = (Button) view.findViewById(R.id.button_confirm);

        String time = PreferencesTool.getInstance().get(preferencesKey, String.class);
        LocalTime localTime = LocalTime.parse(time, dateTimeFormatter);
        timePicker.setHour(localTime.getHourOfDay());
        timePicker.setMinute(localTime.getMinuteOfHour());
        buttonConfirm.setOnClickListener(this);
    }

    public interface Listener {

        void onTimePick(PreferencesKey preferencesKey, String time);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_confirm:
                LocalTime localTime =
                        new LocalTime().hourOfDay().setCopy(timePicker.getHour())
                                .minuteOfHour().setCopy(timePicker.getMinute())
                                .secondOfMinute().setCopy("00");
                listener.onTimePick(preferencesKey, localTime.toString(dateTimeFormatter));
                dismiss();
                break;
        }
    }
}
