package com.crazymike.trace.sender;

import com.crazymike.api.FUNC;
import com.crazymike.api.NetworkService;
import com.crazymike.api.TFUNC;
import com.crazymike.models.Banner;
import com.crazymike.models.ItemDetail;
import com.crazymike.models.ItemList;
import com.crazymike.models.RotateJson;
import com.crazymike.trace.writer.TraceQueryWriter;
import com.crazymike.util.RxUtil;

/**
 * Created by ChaoJen on 2016/11/18.
 */

public class UserBehaviorSender {

    public void sendPageChanged(int position) {
        NetworkService.getInstance().getTraceApi().traceClick(
                FUNC.TRACE,
                TFUNC.TRACE,
                TraceQueryWriter.getTagTcode(position),
                TraceQueryWriter.getTagFcode())
                .compose(RxUtil.mainAsync())
                .subscribe(traceResponse -> {}, Throwable::printStackTrace);
    }

    //trace user click MLinkBanner, but the RotateJson of MLinkBanner has no tcode
    public void sendBannerClick(Banner banner, RotateJson rotateJson) {
        NetworkService.getInstance().getTraceApi().traceClick(
                FUNC.TRACE,
                TFUNC.TRACE,
                TraceQueryWriter.getBannerTcode(banner, rotateJson),
                TraceQueryWriter.getBannerFcode(banner))
                    .compose(RxUtil.mainAsync())
                    .subscribe(traceResponse -> {}, Throwable::printStackTrace);
    }

    public void sendTopBottomBannerClick(String tCode) {
        NetworkService.getInstance().getTraceApi().traceClick(
                FUNC.TRACE,
                TFUNC.TRACE,
                "I"+tCode,
                "")
                .compose(RxUtil.mainAsync())
                .subscribe(traceResponse -> {}, Throwable::printStackTrace);
    }

    public void sendItemListClick(ItemList itemList, String tag) {
        NetworkService.getInstance().getTraceApi().traceClick(
                FUNC.TRACE,
                TFUNC.TRACE,
                TraceQueryWriter.getItemListTcode(itemList),
                TraceQueryWriter.getItemListFcode(tag))
                .compose(RxUtil.mainAsync())
                .subscribe(traceResponse -> {}, Throwable::printStackTrace);
    }

    public void sendItemDetailDisplay(ItemDetail itemDetail) {
        NetworkService.getInstance().getTraceApi().traceDisplay(
                FUNC.TRACE,
                TFUNC.ITEM,
                itemDetail.getOnline().getOnline_id(),
                itemDetail.getOnline().getChannel_id())
                .compose(RxUtil.mainAsync())
                .subscribe(traceResponse -> {}, Throwable::printStackTrace);
    }
}
