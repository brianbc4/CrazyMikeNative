package com.crazymike.alert;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crazymike.R;
import com.crazymike.respositories.OrderNoticeRepository;

/**
 * Created by ChaoJen on 2017/2/15.
 */

public class OrderNoticeDialog extends AlertDialog implements View.OnClickListener {

    private Context mContext;
    private Listener mListener;

    private View vOrderNotice;
    private ImageView imgMailNotice;
    private ImageView imgPhoneNotice;
    private LinearLayout lineLayUserInput;
    private EditText editUserInput;
    private TextView txtErrorFormat;
    private TextView txtSend;
    private ImageView imgCancel;
    private boolean mIsMailType;

    public interface Listener {
        void onOrderNoticeDialogSendBtnClick(String orderNoticeType, String orderNoticeSend);
    }

    public OrderNoticeDialog(Context context, Listener listener) {
        super(context);
        mContext = context;
        mListener = listener;
        initView();
    }

    private void initView() {
        vOrderNotice = LayoutInflater.from(mContext).inflate(R.layout.dialog_order_notice, null);
        imgMailNotice = (ImageView) vOrderNotice.findViewById(R.id.img_mail_notice);
        imgPhoneNotice = (ImageView) vOrderNotice.findViewById(R.id.img_phone_notice);
        lineLayUserInput = (LinearLayout) vOrderNotice.findViewById(R.id.lineLay_user_input);
        editUserInput = (EditText) vOrderNotice.findViewById(R.id.edit_user_input);
        txtErrorFormat = (TextView) vOrderNotice.findViewById(R.id.txt_error_format);
        txtSend = (TextView) vOrderNotice.findViewById(R.id.txt_send);
        imgCancel = (ImageView) vOrderNotice.findViewById(R.id.img_cancel);

        setView(vOrderNotice);
        lineLayUserInput.setVisibility(View.GONE);
        imgMailNotice.setOnClickListener(this);
        imgPhoneNotice.setOnClickListener(this);
        imgCancel.setOnClickListener(this);
        txtErrorFormat.setVisibility(View.GONE);
        txtSend.setOnClickListener(this);
        editUserInput.setHintTextColor(mContext.getResources().getColor(R.color.grey_700));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_mail_notice:
                mIsMailType = true;
                imgMailNotice.setImageResource(R.mipmap.ic_mail_notice_rectangle);
                imgMailNotice.getLayoutParams().height = (int) mContext.getResources().getDimension(R.dimen.order_notice_selected);
                imgMailNotice.getLayoutParams().width = (int) mContext.getResources().getDimension(R.dimen.order_notice_selected);
                imgMailNotice.requestLayout();

                imgPhoneNotice.setImageResource(R.mipmap.ic_phone_notice_circle);
                imgPhoneNotice.getLayoutParams().height = (int) mContext.getResources().getDimension(R.dimen.order_notice_unselected);
                imgPhoneNotice.getLayoutParams().width = (int) mContext.getResources().getDimension(R.dimen.order_notice_unselected);
                imgPhoneNotice.requestLayout();

                lineLayUserInput.setVisibility(View.VISIBLE);
                editUserInput.setText("");
                editUserInput.setHint(R.string.please_input_mail);
                editUserInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                break;
            case R.id.img_phone_notice:
                mIsMailType = false;
                imgMailNotice.setImageResource(R.mipmap.ic_mail_notice_circle);
                imgMailNotice.getLayoutParams().height = (int) mContext.getResources().getDimension(R.dimen.order_notice_unselected);
                imgMailNotice.getLayoutParams().width = (int) mContext.getResources().getDimension(R.dimen.order_notice_unselected);
                imgMailNotice.requestLayout();

                imgPhoneNotice.setImageResource(R.mipmap.ic_phone_notice_rectangle);
                imgPhoneNotice.getLayoutParams().height = (int) mContext.getResources().getDimension(R.dimen.order_notice_selected);
                imgPhoneNotice.getLayoutParams().width = (int) mContext.getResources().getDimension(R.dimen.order_notice_selected);
                imgPhoneNotice.requestLayout();

                lineLayUserInput.setVisibility(View.VISIBLE);
                editUserInput.setText("");
                editUserInput.setHint(R.string.please_input_phone);
                editUserInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_VARIATION_PHONETIC);
                break;
            case R.id.txt_send:
                if (mIsMailType && !android.util.Patterns.EMAIL_ADDRESS.matcher(editUserInput.getText().toString()).matches()) {
                    txtErrorFormat.setVisibility(View.VISIBLE);
                    return;
                } else if (!mIsMailType && editUserInput.length() != 10) {
                    txtErrorFormat.setVisibility(View.VISIBLE);
                    return;
                } else if (!mIsMailType && !editUserInput.getText().toString().substring(0, 2).equals("09")) {
                    txtErrorFormat.setVisibility(View.VISIBLE);
                    return;
                }
                mListener.onOrderNoticeDialogSendBtnClick(
                        mIsMailType ? OrderNoticeRepository.ORDER_NOTICE_TYPE_MAIL
                                : OrderNoticeRepository.ORDER_NOTICE_TYPE_PHONE,
                        editUserInput.getText().toString());
                dismiss();
                break;
            case R.id.img_cancel:
                dismiss();
                break;
        }
    }
}
