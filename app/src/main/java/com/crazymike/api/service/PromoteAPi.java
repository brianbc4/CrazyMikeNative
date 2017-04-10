package com.crazymike.api.service;

import com.crazymike.api.response.PromoteResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface PromoteAPi {

    @GET("/?callfrom=app")
    Observable<PromoteResponse> callPromote(@Query("func") String func, @Query("key") String key, @Query("pro_id") String pro_id, @Query("item_id") String item_id);
}