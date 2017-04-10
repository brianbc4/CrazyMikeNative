package com.crazymike.models;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RotateJson {
    private String name;
    private String imgurl;
    private String imgurl_mobile;
    @SerializedName("class")
    private String classX;
    private String css;
    private String url;
    private String tcode;
    private String target;
    private String click;
}
