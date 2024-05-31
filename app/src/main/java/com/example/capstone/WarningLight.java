package com.example.capstone;

import android.os.Parcel;
import android.os.Parcelable;

public class WarningLight implements Parcelable {//경고등
    private String imageName;
    private String title;
    private String description;
    private String description2;
    private String description3;

    // Constructor
    public WarningLight(String imageName, String title, String description, String description2, String description3) {
        this.imageName = imageName;
        this.title = title;
        this.description = description;
        this.description2 = description2;
        this.description3 = description3;
    }

    // Getter methods
    public String getImageName() { return imageName; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getDescription2() { return description2; }
    public String getDescription3() { return description3; }

    protected WarningLight(Parcel in) {
        imageName = in.readString();
        title = in.readString();
        description = in.readString();
        description2 = in.readString();
        description3 = in.readString();
    }

    public static final Creator<WarningLight> CREATOR = new Creator<WarningLight>() {
        @Override
        public WarningLight createFromParcel(Parcel in) {
            return new WarningLight(in);
        }

        @Override
        public WarningLight[] newArray(int size) {
            return new WarningLight[size];
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