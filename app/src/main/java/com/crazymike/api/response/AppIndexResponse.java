package com.crazymike.api.response;

import com.crazymike.models.Banner;
import com.crazymike.models.ItemList;
import com.crazymike.models.ChannelInfo;
import com.crazymike.models.Notice;
import com.crazymike.models.Promote;
import com.crazymike.models.SearchHotKey;
import com.crazymike.models.Tag;
import com.crazymike.models.UpSideMenu;

import java.util.List;

import lombok.Getter;

@Getter
public class AppIndexResponse extends BaseResponse {

    private Rtn rtn;

    @Getter
    public static class Rtn {

        private List<Tag> tag;

        private List<Notice> notice;

        private List<Banner> banner;

        private List<ItemList> itemList;

        private List<UpSideMenu> upSideMenu;

        private List<SearchHotKey> searchHotkey;

        private Promote promote;

        private ChannelInfo channelInfo;
    }
}
