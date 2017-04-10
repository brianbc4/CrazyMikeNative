package com.crazymike.api.response;

import com.crazymike.models.SWCalendar;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;

/**
 * Created by ChaoJen on 2017/3/21.
 */

@Getter
public class SWCalendarResponse extends BaseResponse {

    @SerializedName("rtn")
    private List<SWCalendar> swCalendarList;
}
