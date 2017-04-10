package com.crazymike.respositories;

import com.crazymike.api.NetworkService;
import com.crazymike.api.response.PromoteResponse;

import rx.Observable;

public class PromoteRepository {

    private static PromoteRepository INSTANCE = null;

    private PromoteRepository() {

    }

    public static PromoteRepository getInstance() {
        return INSTANCE != null ? INSTANCE : (INSTANCE = new PromoteRepository());
    }

    public Observable<PromoteResponse> getPromote(String proId){
        return NetworkService.getInstance().getPromoteApi().callPromote("promote","detail", proId, "");
    }

}
