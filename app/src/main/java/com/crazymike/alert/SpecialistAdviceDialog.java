package com.crazymike.alert;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crazymike.R;
import com.crazymike.util.PreferencesKey;
import com.crazymike.util.PreferencesTool;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by ChaoJen on 2017/3/16.
 */

public class SpecialistAdviceDialog extends AlertDialog implements View.OnClickListener {

    private static final String TAG = SpecialistAdviceDialog.class.getSimpleName();

    private View view;

    private Listener listener;
    private Context context;
    private String productId;
    private String csServiceStartTime;
    private String csServiceEndTime;
    private boolean isTravelProduct;

    public SpecialistAdviceDialog(Context context,  Listener listener, String productId, boolean isTravelProduct, String csServiceStartTime, String csServiceEndTime) {
        super(context);
        this.context = context;
        this.view = LayoutInflater.from(context).inflate(R.layout.dialog_specialist_advice, null);
        this.productId = productId;
        this.isTravelProduct = isTravelProduct;
        this.listener = listener;
        this.csServiceStartTime = csServiceStartTime != null ? csServiceStartTime : context.getResources().getString(R.string.default_cs_service_start_time);
        this.csServiceEndTime = csServiceEndTime != null ? csServiceEndTime : context.getResources().getString(R.string.default_cs_service_end_time);

        if (PreferencesTool.getInstance().get(PreferencesKey.IS_DEVEL, Boolean.class)) {
            String terminalServiceStartTime = PreferencesTool.getInstance().get(PreferencesKey.TERMINAL_SERVICE_TIME_START, String.class);
            String terminalServiceEndTime = PreferencesTool.getInstance().get(PreferencesKey.TERMINAL_SERVICE_TIME_END, String.class);
            this.csServiceStartTime = terminalServiceStartTime.length() != 0 ? terminalServiceStartTime : context.getResources().getString(R.string.default_cs_service_start_time);
            this.csServiceEndTime = terminalServiceEndTime.length() != 0 ? terminalServiceEndTime : context.getResources().getString(R.string.default_cs_service_end_time);
        }
        initView();
    }

    private void initView() {
        setView(view);

        TextView textViewPhoneNumber = (TextView) view.findViewById(R.id.textView_phoneNumber);
        TextView textViewNumberTwo = (TextView) view.findViewById(R.id.textView_numberTwo);
        TextView textViewProductId = (TextView) view.findViewById(R.id.textView_productId);
        ImageView imageViewCancel = (ImageView) view.findViewById(R.id.imageView_cancel);
        LinearLayout linearLayoutMessage = (LinearLayout) view.findViewById(R.id.linearLayout_message);
        Button buttonCallNow = (Button) view.findViewById(R.id.button_callNow);
        Button buttonMessageOnline = (Button) view.findViewById(R.id.button_messageOnline);

        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("HH:mm:ss");
        LocalTime csServiceStartDateTime = LocalTime.parse(csServiceStartTime, dateTimeFormatter);
        LocalTime csServiceEndDateTime = LocalTime.parse(csServiceEndTime, dateTimeFormatter);
        buttonCallNow.setVisibility(LocalTime.now().isAfter(csServiceStartDateTime) && LocalTime.now().isBefore(csServiceEndDateTime) ? View.VISIBLE : View.GONE);
        linearLayoutMessage.setVisibility(buttonCallNow.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        textViewPhoneNumber.setText(isTravelProduct ? context.getResources().getString(R.string.specialist_advice_phone_number_travel) : context.getResources().getString(R.string.specialist_advice_phone_number));
        textViewNumberTwo.setVisibility(isTravelProduct ? View.GONE : View.VISIBLE);
        textViewProductId.setText(String.format(context.getResources().getString(R.string.specialist_advice_product_id), productId));
        imageViewCancel.setOnClickListener(this);
        buttonCallNow.setOnClickListener(this);
        buttonMessageOnline.setOnClickListener(this);
    }

    public interface Listener {
        void onSpecialistAdviceCallNowClick(String phoneNumber);
        void onSpecialistAdviceMessageBtnClick();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView_cancel:
                dismiss();
                break;
            case R.id.button_callNow:
                listener.onSpecialistAdviceCallNowClick(isTravelProduct ? context.getResources().getString(R.string.specialist_advice_phone_number_travel) : context.getResources().getString(R.string.specialist_advice_phone_number));
                break;
            case R.id.button_messageOnline:
                listener.onSpecialistAdviceMessageBtnClick();
                break;
        }
    }
}
