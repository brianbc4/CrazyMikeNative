package com.crazymike.api.response;

import com.crazymike.models.LeftSideMenu;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;

/**
 * Created by ChaoJen on 2016/12/8.
 */

@Getter
public class AppLeftSideMenuResponse extends BaseResponse{

    @SerializedName("rtn")
    private List<LeftSideMenu> leftSideMenus;
}
