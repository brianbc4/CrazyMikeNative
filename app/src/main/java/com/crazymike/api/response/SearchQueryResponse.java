package com.crazymike.api.response;

import com.crazymike.models.ItemList;

import java.util.List;

import lombok.Getter;

/**
 * Created by cuber on 2016/11/8.
 */

@Getter
public class SearchQueryResponse extends BaseResponse{

    private Rtn rtn;

    @Getter
    public static class Rtn {
        private int total;
        private List<ItemList> html;
    }
}
