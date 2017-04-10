package com.crazymike.product.detail;

import android.net.Uri;
import android.util.Log;

import com.crazymike.api.NetworkService;
import com.crazymike.api.URL;
import com.crazymike.respositories.OrderNoticeRepository;
import com.crazymike.respositories.SWCalendarRepository;
import com.crazymike.trace.writer.BaseWriter;
import com.crazymike.trace.writer.GAWriter;
import com.crazymike.models.General;
import com.crazymike.models.ItemDetail;
import com.crazymike.models.ItemList;
import com.crazymike.respositories.CartRepository;
import com.crazymike.respositories.CookieRepository;
import com.crazymike.respositories.ProductRepository;
import com.crazymike.respositories.PromoteRepository;
import com.crazymike.respositories.TrackRepository;
import com.crazymike.trace.sender.GASender;
import com.crazymike.trace.sender.ServerLogSender;
import com.crazymike.trace.sender.UserBehaviorSender;
import com.crazymike.util.PreferencesKey;
import com.crazymike.util.PreferencesTool;
import com.crazymike.util.RxUtil;
import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;

class ProductDetailPresenter implements ProductDetailContract.Presenter {

    private static final String TAG = ProductDetailPresenter.class.getSimpleName();
    private ProductDetailContract.View view;
    private Subscription timeSubscription;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:ss:mm", Locale.TAIWAN);
    private int page = 0;
    private ItemDetail itemDetail;
    private List<ItemList> itemLists = new ArrayList<>();
    private String mStrMemberOrderNoticeType = "";
    private String mStrMemberOrderNoticeSend = "";
    private String mCSServiceStartTime;
    private String mCSServiceEndTime;


    public ProductDetailPresenter(ProductDetailContract.View view) {
        this.view = view;
        CartRepository.getInstance().getCartObservable()
                .compose(RxUtil.bindLifecycle(view))
                .subscribe(view::onGetCart, Throwable::printStackTrace);

        CartRepository.getInstance().getCartCountObservable()
                .compose(RxUtil.bindLifecycle(view))
                .subscribe(view::onGetCartCount, Throwable::printStackTrace);

        TrackRepository.getInstance().getTrackAddOrDelObservable()
                .compose(RxUtil.bindLifecycle(view))
                .subscribe(view::onTrackChanged, Throwable::printStackTrace);

        SWCalendarRepository.getInstance().callSWCalendar()
                .compose(RxUtil.bindLifecycle(view))
                .subscribe(swCalendarRepository -> {
                    mCSServiceStartTime = swCalendarRepository.getCsServiceStartTime();
                    mCSServiceEndTime = swCalendarRepository.getCsServiceEndTime();
                }, Throwable::printStackTrace);
    }

    @Override
    public void getItemDetail(String itemId) {
        view.showProgress();
        ProductRepository.getInstance().getItemDetail(itemId)
                .compose(RxUtil.mainAsync())
                .compose(RxUtil.bindLifecycle(view))
                .doAfterTerminate(() -> view.hideProgress())
                .subscribe(itemDetail -> {
                    this.itemDetail = itemDetail;
                    view.onGetItemDetail(itemDetail);
                    new UserBehaviorSender().sendItemDetailDisplay(itemDetail);
                }, throwable -> view.onGetItemDetailError(throwable));
    }


    @Override
    public void getPromote(String itemId) {
        PromoteRepository.getInstance().getPromote(itemId)
                .compose(RxUtil.mainAsync())
                .compose(RxUtil.bindLifecycle(view))
                .subscribe(general -> {

//                    if (general != null) view.onGetPromote(general);


                    General g = new General();
                    g.setIsMember(true);
                    g.setDiscType(1);

                    g.setDiscPromote("88");

                    List<String> discCash = new ArrayList<>();
                    discCash.add("可折抵50元");
                    discCash.add("滿500元時");
                    g.setDiscCash(discCash);

                    List<String> password = new ArrayList<>();
                    password.add("Cuber5566");
                    g.setPassword(password);

                    List<General.Tag> tags = new ArrayList<>();
                    tags.add(new General.Tag("限3C周邊", true));
                    tags.add(new General.Tag("限行動賣客", true));
                    tags.add(new General.Tag("部分商品除外", true));
                    g.setTag(tags);

                    view.onGetPromote(g);

                    g.setDateOffline("2016/10/31");

                }, throwable -> view.onGetPromoteError(throwable));
    }

    @Override
    public void getCart() {
        CartRepository.getInstance().callCartMap();
    }

    @Override
    public String getBuyNewUrl(String itemId) {
        Uri uri;
        if (PreferencesTool.getInstance().get(PreferencesKey.IS_DEVEL, Boolean.class)) {
             uri = new Uri.Builder()
                    .path(URL.DEVELBUY)
                    .appendQueryParameter("item_id", itemId)
                    .build();
        } else {
             uri = new Uri.Builder()
                    .path(URL.BUY)
                    .appendQueryParameter("item_id", itemId)
                    .build();
        }


        return uri.toString();
    }

    @Override
    public boolean isLogin() {
        return CookieRepository.getInstance().isLogin();
    }

    @Override
    public boolean isTrack(String itemId) {
        return TrackRepository.getInstance().isIn(itemId);
    }

    @Override
    public void addTrack(String itemId) {
        if (TrackRepository.getInstance().isIn(itemId)) {
            TrackRepository.getInstance().delTrack(itemId);
        } else {
            TrackRepository.getInstance().addTrack(itemId);
        }
    }

