package com.crazymike.api.response.appapi2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GetAppVersion extends BaseRes {

    @SerializedName("version")
    @Expose
    public List<Version> version = new ArrayList<>();

    public List<Version> getVersion() {
        return version;
    }

    public class Version {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("os")
        @Expose
        public String os;
        @SerializedName("latestVersion")
        @Expose
        public String latestVersion;
        @SerializedName("lowestVersion")
        @Expose
        public String lowestVersion;
        @SerializedName("updated_time")
        @Expose
        public String updatedTime;
        @SerializedName("editor")
        @Expose
        public String editor;

        public String getId() {
            return id;
        }

        public String getOs() {
            return os;
        }

        public String getLatestVersion() {
            return latestVersion;
        }

        public String getLowestVersion() {
            return lowestVersion;
        }

        public String getUpdatedTime() {
            return updatedTime;
        }

        public String getEditor() {
            return editor;
        }
    }
}
