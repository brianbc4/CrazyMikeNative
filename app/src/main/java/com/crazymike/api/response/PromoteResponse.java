package com.crazymike.api.response;

import com.crazymike.models.General;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;

@Getter
public class PromoteResponse extends BaseResponse{

    @SerializedName("rtn")
    private List<General> general;
}
