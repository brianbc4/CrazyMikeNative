package com.crazymike.respositories;

import com.crazymike.models.Spec;
import com.crazymike.models.SpecCounter;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by ChaoJen on 2017/3/1.
 */

public class SpecCounterRepository {

    public static SpecCounterRepository mInstance = new SpecCounterRepository();
    private List<SpecCounter> mSpecList;
    private Observable<List<SpecCounter>> specCountChangeObservable;
    private Subscriber<? super List<SpecCounter>> specCountChangeSubscriber;

    public static SpecCounterRepository getInstance() {
        return mInstance;
    }

    public SpecCounterRepository() {
        mSpecList = new ArrayList<>();
    }

    public void increaseSpecCount(Spec spec) {
        for (int i = 0; i < mSpecList.size(); i++) {
            if (mSpecList.get(i).getSpec().getSpec_id().equals(spec.getSpec_id())) {
                if (Integer.valueOf(mSpecList.get(i).getSpec().getSpec_stock()) > mSpecList.get(i).getQty()) {
                    mSpecList.get(i).setQty(mSpecList.get(i).getQty() + 1);
                }
                if (specCountChangeSubscriber != null) specCountChangeSubscriber.onNext(mSpecList);
                return;
            }
        }
        mSpecList.add(new SpecCounter(spec, 1));
        if (specCountChangeSubscriber != null) specCountChangeSubscriber.onNext(mSpecList);
    }

    public void decreaseSpecCount(Spec spec) {
        for (int i = 0; i < mSpecList.size(); i++) {
            if (mSpecList.get(i).getSpec().getSpec_id().equals(spec.getSpec_id())) {
                mSpecList.get(i).setQty(mSpecList.get(i).getQty() - 1);

                if (mSpecList.get(i).getQty() == 0) mSpecList.remove(i);
            }
        }
        if (specCountChangeSubscriber != null) specCountChangeSubscriber.onNext(mSpecList);
    }

    public List<SpecCounter> getSpecCounterList() {
        return mSpecList;
    }

    public void clearSpecList() {
        mSpecList.clear();
    }

    public int getProductCount() {
        int productCount = 0;
        for (SpecCounter specCounter : mSpecList) {
            productCount += specCounter.getQty();
        }
        return productCount;
    }

    public Observable<List<SpecCounter>> getSpecCounterListChangedObservable() {
        if (specCountChangeObservable == null) {
            specCountChangeObservable = Observable.create((Observable.OnSubscribe<List<SpecCounter>>) subscriber -> specCountChangeSubscriber = subscriber).share();
        }
        return specCountChangeObservable;
    }
}
