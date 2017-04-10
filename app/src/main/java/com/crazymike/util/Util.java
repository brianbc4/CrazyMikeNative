package com.crazymike.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.text.DecimalFormat;
import java.util.concurrent.TimeoutException;

public class Util {

    static boolean checkNetworkState(Throwable throwable) {
        return throwable instanceof SocketException || throwable instanceof SocketTimeoutException || throwable instanceof TimeoutException;
    }

    public static Integer versionCompare(String str1, String str2) {
        String[] vals1 = str1.split("\\.");
        String[] vals2 = str2.split("\\.");
        int i = 0;
        while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])) {
            i++;
        }
        if (i < vals1.length && i < vals2.length) {
            int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
            return Integer.signum(diff);
        } else {
            return Integer.signum(vals1.length - vals2.length);
        }
    }

    public static String getVersionName(Context context) {

        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String bundleToString(Bundle bundle) {
        if (bundle == null) return "";

        String separator = "";
        StringBuilder sb = new StringBuilder("{ ");
        for (String key : bundle.keySet()) {
            Object value = bundle.get(key);
            sb.append(separator).append(key).append(":");

            if (value == null) {
                sb.append("<null>");
            } else if (value instanceof String) {
                sb.append("\"").append(value).append("\"");
            } else if (value instanceof Boolean || value instanceof Integer || value instanceof Float || value instanceof Double) {
                sb.append(value);
            } else {
                sb.append(value).append(" (").append(value.getClass().getSimpleName()).append(")");
            }
            separator = ", ";
        }
        sb.append(" }");
        return sb.toString();
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static String getDiscount(String priceFake, String priceTitle) {

        try {

            float fake = Integer.valueOf(priceFake);
            float title = Integer.valueOf(priceTitle);
            float discount = title / fake * 10;
            return new DecimalFormat("#.#").format(discount);
        } catch (Exception e) {
            return "";
        }
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
