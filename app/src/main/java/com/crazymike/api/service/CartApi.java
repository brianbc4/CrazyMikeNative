package com.crazymike.api.service;

import com.crazymike.api.URL;
import com.crazymike.api.response.CartsResponse;
import com.crazymike.models.Cart;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface CartApi {

    @GET("/?callfrom=app")
    Observable<CartsResponse> callCarts(@Query("func") String func, @Query("opt") String opt, @Query("C_type") String C_type,
                                        @Query("user") String user, @Query("carts") String carts, @Query("item") String item);

    @GET(URL.GET_CART_LIST)
    Observable<Map<String, Cart>> getCartList();

    @GET(URL.ADD_CART)
    Observable<String> addCart(@Query("item_id") String itemId, @Query("qty") int qty, @Query("specs") String jsonSpecList);
}
