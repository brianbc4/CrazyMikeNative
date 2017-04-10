package com.crazymike.api.service;

import com.crazymike.api.response.DailyNoticeResponse;
import com.crazymike.models.DailyNotice;

import lombok.Getter;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by user1 on 2017/3/20.
 */

public interface DailyNoticeApi {
    @GET("/?callfrom=app")
    Observable<DailyNoticeResponse> callDailyNotice(@Query("func")String fun, @Query("id")String id,@Query("platform")String plateform,@Query("notice_style")String style);
}
