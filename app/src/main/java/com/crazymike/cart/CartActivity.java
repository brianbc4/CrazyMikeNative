package com.crazymike.cart;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.crazymike.R;
import com.crazymike.base.BaseActivity;
import com.crazymike.web.WebRule;

public class CartActivity extends BaseActivity implements CartContract.View {

    public static final String TAG = "CartActivity";

    private CartPresenter presenter;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, CartActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new CartPresenter(this);

        setContentView(R.layout.activity_cart);

        WebView webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAppCacheEnabled(false);
        webView.addJavascriptInterface(new CartJavaScriptInterface(), "HTMLOUT");
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (WebRule.isClickBack(url)) onBackPressed();
                Log.i(TAG, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.i(TAG, url);
            }
        });
        webView.loadUrl("https://m.crazymike.tw/ajax-cookie_set?k=carts&rtn=json");
    }


    class CartJavaScriptInterface {
        @JavascriptInterface
        public void showHTML(String html) {
            presenter.handleHTML(html);
        }
    }
}
