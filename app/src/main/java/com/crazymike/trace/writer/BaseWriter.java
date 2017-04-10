package com.crazymike.trace.writer;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ChaoJen on 2017/1/12.
 */

public abstract class BaseWriter {

    private static final String TAG = BaseWriter.class.getSimpleName();

    public static String getTargetWithTag(String tagId) {
        return tagId.equals("5") ? "channel-5" : String.format("/cata/tag-%s", tagId);
    }

    public static String getTargetWithItem(String itemId) {
        return String.format("/product/item-%s", itemId);
    }

    public static String getTargetWithKeyword(String keyword) {
        return String.format("/search-%s", keyword);
    }

    public static String getParams(String url) {
        String params = "";
        Map<String, String> paramsMap = new HashMap<>();
        String[] paramKeys = new String[]{"mkt", "mkt3", "partner", "affcode", "utm_medium", "utm_source", "utm_campaign"};
        for (int i = 0; i < paramKeys.length; i ++) {
            try {
                paramsMap.put(paramKeys[i], url.split(paramKeys[i] + "=")[1].split("&")[0]);
            }catch (Exception e) {
                Log.e(TAG, e.toString());
                paramsMap.remove(paramKeys[i]);
            }
        }

        for (int i = 0; i < paramKeys.length; i++) {
            if (paramsMap.get(paramKeys[i]) != null) {
                params += paramKeys[i] + "=" + paramsMap.get(paramKeys[i]) + "&";
            }
        }

        try {
            params = params.substring(0, params.length() - 1);
        }catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return params;
    }
}
