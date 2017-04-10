package com.crazymike.models;

import android.provider.Settings;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChaoJen on 2017/3/22.
 */

public class CSServicePostMessage {

    private String contact_type = "31";
    private String item_deliver_from;
    private String json_return = "true";
    private String cfrom = "consultation";
    private String title;
    private String name;
    private String tel;
    private String email;
    private String content;
    private String callfrom = "app";

    public CSServicePostMessage(String itemDeliverFrom, String title, String consumerName, String consumerPhoneNumber, String consumerEmail, String content) {
        this.item_deliver_from = itemDeliverFrom;
        this.title = title;
        this.name = consumerName;
        this.tel = consumerPhoneNumber;
        this.email = consumerEmail;
        this.content = content;
    }
}
