/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.crazymike.service;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.crazymike.R;
import com.crazymike.main.MainActivity;
import com.crazymike.notification.NotificationTool;
import com.crazymike.notification.ShowNotificationActivity;
import com.crazymike.util.PreferencesKey;
import com.crazymike.util.PreferencesTool;
import com.crazymike.util.Util;
import com.google.android.gms.gcm.GcmListenerService;

import java.io.IOException;
import java.net.URL;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = MyGcmListenerService.class.getSimpleName();

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String push = Util.bundleToString(data);

        Log.d(TAG, "From: " + from);
        Log.d(TAG, "send { extras=" + push + " }");

        if (!from.equals("53671992933")) return;

        String lastPush = PreferencesTool.getInstance().get(PreferencesKey.LAST_PUSH, String.class);

        if (null != lastPush && push.equals(lastPush)) return;

        PreferencesTool.getInstance().put(PreferencesKey.LAST_PUSH, push);
        sendNotification(data);
    }

    private void sendNotification(Bundle bundle) {

        String bundleTitle = bundle.getString("title", getString(R.string.app_name));
        String title = bundleTitle.equals("") ? getString(R.string.app_name) : bundleTitle;
        String message = bundle.getString("message", "");
        String id = bundle.getString("msgNo", "");
        String url = bundle.getString("url", "");
        String imageUrl = bundle.getString("imageUrl", "");
        String dialogType = bundle.getString("dialogType", "0");

        //notification type
        if (dialogType.equals("0")) {

            //no image
            if (imageUrl.equals("")) {
                showNotification(id, url, title, message, null);
                return;
            }

            try {
                URL target = new URL(url);
                Bitmap image = BitmapFactory.decodeStream(target.openConnection().getInputStream());
                showNotification(id, url, title, message, image);
            } catch (IOException e) {
                e.printStackTrace();
                showNotification(id, url, title, message, null);
            }


            //has image
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> Glide.with(this).load(imageUrl).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    showNotification(id, url, title, message, resource);
                }

                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    super.onLoadFailed(e, errorDrawable);
                    showNotification(id, url, title, message, null);
                }
            }));
        }

        //dialog type
        else {
            ShowNotificationActivity.startActivity(this, id, message, imageUrl, url);
        }
    }

    private void showNotification(String id, String url, String title, String message, Bitmap bitmap) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setAction(MainActivity.PUSH);
        intent.putExtra(MainActivity.MSG_ID, id);
        intent.putExtra(MainActivity.HYPER_LINK, url);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        int requestCode = null == id ? 0 : Integer.valueOf(id);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode /* Request code */, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationTool.show(this, requestCode, title, message, bitmap, pendingIntent);
    }
}