    @Override
    public void buy(ItemDetail itemDetail) {

    }

    @Override
    public void countDown(String date_offline) {

        long sec = getTime(date_offline);
        long now = new Date().getTime();
        timeSubscription = Observable.interval(1, TimeUnit.SECONDS)
                .compose(RxUtil.mainAsync())
                .compose(RxLifecycle.bindUntilEvent(((RxAppCompatActivity) view).lifecycle(), ActivityEvent.CREATE))
                .map(time -> ((sec - now) / 1000) - time)
                .subscribe(time -> {
                    if (time < 1) stopTimer();
                    view.onTimerCountDown(time.intValue());
                }, Throwable::printStackTrace);
    }

    @Override
    public void stopTimer() {
        if (timeSubscription == null) return;
        timeSubscription.unsubscribe();
        timeSubscription = null;
        view.onTimerCountDown(0);
    }

    @Override
    public void getItemList() {
        view.showProgress();
        ProductRepository.getInstance().callItemListByChannel(itemDetail.getInfo().getChannel_id(), page)
                .compose(RxUtil.mainAsync())
                .compose(RxUtil.bindLifecycle(view))
                .doAfterTerminate(() -> view.hideProgress())
                .subscribe(this::handleItemListResponse, Throwable::printStackTrace);
    }

    @Override
    public void handleItemListResponse(List<ItemList> items) {
        itemLists.addAll(items);
        view.onGetItemList(itemLists);
    }

    @Override
    public void onScrollBottom() {
        page++;
        getItemList();
    }

    @Override
    public void sendServerLog(String itemId, String url) {
        ServerLogSender.send(url, BaseWriter.getTargetWithItem(itemId));
    }

    @Override
    public void sendGA(String url, String itemId) {
        GASender.sendScreenView(new GAWriter(url).getItemMessage(itemId));
    }

    @Override
    public boolean hasSpecialLogo() {
        return ProductRepository.getInstance().hasSpecialLogo();
    }

    @Override
    public String getLogo() {
        return ProductRepository.getInstance().getChannelInfo().getLogo();
    }

    @Override
    public boolean isTravelTag(String tagId) {
        return ProductRepository.getInstance().checkIsTravelTag(tagId);
    }

    @Override
    public void checkOrderNotice() {
        String strLoginType = PreferencesTool.getInstance().get(PreferencesKey.LOGIN_TYPE, String.class);
        String strLoginUser = PreferencesTool.getInstance().get(PreferencesKey.LOGIN_USER, String.class);

        if (strLoginType.length() != 0 && strLoginUser.length() != 0) {
            OrderNoticeRepository.getInstance().callMemberOrderNoticeInfo(strLoginType, strLoginUser)
                    .compose(RxUtil.mainAsync())
                    .subscribe(memberOrderNoticeInfo -> {
                        mStrMemberOrderNoticeType = memberOrderNoticeInfo.getOrder_notice_type();
                        mStrMemberOrderNoticeSend = memberOrderNoticeInfo.getOrder_notice_send();

                        List<Integer> itemCallList = memberOrderNoticeInfo.getItems_call();
                        for (int itemId : itemCallList) {
                            if (String.valueOf(itemId).equals(itemDetail.getInfo().getItem_id())) {
                                view.onOrderNoticeChecked(mStrMemberOrderNoticeType.length() != 0 && mStrMemberOrderNoticeSend.length() != 0);
                                return;
                            }
                        }
                        view.onOrderNoticeChecked(false);
                    }, throwable -> {
                        throwable.printStackTrace();
                    });
        } else {
            view.onOrderNoticeChecked(false);
        }
    }

    @Override
    public void getOrderNoticeRequestUrl() {
        view.onOrderNoticeRequestUrlGet(
                mStrMemberOrderNoticeType.length() != 0 && mStrMemberOrderNoticeSend.length() != 0,
                getOrderNoticeRequestUrl(mStrMemberOrderNoticeType, mStrMemberOrderNoticeSend));
    }

    @Override
    public void getOrderNoticeRequestUrlOnce(String orderNoticeType, String orderNoticeSend) {
        view.onOrderNoticeRequestUrlGet(true, getOrderNoticeRequestUrl(orderNoticeType, orderNoticeSend));
    }

    @Override
    public String getCSServiceStartTime() {
        return mCSServiceStartTime;
    }

    @Override
    public String getCSServiceEndTime() {
        return mCSServiceEndTime;
    }

    @Override
    public void postCSServiceMessage(String name, String title, String phoneNumber, String email, String content) {
        NetworkService.getInstance().getProductApi().postCSServiceMessage("31", itemDetail.getInfo().getDeliver_from(), "true", "consultation", title, name, phoneNumber, email, content)
                .compose(RxUtil.mainAsync())
                .subscribe(csServiceMessageResponse -> {
                    view.onCSServiceMessageResponseGet(!Boolean.valueOf(csServiceMessageResponse.getIsError()), csServiceMessageResponse.getMessage());
                }, throwable -> {
                    Log.e(TAG, throwable.toString());
                });
    }

    public String getItemName() {
        return itemDetail.getInfo().getName();
    }

    private long getTime(String date) {
        try {
            return format.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private String getOrderNoticeRequestUrl(String orderNoticeType, String orderNoticeSend) {
        String orderNoticeRequestUrl = "https://m2.crazymike.tw//ajax-order_notice_log/?item=" + itemDetail.getInfo().getItem_id() + "&type=" + orderNoticeType + "&send=" + orderNoticeSend;
        return orderNoticeRequestUrl;
    }
}
