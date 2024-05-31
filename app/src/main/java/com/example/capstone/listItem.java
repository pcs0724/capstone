package com.example.capstone;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class listItem implements Parcelable {//검출된 경고등 저장

    String imageName;
    String title;
    String description;
    String description2;
    String description3;

    public listItem(String imageName, String title, String description, String description2, String description3) {
        this.imageName = imageName;
        this.title = title;
        this.description = description;
        this.description2 = description2;
        this.description3 = description3;

    }
    protected listItem(Parcel in) {
        imageName = in.readString();
        title = in.readString();
        description = in.readString();
        description2 = in.readString();
        description3 = in.readString();

    }

    // Parcelable.Creator 구현
    public static final Creator<listItem> CREATOR = new Creator<listItem>() {
        @Override
        public listItem createFromParcel(Parcel in) {
            return new listItem(in);
        }

        @Override
        public listItem[] newArray(int size) {
            return new listItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageName);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(description2);
        dest.writeString(description3);

    }
}

