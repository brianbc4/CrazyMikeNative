package com.crazymike.trace.writer;

import com.crazymike.models.Banner;
import com.crazymike.models.ItemList;
import com.crazymike.models.RotateJson;
import com.crazymike.respositories.ProductRepository;

/**
 * Created by ChaoJen on 2016/11/18.
 *
 * return format "Tcode" & "Fcode" for traceApi use
 */

public class TraceQueryWriter {

    public static String getTagTcode(int position) {
        String tagId;
        if(position == 0) {
            //trace user click Home tab, but there have no tagId
            return "HomePage";
        }else {
            tagId = ProductRepository.getInstance()
                    .getUpSideMenus().get(position)
                    .getUrl().split("tag-")[1].split("/")[0];
        }
        return String.format("T%s", tagId);
    }

    public static String getTagFcode() {
        return "S34";
    }

    public static String getBannerTcode(Banner banner, RotateJson rotateJson) {
        return (rotateJson.getTcode() != null && rotateJson.getTcode().length() != 0)
                ? rotateJson.getTcode()
                : String.format("B%s", banner.getBanner_id());
    }

    public static String getBannerFcode(Banner banner) {
        return String.format("B%s", banner.getBanner_id());
    }

    public static String getItemListTcode(ItemList itemList) {
        return String.format("I%s", itemList.getOnline_id());
    }

    public static String getItemListFcode(String tagId) {
        return tagId.equals("5") ? "CI5" : String.format("TI", tagId);
    }
}
