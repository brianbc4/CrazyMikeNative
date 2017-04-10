package com.crazymike.product.spec.selector;

import android.util.Log;

import com.crazymike.api.NetworkService;
import com.crazymike.models.ItemDetail;
import com.crazymike.respositories.ProductRepository;
import com.crazymike.respositories.SWCalendarRepository;
import com.crazymike.util.RxUtil;

/**
 * Created by Elliot on 2017/3/27.
 */

public class SpecSelectorPresenter
        implements SpecSelectorContract.Presenter {

    private static final String TAG = SpecSelectorPresenter.class.getSimpleName();

    private SpecSelectorContract.View view;
    private String csServiceStartTime;
    private String csServiceEndTime;
    private ItemDetail itemDetail;

    public SpecSelectorPresenter(SpecSelectorContract.View view, ItemDetail itemDetail) {
        this.view = view;
        this.itemDetail = itemDetail;
        SWCalendarRepository.getInstance().callSWCalendar()
                .compose(RxUtil.bindLifecycle(view))
                .subscribe(swCalendarRepository -> {
                    csServiceStartTime = swCalendarRepository.getCsServiceStartTime();
                    csServiceEndTime = swCalendarRepository.getCsServiceEndTime();
                }, Throwable::printStackTrace);
    }

    @Override
    public boolean isTravelProduct(String tagId) {
        return  ProductRepository.getInstance().checkIsTravelTag(tagId);
    }

    @Override
    public String getCSServiceStartTime() {
        return csServiceStartTime;
    }

    @Override
    public String getCSServiceEndTime() {
        return csServiceEndTime;
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
}
