package com.crazymike.api.service;

import com.crazymike.api.response.TrackResponse;
import com.crazymike.api.response.appapi2.TrackListResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface TrackApi {

    @GET("/?callfrom=app")
    Observable<TrackResponse> track(@Query("func") String func, @Query("C_type") String C_type, @Query("user") String user, @Query("tfunc") String tfunc, @Query("item_id") String item_id);

    @GET("/?callfrom=app")
    Observable<TrackListResponse> trackList(@Query("func") String func, @Query("C_type") String C_type, @Query("user") String user, @Query("tfunc") String tfunc);
}
