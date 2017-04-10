package com.crazymike.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CueSheet implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("pushDate")
    @Expose
    private String pushDate;
    @SerializedName("hyperLink")
    @Expose
    private String hyperLink;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("imagePath")
    @Expose
    private String imagePath;
    @SerializedName("clickCount_iOS")
    @Expose
    private String clickCountIOS;
    @SerializedName("clickCount_android")
    @Expose
    private String clickCountAndroid;
    @SerializedName("deleted")
    @Expose
    private String deleted;
    @SerializedName("created_time")
    @Expose
    private String createdTime;
    @SerializedName("updated_time")
    @Expose
    private String updatedTime;
    @SerializedName("creator")
    @Expose
    private String creator;
    @SerializedName("editor")
    @Expose
    private String editor;
    @SerializedName("dialogType")
    @Expose
    private String dialogType;

    protected CueSheet(Parcel in) {
        id = in.readString();
        type = in.readString();
        pushDate = in.readString();
        hyperLink = in.readString();
        message = in.readString();
        imagePath = in.readString();
        clickCountIOS = in.readString();
        clickCountAndroid = in.readString();
        deleted = in.readString();
        createdTime = in.readString();
        updatedTime = in.readString();
        creator = in.readString();
        editor = in.readString();
        dialogType = in.readString();
    }

    public static final Creator<CueSheet> CREATOR = new Creator<CueSheet>() {
        @Override
        public CueSheet createFromParcel(Parcel in) {
            return new CueSheet(in);
        }

        @Override
        public CueSheet[] newArray(int size) {
            return new CueSheet[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(type);
        dest.writeString(pushDate);
        dest.writeString(hyperLink);
        dest.writeString(message);
        dest.writeString(imagePath);
        dest.writeString(clickCountIOS);
        dest.writeString(clickCountAndroid);
        dest.writeString(deleted);
        dest.writeString(createdTime);
        dest.writeString(updatedTime);
        dest.writeString(creator);
        dest.writeString(editor);
        dest.writeString(dialogType);
    }
}
