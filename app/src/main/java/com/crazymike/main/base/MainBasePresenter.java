package com.crazymike.main.base;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.crazymike.main.MainActivity;
import com.crazymike.api.response.appapi2.GetAppVersion;
import com.crazymike.respositories.AppApi2Repository;
import com.crazymike.util.PreferencesKey;
import com.crazymike.util.PreferencesTool;
import com.crazymike.util.RxUtil;
import com.crazymike.util.Util;
import com.crazymike.web.WebViewActivity;

public class MainBasePresenter implements MainBaseContract.Presenter{

    private Context context;
    private MainBaseContract.View view;
    private boolean needUpdate = true;

    public MainBasePresenter(Context context, MainBaseContract.View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void checkAppVersion() {
        AppApi2Repository.getInstance().getAppVersion()
                .compose(RxUtil.mainAsync())
                .compose(RxUtil.bindLifecycle(view))
                .subscribe(this::compareVersion, Throwable::printStackTrace);
    }

    private void compareVersion(GetAppVersion getAppVersion) {

        if (!getAppVersion.getStatus().equals("200")) return;
        String versionName = Util.getVersionName(context);

        for (GetAppVersion.Version version : getAppVersion.getVersion()) {
            if (version.getOs().equals("Android")) {

                //should be
                if (Util.versionCompare(versionName, (version.getLowestVersion())) == -1) {
                    view.onAppUpdate(true);
                }

                //recommend
                else if (Util.versionCompare(versionName, (version.getLatestVersion())) == -1) {
                    view.onAppUpdate(false);
                }
                break;
            }
        }
    }

    @Override
    public void handleHyperLink(Intent intent) {
        if (null == intent) return;

        /* from push */
        if (null != intent.getAction() && intent.getAction().equals(MainActivity.PUSH)) {
            needUpdate = false;

            String id = intent.getStringExtra(MainActivity.MSG_ID);
            String hyperLink = intent.getStringExtra(MainActivity.HYPER_LINK);

            if (null != hyperLink && hyperLink.contains("crazymike.tw")) {
                WebViewActivity.startActivity(context, hyperLink);
            }

            if (null != id) {
                AppApi2Repository.getInstance().clickEvent(id, PreferencesTool.getInstance().get(PreferencesKey.GCM_TOKE, String.class))
                        .compose(RxUtil.bindLifecycle(view))
                        .compose(RxUtil.mainAsync())
                        .subscribe(response -> {/* do nothing */}, Throwable::printStackTrace);
            }
        }

        /* from public */
        else {

            String url = intent.getDataString();
            if (null == url) return;

            Uri uri = Uri.parse(url);
            if (null == uri) return;

            String hyperLink = uri.getQueryParameter("url");
            if (null != hyperLink  && hyperLink.contains("crazymike.tw")) {
                WebViewActivity.startActivity(context, hyperLink);
            }
        }
    }

    public boolean isNeedUpdate() {
        return needUpdate;
    }

    public MainBasePresenter setNeedUpdate(boolean needUpdate) {
        this.needUpdate = needUpdate;
        return this;
    }
}
