package com.crazymike.search.box;


import com.crazymike.models.SearchHotKey;
import com.crazymike.respositories.ProductRepository;

import java.util.List;

public class SearchPresenter implements SearchContract.Presenter{

    private SearchContract.View view;

    SearchPresenter(SearchContract.View view) {
        this.view = view;
    }

    @Override
    public List<SearchHotKey> getSearchHotKeyList() {
        return ProductRepository.getInstance().getSearchHotKeys();
    }
}
