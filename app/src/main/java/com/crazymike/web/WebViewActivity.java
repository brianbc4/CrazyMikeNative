package com.crazymike.web;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.crazymike.R;
import com.crazymike.alert.ShowAlertActivity;
import com.crazymike.base.BaseActivity;
import com.crazymike.models.Tag;
import com.crazymike.product.detail.ProductDetailActivity;
import com.crazymike.respositories.CookieRepository;
import com.crazymike.search.result.SearchQueryActivity;
import com.crazymike.util.ActionName;
import com.orhanobut.logger.Logger;

import java.net.URISyntaxException;

public class WebViewActivity extends BaseActivity implements WebViewContract.View {

    private static final String TAG = "WebViewActivity";

    private static final String URL = "URL";
    private static final String TITLE = "TITLE";

    private String url;
    private boolean isPause = false;

    private WebViewPresenter presenter;
    private WebView webView;
    private Handler javascriptInterfaceHandler = new Handler();

    public static void startActivity(Context context, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(URL, url);
        context.startActivity(intent);
    }

    public static void startActivity(Activity activity, String url, int requestCode) {
        Intent intent = new Intent(activity, WebViewActivity.class);
        intent.putExtra(URL, url);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void startActivity(Activity activity, String url, String title, int requestCode) {
        Intent intent = new Intent(activity, WebViewActivity.class);
        intent.putExtra(URL, url);
        intent.putExtra(TITLE, title);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getIntent().getStringExtra(URL);

        String title = getIntent().getStringExtra(TITLE);
        presenter = new WebViewPresenter(this);
        setContentView(R.layout.activity_web);

        presenter.checkBackButtonNeedHide(url);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (title == null) {
            toolbar.setVisibility(View.GONE);
        } else {
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setTitle(title);
            }
            toolbar.setNavigationOnClickListener(view -> finish());
        }


        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

        findViewById(R.id.home).setOnClickListener(view -> {
            finish();
        });

        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= 21) webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        webView.addJavascriptInterface(new JavaScriptInterface(this), "HtmlCatcher");
        webView.getSettings().setDomStorageEnabled(true);
        webView.clearCache(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (isPause) return;
                progressBar.setVisibility(View.VISIBLE);
                if ( presenter.checkUrl(url)) {
                    view.stopLoading();
                    onBackPressed();
                }
                Log.d("Btest"," onPageStarted");
                Logger.i(TAG, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (isPause) return;
                presenter.checkCookie(url);
//                CookieRepository.getInstance().getCookieJar();
                progressBar.setVisibility(View.GONE);

                webView.loadUrl("javascript:window.HtmlCatcher.showHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                Log.d("Btest"," onPageFinished");
                Logger.i(TAG, url);
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
            }
        });

        insertCookie(url);
        toWebPage(url);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isPause = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPause = true;
    }

    @Override
    protected void onDestroy() {
        webView.destroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onBackClick() {
        finish();
    }

    @Override
    public void toProduct(String itemUrl) {
        if (isPause) return;
        if (url.contains("buy.asp?")) {
            onBackPressed();
            return;
        }
        ProductDetailActivity.startActivity(this, itemUrl);
    }

    @Override
    public void onToSpecialUrl(String url) {
        try {
            Intent intent = Intent.parseUri(url, 0);
            startActivity(intent);
            webView.goBack();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onShowAlert(String msg) {
        ShowAlertActivity.startActivity(this, msg);
    }

    @Override
    public void onToTagWebView(String url) {
        toWebPage(url);
    }

    @Override
    public void onToTagPage(Tag tag, int tagPosition, Tag subTag1, int subTag1Position, Tag subTag2, int subTag2Position) {
        Intent intent = new Intent();
        intent.setAction(ActionName.TAG);
        intent.putExtra(WebExtra.TAG, tag.getTag_id());
        intent.putExtra(WebExtra.URL, url);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onToHomePage(String tagId) {
        Intent intent = new Intent();
        intent.setAction(ActionName.TAG);
        intent.putExtra(WebExtra.TAG, tagId);
        intent.putExtra(WebExtra.URL, url);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackButtonHide() {
        findViewById(R.id.home).setVisibility(View.GONE);
    }

    @Override
    public void toSearch(String keyWord) {
        SearchQueryActivity.startActivity(this, keyWord);
    }

    @Override
    public void onLoginFailure() {
            new MaterialDialog.Builder(this)
                    .content(R.string.login_failure)
                    .positiveText(R.string.confirm)
                    .show();
    }


    private void toWebPage(String url) {
        Uri uri = Uri.parse(url).buildUpon()
                .appendQueryParameter("channel", "app")
                .build();
        webView.loadUrl(Uri.decode(uri.toString()));
    }

    public void insertCookie(String url) {
        String insertCookieUrl = com.crazymike.api.URL.INSERT_COOKIE;
        if (url.contains("partner=")) {
            insertCookieUrl += "&partner=" + url.split("partner=")[1].split("&")[0];
        }
        if (url.contains("affcode=")) {
            insertCookieUrl += "&affcode=" + url.split("affcode=")[1].split("&")[0];
        }
        if (url.contains("gid=")) {
            insertCookieUrl += "&gid=" + url.split("gid=")[1].split("&")[0];
        }
        if (url.contains("mkt=")) {
            insertCookieUrl += "&mkt=" + url.split("mkt=")[1].split("&")[0];
        }
        if (url.contains("mkt3=")) {
            insertCookieUrl += "&mkt3=" + url.split("mkt3=")[1].split("&")[0];
        }
        webView.loadUrl(insertCookieUrl);
    }

    private class JavaScriptInterface {
        private Context context;

        JavaScriptInterface(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        public void showHTML(String html) {
//            javascriptInterfaceHandler.post(() -> {
//                //TODO catch html of the webView
//            });

            presenter.handleHTML(html);
        }
    }
}
