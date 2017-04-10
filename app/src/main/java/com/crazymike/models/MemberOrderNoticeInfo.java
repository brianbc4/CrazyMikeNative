package com.crazymike.models;

import java.util.List;

import lombok.Getter;

/**
 * Created by ChaoJen on 2017/2/16.
 */

@Getter
public class MemberOrderNoticeInfo {

    private List<Integer> items_call;
    private String order_notice_type;
    private String order_notice_send;
}
