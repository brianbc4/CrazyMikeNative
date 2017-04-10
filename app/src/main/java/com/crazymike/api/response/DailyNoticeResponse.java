package com.crazymike.api.response;

import com.crazymike.models.DailyNotice;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;

/**
 * Created by user1 on 2017/3/21.
 */
@Getter
public class DailyNoticeResponse extends BaseResponse{
    @SerializedName("rtn")
    private List<DailyNotice> dailyNoticeList;
}
