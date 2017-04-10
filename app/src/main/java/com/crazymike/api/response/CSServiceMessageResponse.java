package com.crazymike.api.response;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;

/**
 * Created by ChaoJen on 2017/3/22.
 */

@Getter
public class CSServiceMessageResponse {
    @SerializedName("isErr")
    private String isError;
    @SerializedName("msg")
    private String message;
}
