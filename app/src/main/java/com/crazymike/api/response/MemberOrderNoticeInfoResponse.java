package com.crazymike.api.response;

import com.crazymike.models.MemberOrderNoticeInfo;

import lombok.Getter;

/**
 * Created by ChaoJen on 2017/2/16.
 */

@Getter
public class MemberOrderNoticeInfoResponse {

    private boolean result;
    private String msg;
    private MemberOrderNoticeInfo rtn;
}
