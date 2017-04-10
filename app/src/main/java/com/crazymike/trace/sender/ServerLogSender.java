package com.crazymike.trace.sender;

import com.crazymike.api.NetworkService;
import com.crazymike.trace.writer.BaseWriter;
import com.crazymike.util.PreferencesKey;
import com.crazymike.util.PreferencesTool;
import com.crazymike.util.RxUtil;

/**
 * Created by ChaoJen on 2017/1/5.
 */

public class ServerLogSender {

    public static void send(String url, String target) {
        NetworkService.getInstance().getServerApi().sendServerLog(
                PreferencesTool.getInstance().get(PreferencesKey.PHPSESSID, String.class),
                PreferencesTool.getInstance().get(PreferencesKey.LOGIN_USER, String.class),
                PreferencesTool.getInstance().get(PreferencesKey.MEMBER_ID, String.class),
                PreferencesTool.getInstance().get(PreferencesKey.GCM_TOKE, String.class),
                BaseWriter.getParams(url),
                target
        ).compose(RxUtil.mainAsync()).subscribe(baseResponse -> {}, Throwable::printStackTrace);
    }
}
