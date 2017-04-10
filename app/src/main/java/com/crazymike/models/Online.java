package com.crazymike.models;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Online {


    /**
     * online_keys : groupon_online
     * online_id : 88759
     * item_id : 66084
     * channel_id : 5
     * date_online : 2016-08-23 09:00:00
     * date_offline : 2016-08-31 09:00:00
     * tag_id : 1484,4,4,1317,1375,1434
     * buy_online_items : 0
     * is_stock : true
     * main_tag : 1484
     * click_count : 183
     */


    private String online_keys;
    private String online_id;
    private String item_id;
    private String channel_id;
    private String date_online;
    private String date_offline;
    private String tag_id;
    private String buy_online_items;
    private Boolean is_stock;
    private String main_tag;
    private String click_count;
}
