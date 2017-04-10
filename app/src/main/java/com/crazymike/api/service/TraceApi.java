package com.crazymike.api.service;

import com.crazymike.api.response.TraceResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by ChaoJen on 2016/11/18.
 */

public interface TraceApi {
    @GET("/?callfrom=app")
    Observable<TraceResponse> traceClick(
            @Query("func") String func,
            @Query("tfunc") String tfunc,
            @Query("tcode") String tcode,
            @Query("fcode") String fcode);

    @GET("/?callfrom=app")
    Observable<TraceResponse> traceDisplay(
            @Query("func") String func,
            @Query("tfunc") String tfunc,
            @Query("online_id") String online_id,
            @Query("channel") String channel);
}