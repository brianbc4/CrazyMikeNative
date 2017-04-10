package com.crazymike.alert;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.crazymike.R;

/**
 * Created by ChaoJen on 2017/3/20.
 */

public class SpecialistAdviceMessageDialog extends AlertDialog implements View.OnClickListener, TextWatcher {

    private static final String TAG = SpecialistAdviceMessageDialog.class.getSimpleName();

    private Context context;
    private View view;
    private Listener listener;
    private EditText editTextName;
    private EditText editTextPhone;
    private EditText editTextEmail;
    private EditText editTextContent;
    private Button buttonSend;

    private String productName;
    private String productId;

    public SpecialistAdviceMessageDialog(@NonNull Context context, Listener listener, String productName, String productId) {
        super(context);
        this.context = context;
        this.listener = listener;
        this.view = LayoutInflater.from(context).inflate(R.layout.dialog_specialist_advice_message, null);
        this.productName = productName;
        this.productId = productId;

        initView();
    }

    public interface Listener {

        void onSendBtnClick(String name, String title, String phoneNumber, String email, String content);
    }

    private void initView() {
        setView(view);

        ImageView imageViewCancel = (ImageView) view.findViewById(R.id.imageView_cancel);
        editTextName = (EditText) view.findViewById(R.id.editText_name);
        editTextPhone = (EditText) view.findViewById(R.id.editText_phone);
        editTextEmail = (EditText) view.findViewById(R.id.editText_email);
        editTextContent = (EditText) view.findViewById(R.id.editText_content);
        buttonSend = (Button) view.findViewById(R.id.button_send);

        String remarkContent = String.format("%s: %s\n%s: %s", context.getResources().getString(R.string.product_id), productId, context.getResources().getString(R.string.product_name), productName);
        editTextContent.setText(remarkContent);
        imageViewCancel.setOnClickListener(this);
        buttonSend.setOnClickListener(this);
        buttonSend.setEnabled(false);
        editTextName.addTextChangedListener(this);
        editTextPhone.addTextChangedListener(this);
        editTextContent.addTextChangedListener(this);

        InputFilter filter = (CharSequence source, int start, int end, Spanned dest, int dstart, int dend) -> {
            for (int i = start; i < end; i++) {
                if (Character.isWhitespace(source.charAt(i))) {
                    return "";
                }
            }
            return null;
        };
        editTextName.setFilters(new InputFilter[] { filter });
        editTextEmail.setFilters(new InputFilter[] { filter });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView_cancel:
                dismiss();
                break;
            case R.id.button_send:
                String name = editTextName.getText().toString();
                String phoneNumber = editTextPhone.getText().toString();
                String email = editTextEmail.getText().toString();
                String content = editTextContent.getText().toString();
                listener.onSendBtnClick(name, String.format("%s:%s", context.getResources().getString(R.string.product_id), productId), phoneNumber, email, content);
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        buttonSend.setEnabled(editTextName.length() != 0 && editTextPhone.length() != 0 && editTextContent.length() != 0);
    }
}
