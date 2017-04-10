package com.crazymike.models;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Notice implements Parcelable{
    private String notice_id;
    private String title;
    private String url;
    private String is_url;
    private String cata_id;
    private String img;
    private String date_on;
    private String date_off;
    private Object content;

    protected Notice(Parcel in) {
        notice_id = in.readString();
        title = in.readString();
        url = in.readString();
        is_url = in.readString();
        cata_id = in.readString();
        img = in.readString();
        date_on = in.readString();
        date_off = in.readString();
    }

    public static final Creator<Notice> CREATOR = new Creator<Notice>() {
        @Override
        public Notice createFromParcel(Parcel in) {
            return new Notice(in);
        }

        @Override
        public Notice[] newArray(int size) {
            return new Notice[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(notice_id);
        parcel.writeString(title);
        parcel.writeString(url);
        parcel.writeString(is_url);
        parcel.writeString(cata_id);
        parcel.writeString(img);
        parcel.writeString(date_on);
        parcel.writeString(date_off);
    }
}
