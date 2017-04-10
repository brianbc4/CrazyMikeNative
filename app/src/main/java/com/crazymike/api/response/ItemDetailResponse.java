package com.crazymike.api.response;

import com.crazymike.models.ItemDetail;

import lombok.Getter;

@Getter
public class ItemDetailResponse extends BaseResponse{

    private ItemDetail rtn;
}
