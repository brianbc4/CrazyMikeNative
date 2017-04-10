package com.crazymike.respositories;

import com.crazymike.api.NetworkService;
import com.crazymike.api.response.MemberOrderNoticeInfoResponse;
import com.crazymike.models.MemberOrderNoticeInfo;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by ChaoJen on 2017/2/17.
 */

public class OrderNoticeRepository {

    private static OrderNoticeRepository instance = new OrderNoticeRepository();
    public static final String ORDER_NOTICE_TYPE_MAIL = "email";
    public static final String ORDER_NOTICE_TYPE_PHONE = "phone";

    public static OrderNoticeRepository getInstance() {
        return instance;
    }

    public OrderNoticeRepository() {

    }

    public Observable<MemberOrderNoticeInfo> callMemberOrderNoticeInfo(String strLoginType, String strLoginUser) {
        return NetworkService.getInstance().getProductApi().callMemberOrderNoticeInfo(strLoginType, strLoginUser)
                .map(memberOrderNoticeInfoResponse -> {
                    return memberOrderNoticeInfoResponse.getRtn();
                });
    }
}
