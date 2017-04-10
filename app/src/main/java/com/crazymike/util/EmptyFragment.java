package com.crazymike.util;

import android.os.Bundle;

import com.crazymike.base.BaseFragment;

public class EmptyFragment extends BaseFragment{

    public static EmptyFragment newInstance(){
        EmptyFragment fragment = new EmptyFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }
}
