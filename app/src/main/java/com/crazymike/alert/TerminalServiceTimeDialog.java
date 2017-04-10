package com.crazymike.alert;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crazymike.R;
import com.crazymike.api.NetworkService;
import com.crazymike.models.SWCalendar;
import com.crazymike.util.PreferencesKey;
import com.crazymike.util.PreferencesTool;
import com.crazymike.util.RxUtil;

import java.util.List;

/**
 * Created by Elliot on 2017/3/27.
 */

public class TerminalServiceTimeDialog
        extends AlertDialog
        implements View.OnClickListener, TerminalTimePickerDialog.Listener {

    private static final String TAG = TerminalServiceTimeDialog.class.getSimpleName();

    private Context context;
    private TextView textViewStartTime;
    private TextView textViewEndTime;

    public TerminalServiceTimeDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_terminal_service_time, null);
        setView(view);

        RelativeLayout relativeLayoutStartTime = (RelativeLayout) view.findViewById(R.id.relativeLayout_startTime);
        RelativeLayout relativeLayoutEndTime = (RelativeLayout) view.findViewById(R.id.relativeLayout_endTime);
        textViewStartTime = (TextView) view.findViewById(R.id.textView_startTime);
        textViewEndTime = (TextView) view.findViewById(R.id.textView_endTime);

        setTime();
        relativeLayoutStartTime.setOnClickListener(this);
        relativeLayoutEndTime.setOnClickListener(this);
    }

    private void setTime() {
        String startTime = PreferencesTool.getInstance().get(PreferencesKey.TERMINAL_SERVICE_TIME_START, String.class);
        String endTime = PreferencesTool.getInstance().get(PreferencesKey.TERMINAL_SERVICE_TIME_END, String.class);
        if (startTime.length() == 0 || endTime.length() == 0) {
            initTerminalServiceTime();
        }
        textViewStartTime.setText(startTime);
        textViewEndTime.setText(endTime);
    }

    private void initTerminalServiceTime() {
        NetworkService.getInstance().getProductApi().callSWCalendar("swcalendar", "1", "")
                .compose(RxUtil.mainAsync())
                .subscribe(swCalendarResponse -> {
                    List<SWCalendar> swCalendarList = swCalendarResponse.getSwCalendarList();
                    String csServiceStartTime = swCalendarList.get(0).getCsServiceStartTime();
                    String csServiceEndTime = swCalendarList.get(0).getCsServiceEdnTime();

                    textViewStartTime.setText(csServiceStartTime);
                    textViewEndTime.setText(csServiceEndTime);

                    PreferencesTool.getInstance().put(PreferencesKey.TERMINAL_SERVICE_TIME_START, csServiceStartTime);
                    PreferencesTool.getInstance().put(PreferencesKey.TERMINAL_SERVICE_TIME_END, csServiceEndTime);
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relativeLayout_startTime:
                new TerminalTimePickerDialog(context, this, PreferencesKey.TERMINAL_SERVICE_TIME_START).show();
                break;
            case R.id.relativeLayout_endTime:
                new TerminalTimePickerDialog(context, this, PreferencesKey.TERMINAL_SERVICE_TIME_END).show();
                break;
        }
    }

    @Override
    public void onTimePick(PreferencesKey preferencesKey, String time) {
        PreferencesTool.getInstance().put(preferencesKey, time);
        setTime();
    }
}
