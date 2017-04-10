package com.crazymike.alert;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.afollestad.materialdialogs.MaterialDialog;
import com.crazymike.R;
import com.crazymike.base.BaseActivity;


/**
 * for show dialog on top anywhere
 */
public class ShowAlertActivity extends BaseActivity {

    public static final String MSG = "MSG";

    public static void startActivity(Context context, String msg) {
        Intent intent = new Intent(context, ShowAlertActivity.class);
        intent.putExtra(MSG, msg);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new MaterialDialog.Builder(this)
                .content(getIntent().getStringExtra(MSG))
                .positiveText(R.string.confirm)
                .dismissListener(dialogInterface -> finish())
                .show();
    }
}
