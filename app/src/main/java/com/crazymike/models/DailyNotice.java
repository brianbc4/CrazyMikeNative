package com.crazymike.models;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by user1 on 2017/3/21.
 */

@Setter
@Getter

public class DailyNotice {

    private String notice_id;
    private String title;
    private String url;
    private String is_url;
    private String cata_id;
    private String img;
    private String date_on;
    private String date_off;
    private String content;

}
