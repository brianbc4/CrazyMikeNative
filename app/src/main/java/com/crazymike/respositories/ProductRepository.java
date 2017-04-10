package com.crazymike.respositories;

import com.crazymike.api.FUNC;
import com.crazymike.api.NetworkService;
import com.crazymike.api.URL;
import com.crazymike.api.response.AppIndexResponse;
import com.crazymike.api.response.BannerResponse;
import com.crazymike.api.response.ItemDetailResponse;
import com.crazymike.api.response.ItemListResponse;
import com.crazymike.api.response.SearchQueryResponse;
import com.crazymike.models.Banner;
import com.crazymike.models.ChannelInfo;
import com.crazymike.models.ItemDetail;
import com.crazymike.models.ItemList;
import com.crazymike.models.Notice;
import com.crazymike.models.Promote;
import com.crazymike.models.SearchHotKey;
import com.crazymike.models.Tag;
import com.crazymike.models.UpSideMenu;

import java.util.List;

import lombok.Getter;
import rx.Observable;

import static com.crazymike.api.DISP.BUYDESC;
import static com.crazymike.api.DISP.RNDESC;


@Getter
public class ProductRepository {

    private static ProductRepository INSTANCE = null;
    private static final String TAG_TRAVEL = "2247";
    private int carts;

    private List<Notice> notices;
    private List<UpSideMenu> upSideMenus;
    private List<Tag> tags;
    private Promote promote;
    private List<Banner> banners;
    private List<SearchHotKey> searchHotKeys;
    private ChannelInfo channelInfo;

    private ProductRepository() {

    }

    public static ProductRepository getInstance() {
        return INSTANCE != null ? INSTANCE : (INSTANCE = new ProductRepository());
    }


    public Observable<ProductRepository> callAppIndex(String channel, String tag) {
        return NetworkService.getInstance().getProductApi().callAppIndex(FUNC.APP_INDEX, channel, "android", tag, 6)
                .map(appIndexResponse -> {
                    AppIndexResponse.Rtn rtn = appIndexResponse.getRtn();
                    notices = rtn.getNotice();
                    upSideMenus = rtn.getUpSideMenu();
                    banners = rtn.getBanner();
                    tags = rtn.getTag();
                    promote = rtn.getPromote();
                    searchHotKeys = rtn.getSearchHotkey();
                    channelInfo = rtn.getChannelInfo();
                    return this;
                });
    }

    public Observable<List<ItemList>> callItemListByChannel(String channel, int page) {

        String disp = channel.equals("13") ? BUYDESC : RNDESC;
        return NetworkService.getInstance().getProductApi().callItemListByChannel(FUNC.ITEM_LIST, channel, page, disp, "json")
                .map(ItemListResponse::getRtn);
    }

    public Observable<List<ItemList>> callItemListByTag(String channel, String tag, int page) {
        String disp = channel.equals("13") ? BUYDESC : RNDESC;
        return NetworkService.getInstance().getProductApi().callItemListByTag(FUNC.ITEM_LIST, tag, page, disp, "json")
                .map(ItemListResponse::getRtn);
    }

    public Observable<Banner> callBanner(String tag) {
        return NetworkService.getInstance().getProductApi().callBanner("banner", String.format("m_top_banner_tag_%s", tag))
                .map(BannerResponse::getRtn);
    }

    public Observable<ItemDetail> getItemDetail(String itemId) {
        return NetworkService.getInstance().getProductApi().callItemDetail(FUNC.ITEM_DETAIL, itemId, "json")
                .map(ItemDetailResponse::getRtn);
    }

    public void getSubTag(String tagId, OnGetSubTagCallback onGetSubTagCallback) {

        try {
            for (int i = 0; i < tags.size(); i++) {

                Tag tag = tags.get(i);
                if (tag.getTag_id().equals(tagId)) {
                    onGetSubTagCallback.getSubTag(tag, i, null, 0, null, 0);
                    return;
                }

                for (int j = 0; j < tag.getSubs().size(); j++) {
                    Tag subTag1 = tag.getSubs().get(j);
                    if (subTag1.getTag_id().equals(tagId)) {
                        onGetSubTagCallback.getSubTag(tag, i, subTag1, j, null, 0);
                        return;
                    }

                    for (int k = 0; k < subTag1.getSubs().size(); k++) {
                        Tag subTag2 = subTag1.getSubs().get(k);
                        if (subTag2.getTag_id().equals(tagId)) {
                            onGetSubTagCallback.getSubTag(tag, i, subTag1, j, subTag2, k);
                            return;
                        }
                    }
                }
            }
            onGetSubTagCallback.getNothing();
        } catch (Exception e) {
            e.printStackTrace();
            onGetSubTagCallback.getNothing();
        }
    }

    public void getSubTagByName(String tagName, OnGetSubTagCallback onGetSubTagCallback) {

        try {

            for (int i = 0; i < tags.size(); i++) {

                Tag tag = tags.get(i);
                if (tag.getName().equals(tagName)) {
                    onGetSubTagCallback.getSubTag(tag, i, null, 0, null, 0);
                    return;
                }

                for (int j = 0; j < tag.getSubs().size(); j++) {
                    Tag subTag1 = tag.getSubs().get(j);
                    if (subTag1.getName().equals(tagName)) {
                        onGetSubTagCallback.getSubTag(tag, i, subTag1, j, null, 0);
                        return;
                    }

                    for (int k = 0; k < subTag1.getSubs().size(); k++) {
                        Tag subTag2 = subTag1.getSubs().get(k);
                        if (subTag2.getName().equals(tagName)) {
                            onGetSubTagCallback.getSubTag(tag, i, subTag1, j, subTag2, k);
                            return;
                        }
                    }
                }
            }
            onGetSubTagCallback.getNothing();
        } catch (Exception e) {
            e.printStackTrace();
            onGetSubTagCallback.getNothing();
        }
    }

    public boolean hasSpecialLogo() {
        return channelInfo != null && channelInfo.getLogo() != null && !channelInfo.getLogo().equals("");
    }

    public interface OnGetSubTagCallback {
        void getSubTag(Tag tag, int tagPosition, Tag subTag1, int subTag1Position, Tag subTag2, int subTag2Position);

        void getNothing();
    }

    public Observable<SearchQueryResponse.Rtn> searchQuery(String key, int pageIndex, int pageSize, String sortType, int sort) {
        return NetworkService.getInstance().getProductApi().searchQuery("searchQuery", key, pageIndex, pageSize, "json", sortType, sort)
                .map(SearchQueryResponse::getRtn);
    }

    public boolean checkIsTravelTag(String tagId) {

        try {

            if (tagId.equals(TAG_TRAVEL)) {
                return true;
            }

            Tag tag = null;

            for (Tag t : tags) {
                if (t.getTag_id().equals(TAG_TRAVEL)) {
                    tag = t;
                    break;
                }
            }

            if (tag == null) {
                return false;
            }

            for (int i = 0; i < tag.getSubs().size(); i++) {
                Tag subTag1 = tag.getSubs().get(i);
                if (subTag1.getTag_id().equals(tagId)) {
                    return true;
                }

                for (int k = 0; k < subTag1.getSubs().size(); k++) {
                    Tag subTag2 = subTag1.getSubs().get(k);
                    if (subTag2.getTag_id().equals(tagId)) {
                        return true;
                    }
                }
            }

            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
