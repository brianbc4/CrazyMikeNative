package com.crazymike.main;

import android.util.Log;
import android.view.MenuItem;

import com.crazymike.R;
import com.crazymike.respositories.SWCalendarRepository;
import com.crazymike.trace.writer.BaseWriter;
import com.crazymike.trace.writer.GAWriter;
import com.crazymike.models.LeftSideMenu;
import com.crazymike.models.Tag;
import com.crazymike.models.UpSideMenu;
import com.crazymike.respositories.CartRepository;
import com.crazymike.respositories.CookieRepository;
import com.crazymike.respositories.LeftSideMenuRepository;
import com.crazymike.respositories.ProductRepository;
import com.crazymike.respositories.TrackRepository;
import com.crazymike.trace.sender.GASender;
import com.crazymike.trace.sender.ServerLogSender;
import com.crazymike.trace.sender.UserBehaviorSender;
import com.crazymike.util.PreferencesKey;
import com.crazymike.util.PreferencesTool;
import com.crazymike.util.RxUtil;

import java.util.ArrayList;
import java.util.List;

class MainPresenter implements MainContract.Presenter {

    private static final String TAG = MainPresenter.class.getSimpleName();
    private MainContract.View view;
    private boolean isLogin;
    private List<LeftSideMenu> leftSideMenus;

    MainPresenter(MainContract.View view) {
        this.view = view;
        this.isLogin = false;
        CartRepository.getInstance().getCartObservable()
                .compose(RxUtil.bindLifecycle(view))
                .subscribe(view::onGetCart, Throwable::printStackTrace);

        CartRepository.getInstance().getCartCountObservable()
                .compose(RxUtil.bindLifecycle(view))
                .subscribe(view::onGetCartCount, Throwable::printStackTrace);

        CookieRepository.getInstance().getCookieObservable()
                .compose(RxUtil.bindLifecycle(view))
                .compose(RxUtil.mainAsync())
                .subscribe(cookieRepository -> {
                    if (isLogin == cookieRepository.isLogin()) return;
                    view.onLoginStateChange(isLogin = cookieRepository.isLogin());
                }, Throwable::printStackTrace);
    }

    @Override
    public void callAppIndex() {
        ProductRepository.getInstance().callAppIndex("5", "")
                .compose(RxUtil.bindLifecycle(view))
                .compose(RxUtil.mainAsync())
                .subscribe(productRepository -> {

                    view.onGetUpSideMenu(productRepository.getUpSideMenus());
                    view.onGetPromote(productRepository.getPromote());
                    if(ProductRepository.getInstance().hasSpecialLogo()){
                        view.onGetLaunchIcon(productRepository.getChannelInfo().getLogo());
                    }

                }, throwable -> {
                    throwable.printStackTrace();
                    view.onConnectionError();
                });
    }

    @Override
    public void callAppLeftSideMenu(boolean isLogin) {
        LeftSideMenuRepository.getInstance().callAppLeftSideMenu()
                .compose(RxUtil.bindLifecycle(view))
                .compose(RxUtil.mainAsync())
                .subscribe(leftSideMenuRepository -> {
                    List<LeftSideMenu> leftSideMenus = new ArrayList<>();
                    for(int i = 0; i < leftSideMenuRepository.getLeftSideMenus().size(); i++) {
                        LeftSideMenu leftSideMenu = leftSideMenuRepository.getLeftSideMenus().get(i);
                        switch (leftSideMenu.getFunc()) {
                            case 0:
                                leftSideMenus.add(leftSideMenu);
                                break;
                            case 1:
                                if(isLogin) leftSideMenus.add(leftSideMenu);
                                break;
                            case 2:
                                if(!isLogin) leftSideMenus.add(leftSideMenu);
                                break;
                        }
                    }
                    this.leftSideMenus = leftSideMenus;
                    view.onGetLeftSideMenu(leftSideMenus);
                }, throwable -> {
                    throwable.printStackTrace();
                });
    }

