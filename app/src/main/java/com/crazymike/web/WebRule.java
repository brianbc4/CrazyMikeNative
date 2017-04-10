package com.crazymike.web;

import android.net.Uri;
import android.util.Log;

import com.crazymike.respositories.ProductRepository;

import java.net.URLDecoder;

public class WebRule {

    private static final String TAG = WebRule.class.getSimpleName();

    public static boolean isClickBack(String url) {

        Uri uri = Uri.parse(url);
        return uri.getPathSegments().size() == 0 || uri.getPath().equals("index");
    }

    static String isProduct(String url) {

        Uri uri = Uri.parse(url);

        boolean isProduct = false;

        for (String segment : uri.getPathSegments()) {
            if (segment.equals("product")) {
                isProduct = true;
            }
        }

        return isProduct ? url : null;
    }

    static String isTag(String url) {

        Uri uri = Uri.parse(url);

        boolean isCata = false;
        String tagId = null;

        for (String segment : uri.getPathSegments()) {
            if (segment.equals("cata")) {
                isCata = true;
            }

            if (segment.contains("tag-")) {
                tagId = segment.split("tag-")[1];
            }
        }
        return isCata ? tagId : null;
    }

    public static String isSearch(String url) {
        if (url.contains("search?")) {
            try {
                return URLDecoder.decode(url.split("w=")[1].split("&")[0], "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    static String isShowAlert(String url) {
        return Uri.parse(url).getQueryParameter("msg");
    }

    static boolean isSpecialUrl(String url) {
        return url.contains("line.me/R/shop/detail");
    }

    public static String getShowHtmlJS() {
        return "javascript:window.HTMLOUT.showHTML(document.body.innerHTML);";
    }

    public static boolean isWeb(String url) {
        if (url.equals("https://crazymike.tw/upload/product/upload/weiting/anticheat/index.html?channel=app")) return true;
        return false;
    }
}
