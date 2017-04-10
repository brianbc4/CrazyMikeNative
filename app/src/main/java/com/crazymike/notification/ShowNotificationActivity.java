package com.crazymike.notification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.crazymike.R;
import com.crazymike.base.BaseActivity;
import com.crazymike.main.MainActivity;
import com.crazymike.util.PreferencesKey;
import com.crazymike.util.PreferencesTool;

public class ShowNotificationActivity extends BaseActivity {

    private static final String ID = "ID";
    private static final String MESSAGE = "MESSAGE";
    private static final String IMAGE_PATH = "IMAGE_PATH";
    private static final String HYPER_LINK = "HYPER_LINK";

    public static void startActivity(Context context, String id, String message, String imagePath, String hyperLink) {
        Intent intent = new Intent(context, ShowNotificationActivity.class);
        intent.putExtra(ID, id);
        intent.putExtra(MESSAGE, message);
        intent.putExtra(IMAGE_PATH, imagePath);
        intent.putExtra(HYPER_LINK, hyperLink);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_push);

        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString(ID, "");
        String message = bundle.getString(MESSAGE, "");
        String imagePath = bundle.getString(IMAGE_PATH, "");
        String hyperLink = bundle.getString(HYPER_LINK, "");

        PreferencesTool.getInstance().put(PreferencesKey.SHOW_NOTIFICATION_ID, id);

        ImageView img = (ImageView) findViewById(R.id.dialog_push_img);
        if (img != null) {
            if (!imagePath.equals("")) {
                Glide.with(this).load(imagePath).into(img);
            } else {
                img.setVisibility(View.GONE);
            }
        }

        TextView content = (TextView) findViewById(R.id.dialog_push_content);
        if (content != null) content.setText(message);

        View cancel = findViewById(R.id.dialog_push_cancel);
        if (cancel != null) cancel.setOnClickListener(v -> finish());

        View confirm = findViewById(R.id.dialog_push_confirm);
        if (confirm != null) {
            confirm.setOnClickListener(v -> {
                MainActivity.startActivity(this, id, hyperLink);
                finish();
            });
        }
    }
}
