package com.crazymike.api.response;

import com.crazymike.models.UpSideMenu;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;

@Getter
public class AppUpSideMenuResponse extends BaseResponse{

    /**
     * name : 團購首頁
     * url : https://m.crazymike.tw
     */

    @SerializedName("rtn")
    private List<UpSideMenu> UpSideMenu;

}
