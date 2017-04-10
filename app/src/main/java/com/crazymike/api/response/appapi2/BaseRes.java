package com.crazymike.api.response.appapi2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaseRes {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;

    public String getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }
}
