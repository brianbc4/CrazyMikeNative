package com.crazymike.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.crazymike.R;
import com.crazymike.api.URL;
import com.crazymike.base.BaseActivity;
import com.crazymike.web.WebRule;

public class LoginActivity extends BaseActivity implements LoginContract.View {

    private static final String TAG = "LoginActivity";

    private WebView webView;
    private LoginPresenter presenter;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new LoginPresenter(this);

        setContentView(R.layout.login_activity);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAppCacheEnabled(false);
        webView.addJavascriptInterface(new LoginJavaScriptInterface(), "HTMLOUT");
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (WebRule.isClickBack(url)) onBackPressed();
                Log.i(TAG, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                webView.loadUrl(WebRule.getShowHtmlJS());
                progressBar.setVisibility(View.GONE);
                Log.i(TAG, url);
            }
        });
        webView.loadUrl(URL.LOGIN);
    }

    @Override
    protected void onDestroy() {
        webView.destroy();
        super.onDestroy();
    }

    @Override
    public void onLoginSuccess(String memberId, String type, String user) {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onLoginFailure() {
        new MaterialDialog.Builder(this)
                .content(R.string.login_failure)
                .positiveText(R.string.confirm)
                .show();
    }

    class LoginJavaScriptInterface {
        @JavascriptInterface
        public void showHTML(String html) {
            presenter.handleHTML(html);
        }
    }
}
