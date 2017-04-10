package com.crazymike.base;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.crazymike.R;
import com.crazymike.util.DialogTool;
import com.trello.rxlifecycle.components.support.RxDialogFragment;

public class BaseDialogFragment extends RxDialogFragment{

    protected Activity activity;
    protected Context context;
    protected DialogTool dialogTool;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        dialogTool = new DialogTool(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogFragmentTheme);
    }
}
