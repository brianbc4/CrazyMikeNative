package com.crazymike.respositories;

import com.crazymike.api.NetworkService;
import com.crazymike.models.Cart;
import com.crazymike.models.ItemList;
import com.crazymike.util.RxUtil;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;

public class CartRepository {

    public static final String TAG = "CartRepository";
    private static CartRepository INSTANCE = null;
    private Observable<Map<String, Cart>> cartChangeObservable;
    private Subscriber<? super Map<String, Cart>> cartChangeSubscriber;

    private Observable<Integer> cartCountChangeObservable;
    private Subscriber<? super Integer> cartCountChangeSubscriber;

    private Map<String, Cart> cartMap;

    private CartRepository() {
        cartMap = new HashMap<>();
    }

    public static CartRepository getInstance() {
        return INSTANCE != null ? INSTANCE : (INSTANCE = new CartRepository());
    }

    public Map<String, Cart> getCartMap() {
        return cartMap;
    }

    public Observable<Map<String, Cart>> getCartObservable() {
        if (cartChangeObservable == null) {
            cartChangeObservable = Observable.create((Observable.OnSubscribe<Map<String, Cart>>) subscriber -> cartChangeSubscriber = subscriber).share();
        }
        return cartChangeObservable;
    }

    public Observable<Integer> getCartCountObservable() {
        if (cartCountChangeObservable == null) {
            cartCountChangeObservable = Observable.create((Observable.OnSubscribe<Integer>) subscriber -> cartCountChangeSubscriber = subscriber).share();
        }
        return cartCountChangeObservable;
    }

    public void addCart(ItemList itemList, int qty, String jsonArraySpec) {
        addCart(itemList.getItem_id(), qty, jsonArraySpec);
    }

    public void addCart(String itemId, int qty, String jsonSpecList) {
        NetworkService.getInstance().getCartApi().addCart(itemId, qty, jsonSpecList)
                .compose(RxUtil.mainAsync())
                .subscribe(s -> {
                    cartCountChangeSubscriber.onNext(Integer.valueOf(s));
                }, throwable -> cartCountChangeSubscriber.onError(throwable));
    }

    public void callCartMap() {
        NetworkService.getInstance().getCartApi().getCartList()
                .compose(RxUtil.mainAsync())
                .subscribe(cartMap -> {
                    if (cartMap == null) this.cartMap = new HashMap<>();
                    else this.cartMap = cartMap;
                    if (cartChangeSubscriber != null) cartChangeSubscriber.onNext(this.cartMap);
                }, throwable -> cartChangeSubscriber.onError(throwable));
    }
}
