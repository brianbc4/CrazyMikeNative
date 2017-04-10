package com.crazymike.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class General {

    @SerializedName("pro_id")
    @Expose
    private String proId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("date_online")
    @Expose
    private String dateOnline;
    @SerializedName("date_offline")
    @Expose
    private String dateOffline;
    @SerializedName("time_online")
    @Expose
    private List<String> timeOnline = new ArrayList<>();
    @SerializedName("is_member")
    @Expose
    private Boolean isMember;
    @SerializedName("pro_type")
    @Expose
    private Integer proType;
    @SerializedName("disc_type")
    @Expose
    private Integer discType;
    @SerializedName("disc_promote")
    @Expose
    private String discPromote;
    @SerializedName("disc_purchase_cash")
    @Expose
    private Integer discPurchaseCash;
    @SerializedName("disc_cash")
    @Expose
    private List<String> discCash = new ArrayList<>();
    @SerializedName("password")
    @Expose
    private List<String> password = new ArrayList<>();
    @SerializedName("is_use_coupon")
    @Expose
    private Boolean isUseCoupon;
    @SerializedName("is_use_cash")
    @Expose
    private Boolean isUseCash;
    @SerializedName("percent")
    @Expose
    private Object percent;
    @SerializedName("is_alert")
    @Expose
    private Boolean isAlert;
    @SerializedName("tag")
    @Expose
    private List<Tag> tag = new ArrayList<>();

    @Getter
    @Setter
   public static class Tag {

        public Tag(String name, Boolean isView) {
            this.name = name;
            this.isView = isView;
        }

        @SerializedName("tag_id")
        @Expose
        private String tagId;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("is_view")
        @Expose
        private Boolean isView;
    }
}
