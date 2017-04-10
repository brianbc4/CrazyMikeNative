package com.crazymike.main.base;

import android.content.Intent;

public interface MainBaseContract {

    interface View {
        void onAppUpdate(boolean b);
    }

    interface Presenter {
        void checkAppVersion();

        void handleHyperLink(Intent intent);
    }
}
