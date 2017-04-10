package com.crazymike.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.inputmethod.InputMethodManager;

import com.crazymike.util.DialogTool;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

public  class BaseActivity extends RxAppCompatActivity {

    protected DialogTool dialogTool;
    private InputMethodManager imm ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialogTool = new DialogTool(this);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public void hideSoftKeyboard(){
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    public void showSoftKeyboard(){
        imm.showSoftInputFromInputMethod(getWindow().getDecorView().getWindowToken(), 0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
