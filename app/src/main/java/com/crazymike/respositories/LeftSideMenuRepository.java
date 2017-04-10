package com.crazymike.respositories;

import com.crazymike.api.FUNC;
import com.crazymike.api.NetworkService;
import com.crazymike.models.LeftSideMenu;

import java.util.List;

import lombok.Getter;
import rx.Observable;

/**
 * Created by ChaoJen on 2016/12/8.
 */

@Getter
public class LeftSideMenuRepository {

    private static LeftSideMenuRepository INSTANCE = new LeftSideMenuRepository();

    private List<LeftSideMenu> leftSideMenus;

    public LeftSideMenuRepository() {

    }

    public static LeftSideMenuRepository getInstance() {
        return INSTANCE;
    }

    public Observable<LeftSideMenuRepository> callAppLeftSideMenu() {
        return NetworkService.getInstance().getProductApi().callAppLeftSideMenu(FUNC.APP_LEFT_SIDE_MENU)
                .map(appLeftSideMenuResponse -> {
                    this.leftSideMenus = appLeftSideMenuResponse.getLeftSideMenus();
                    return this;
                });
    }
}
