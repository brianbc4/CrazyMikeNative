package com.crazymike.util;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle.components.support.RxDialogFragment;
import com.trello.rxlifecycle.components.support.RxFragment;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RxUtil {

    public static <T> Observable.Transformer<T, T> bindLifecycle(Object view) {
        if (view instanceof RxAppCompatActivity) {
            return ((RxAppCompatActivity) view).bindToLifecycle();
        } else if (view instanceof RxDialogFragment) {
            return ((RxDialogFragment) view).bindToLifecycle();
        } else if (view instanceof RxFragment) {
            return ((RxFragment) view).bindToLifecycle();
        } else {
            throw new IllegalArgumentException("not support this bind object, only support fragment v4 and dialogFragment v4");
        }
    }

    public static <T> Observable.Transformer<T, T> mainAsync() {
        return obs -> obs.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
