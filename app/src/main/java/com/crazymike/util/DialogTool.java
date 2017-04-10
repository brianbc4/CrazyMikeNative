package com.crazymike.util;

import android.content.Context;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.crazymike.R;

public class DialogTool {

    private Context context;
    private MaterialDialog progressDialog;

    public DialogTool(Context context) {
        this.context = context;
    }

    public void showProgressDialog() {
        showProgressDialog(R.string.progress_dialog, R.string.please_wait);
    }

    public void showProgressDialog(int title, int content) {
        showProgressDialog(
                context.getResources().getString(title),
                context.getResources().getString(content));
    }

    public void showProgressDialog(String title, String content) {

        if (progressDialog == null)

            progressDialog = new MaterialDialog.Builder(context)
                    .title(title)
                    .content(content)
                    .progress(true, 0)
                    .cancelable(false)
                    .show();
        else
            progressDialog.show();
    }

    public void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public void showHintDialog(int content) {
        showHintDialog(context.getString(content));
    }

    public void showHintDialog(String content) {

        new MaterialDialog.Builder(context)
                .content(content)
                .positiveText(context.getString(R.string.confirm))
                .show();
    }

    public void showErrorMessage(Throwable throwable) {
        throwable.printStackTrace();

        if (progressDialog != null) {
            progressDialog.dismiss();
        }

        if (Util.checkNetworkState(throwable)) {
            Toast.makeText(context, R.string.dialog_connection_content, Toast.LENGTH_SHORT).show();
            return;
        }

        new MaterialDialog.Builder(context)
                .title(R.string.error)
                .content(throwable.getLocalizedMessage())
                .positiveText(R.string.close)
                .show();
    }
}
