package com.crazymike.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Promote {

    @SerializedName("test")
    private List<General> general = new ArrayList<>();
}
