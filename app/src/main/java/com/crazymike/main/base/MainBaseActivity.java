package com.crazymike.main.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.webkit.CookieSyncManager;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.crazymike.R;
import com.crazymike.api.URL;
import com.crazymike.base.BaseActivity;
import com.crazymike.trace.writer.GAWriter;
import com.crazymike.receiver.UpdateReceiver;
import com.crazymike.service.RegistrationIntentService;
import com.crazymike.trace.sender.GASender;
import com.crazymike.util.QuickstartPreferences;
import com.crazymike.util.Util;
import com.crazymike.web.WebExtra;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MainBaseActivity extends BaseActivity implements MainBaseContract.View {

    private static final String TAG = MainBaseActivity.class.getSimpleName();
    protected MainBasePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new MainBasePresenter(this, this);
        checkNetWork();
        registerGCM();
        checkAppVersion();
        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        presenter.handleHyperLink(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String url = "";
        try {
            url = getIntent().getStringExtra(WebExtra.URL);
        }catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        GASender.sendScreenView(new GAWriter(url).getLaunchMessage());
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
        registerReceiver(connectionChangeReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.getInstance().startSync();
        }
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        unregisterReceiver(connectionChangeReceiver);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.getInstance().stopSync();
        }
        super.onPause();
    }

    /**
     * ======================================== CheckAppVersion ========================================
     */
    protected void checkAppVersion() {
        presenter.checkAppVersion();
    }

    @Override
    public void onAppUpdate(boolean force) {
        if (force) {
            new MaterialDialog.Builder(this)
                    .cancelable(false)
                    .content(R.string.dialog_update_content)
                    .positiveText(R.string.dialog_update_positive)
                    .onPositive((dialog, which) -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(URL.PLAY_URL)))).show();
        } else {
            new MaterialDialog.Builder(this)
                    .content(R.string.dialog_update_content)
                    .negativeText(R.string.dialog_update_negative)
                    .positiveText(R.string.dialog_update_positive)
                    .onPositive((dialog, which) -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(URL.PLAY_URL)))).show();
        }
    }

    /**
     * ======================================== Connection ========================================
     */
    private NetworkConnectionChangeReceiver connectionChangeReceiver = new NetworkConnectionChangeReceiver();

    private class NetworkConnectionChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetInfo != null) {
                onNetworkConnected(false);
            } else {
                onNetworkDisConnected(false);
            }
        }
    }

    protected boolean checkNetWork() {
        if (Util.isConnected(this)) {
            onNetworkConnected(true);
            return true;
        } else {
            onNetworkDisConnected(true);
            return false;
        }
    }

    protected void onNetworkConnected(boolean active) {
        if (!active) return;
        if (presenter.isNeedUpdate()) {
            updateCueSheet();
            presenter.setNeedUpdate(false);
        }
    }

    protected void onNetworkDisConnected(boolean active) {
        if (!active) return;
    }

    /**
     * ======================================== GCM ========================================
     */
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    protected void registerGCM() {

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
            }
        };

        if (checkPlayServices()) {
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Toast.makeText(this, getString(R.string.hint_no_google_service), Toast.LENGTH_LONG).show();
            }
            return false;
        }
        return true;
    }

    /**
     * ======================================== Local Push ========================================
     */
    protected void updateCueSheet() {
        sendBroadcast(new Intent(this, UpdateReceiver.class));
    }
}
