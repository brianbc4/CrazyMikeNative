package com.crazymike.search.box;


import com.crazymike.models.SearchHotKey;

import java.util.List;

public interface SearchContract {

    interface View{

        void onGetSearchKey(String searchKey);
    }

    interface Presenter{

        List<SearchHotKey> getSearchHotKeyList();
    }
}
