package com.crazymike.api.service;

import com.crazymike.api.URL;
import com.crazymike.api.response.AppIndexResponse;
import com.crazymike.api.response.AppLeftSideMenuResponse;
import com.crazymike.api.response.AppUpSideMenuResponse;
import com.crazymike.api.response.BannerResponse;
import com.crazymike.api.response.CSServiceMessageResponse;
import com.crazymike.api.response.ItemDetailResponse;
import com.crazymike.api.response.ItemListResponse;
import com.crazymike.api.response.MemberOrderNoticeInfoResponse;
import com.crazymike.api.response.SWCalendarResponse;
import com.crazymike.api.response.SearchQueryResponse;
import com.crazymike.api.response.TagResponse;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface Api {

    @GET("/?callfrom=app")
    Observable<BannerResponse> callBanner(@Query("func") String fun, @Query("code") String bannerCode);

    @GET("/?callfrom=app")
    Observable<AppIndexResponse> callAppIndex(@Query("func") String fun, @Query("channel") String channel, @Query("platform") String platform, @Query("tag") String tag, @Query("ver") int ver);

    @GET("/?callfrom=app")
    Observable<ItemListResponse> callItemListByChannel(@Query("func") String fun, @Query("channel") String channel,
                                                       @Query("page") int page, @Query("disp") String disp, @Query("ver") String ver);

    @GET("/?callfrom=app")
    Observable<ItemListResponse> callItemListByTag(@Query("func") String fun, @Query("tag") String tag,
                                                   @Query("page") int page, @Query("disp") String disp, @Query("ver") String ver);

    @GET("/?callfrom=app")
    Observable<TagResponse> callTag(@Query("func") String fun, @Query("tags") String tag);

    @GET("/?callfrom=app")
    Observable<TagResponse> callChildTag(@Query("func") String fun, @Query("parent") String tag);

    @GET("/?callfrom=app")
    Observable<AppUpSideMenuResponse> callAppUpSideMenu(@Query("func") String fun);

    @GET("/?callfrom=app")
    Observable<ItemDetailResponse> callItemDetail(@Query("func") String fun, @Query("item") String itemId, @Query("ver") String ver);

    @GET(URL.SEARCH)
    Observable<ItemDetailResponse> search(@Query("w") String fun);

    @GET("/?callfrom=app")
    Observable<SearchQueryResponse> searchQuery(@Query("func") String func, @Query("key") String key, @Query("page_index") int page_index, @Query("page_size") int page_size, @Query("ver") String ver, @Query("orderby_id") String orderby_id, @Query("orderby_sort") int orderby_sort);

    @GET("/?callfrom=app")
    Observable<AppLeftSideMenuResponse> callAppLeftSideMenu(@Query("func") String func);

    @GET(URL.MEMBER_ORDER_NOTICE_INFO)
    Observable<MemberOrderNoticeInfoResponse> callMemberOrderNoticeInfo(@Query("C_type") String loginType, @Query("user") String loginUser);

    @GET("/?callfrom=app")
    Observable<SWCalendarResponse> callSWCalendar(@Query("func") String func, @Query("gtype") String gtype, @Query("datekey") String datekey);

    @Headers({
            "Accept: application/json",
            "Charset: UTF-8"
    })
    @FormUrlEncoded
    @POST(URL.POST_CS_SERVICE_MESSAGE)
    Observable<CSServiceMessageResponse> postCSServiceMessage(
            @Field("contact_type") String contactType,
            @Field("item_deliver_from") String itemDeliverFrom,
            @Field("json_return") String jsonReturn,
            @Field("cfrom") String cFrom,
            @Field("title") String title,
            @Field("name") String name,
            @Field("tel") String tel,
            @Field("email") String email,
            @Field("content") String content
    );
}
