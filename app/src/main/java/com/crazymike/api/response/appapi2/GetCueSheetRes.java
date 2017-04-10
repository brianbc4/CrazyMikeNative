package com.crazymike.api.response.appapi2;

import com.crazymike.models.CueSheet;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GetCueSheetRes extends BaseRes {

    @SerializedName("events")
    @Expose
    private List<CueSheet> events = new ArrayList<>();

    public List<CueSheet> getEvents() {
        return events;
    }
}
