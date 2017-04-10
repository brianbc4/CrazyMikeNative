package com.crazymike.listener;

import android.view.View;
import android.widget.AdapterView;

import rx.functions.Action1;

public class OnItemSelectedListener implements AdapterView.OnItemSelectedListener{

    private Action1<Integer> onItemSelected;
    private boolean isFirstSet = true;

    public OnItemSelectedListener(Action1<Integer> onItemSelected) {
        this.onItemSelected = onItemSelected;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(isFirstSet){
            isFirstSet = false;
            return;
        }
        onItemSelected.call(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
