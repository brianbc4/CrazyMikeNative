package com.crazymike.base;

import android.app.Activity;
import android.content.Context;

import com.crazymike.util.DialogTool;
import com.trello.rxlifecycle.components.support.RxFragment;

public abstract class BaseFragment extends RxFragment {

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
}
