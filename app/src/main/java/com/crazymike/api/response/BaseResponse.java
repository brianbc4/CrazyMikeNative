package com.crazymike.api.response;

import lombok.Getter;

@Getter
public class BaseResponse {

    private boolean result;
    private String msg;
    private String time;
}