    @Override
    public List<UpSideMenu> getUpSideMenus() {
        return ProductRepository.getInstance().getUpSideMenus();
    }

    @Override
    public List<Tag> getTag() {
        return ProductRepository.getInstance().getTags();
    }

    @Override
    public void getCart() {
        CartRepository.getInstance().callCartMap();
    }

    @Override
    public void getTrackList() {
        TrackRepository.getInstance().getTrackList();
    }

    @Override
    public void logout() {
        CookieRepository.getInstance().clearCookies();
        PreferencesTool.getInstance().put(PreferencesKey.MEMBER_ID, "");
        PreferencesTool.getInstance().put(PreferencesKey.LOGIN_TYPE, "");
        PreferencesTool.getInstance().put(PreferencesKey.LOGIN_USER, "");
        getCart();
        getTrackList();
        view.onLoginStateChange(false);
    }

    @Override
    public void sendUserBehaviorTrace(int position) {
        new UserBehaviorSender().sendPageChanged(position);
    }

    @Override
    public void sendServerLog(String url, String tagId) {
        ServerLogSender.send(url , BaseWriter.getTargetWithTag(tagId));
    }

    @Override
    public void sendGA(String url, int position) {
        GASender.sendScreenView(new GAWriter(url).getTagMessage(position));
    }

    @Override
    public boolean isLogin() {
        return CookieRepository.getInstance().isLogin();
    }

    @Override
    public void checkTagName(String tagName) {

        ProductRepository.getInstance().getSubTagByName(tagName, new ProductRepository.OnGetSubTagCallback() {

            @Override
            public void getSubTag(Tag tag, int tagPosition, Tag subTag1, int subTag1Position, Tag subTag2, int subTag2Position) {
                view.onGetTag(tag, tagPosition, subTag1, subTag1Position, subTag2, subTag2Position);
            }

            @Override
            public void getNothing() {
                view.startSearchQuery(tagName);
            }
        });
    }

    @Override
    public void checkTagId(String tagId) {

        ProductRepository.getInstance().getSubTag(tagId, new ProductRepository.OnGetSubTagCallback() {

            @Override
            public void getSubTag(Tag tag, int tagPosition, Tag subTag1, int subTag1Position, Tag subTag2, int subTag2Position) {
                view.onGetTag(tag, tagPosition, subTag1, subTag1Position, subTag2, subTag2Position);
            }

            @Override
            public void getNothing() {
                view.onShowSpecialTagInHomePage(tagId);
            }
        });
    }

    @Override
    public void getDailyNotification(String day) {
        if(!day.equals(PreferencesTool.getInstance().get(PreferencesKey.TODAY,String.class))) {
            PreferencesTool.getInstance().put(PreferencesKey.TODAY, day);
            view.showDailyNotification();
        }
    }

    @Override
    public String getLeftSideMenuUrl(MenuItem item) {
        return leftSideMenus.get(item.getItemId()).getUrl();
    }

    public List<Integer> getTagImage(){
        List<Integer> tagImage = new ArrayList<Integer>();
        tagImage.add(R.mipmap.ic_tag_car);
        tagImage.add(R.mipmap.ic_tag_mobile);
        tagImage.add(R.mipmap.ic_tag_appliances);
        tagImage.add(R.mipmap.ic_tag_living_goods);
        tagImage.add(R.mipmap.ic_tag_fashion);
        tagImage.add(R.mipmap.ic_tag_digital_devices);
        tagImage.add(R.mipmap.ic_tag_cosmetics_health);
        tagImage.add(R.mipmap.ic_tag_necessities_essentials);
        tagImage.add(R.mipmap.ic_tag_food);
        tagImage.add(R.mipmap.ic_tag_mother_and_baby);
        tagImage.add(R.mipmap.ic_tag_hardware_tools);
        tagImage.add(R.mipmap.ic_tag_perfect);
        tagImage.add(R.mipmap.ic_tag_online_convienience_store);
        tagImage.add(R.mipmap.ic_tag_travel);
        return tagImage;
    }
}
