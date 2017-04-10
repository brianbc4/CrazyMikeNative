package com.crazymike.models;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;

/**
 * Created by ChaoJen on 2017/3/21.
 */

@Getter
public class SWCalendar {

    @SerializedName("datekey")
    private String dateKey;
    @SerializedName("calendar_date")
    private String calendarDate;
    @SerializedName("is_deliver_pvr")
    private String isDeliverPvr;
    @SerializedName("is_deliver_whms")
    private String isDeliverWhms;
    @SerializedName("is_cs")
    private String isCs;
    @SerializedName("cs_service_start_time")
    private String csServiceStartTime;
    @SerializedName("cs_service_end_time")
    private String csServiceEdnTime;
}
