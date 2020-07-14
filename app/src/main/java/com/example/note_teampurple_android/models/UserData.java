package com.example.note_teampurple_android.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Usernotes")
public class UserData implements Parcelable
{

    @PrimaryKey(autoGenerate = true)
    public int id;
    String title;
    String subTitle;
    String audiodata;
    double lat;
    double lng;
    String imagedata;
    String category;
    String datedata;

    public UserData() {
    }

    protected UserData(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.subTitle = in.readString();
        this.audiodata = in.readString();
        this.lat = in.readDouble();
        this.lng = in.readDouble();
        this.imagedata = in.readString();
        this.category = in.readString();
        this.datedata = in.readString();
    }

    public static final Creator<UserData> CREATOR = new Creator<UserData>() {
        @Override
        public UserData createFromParcel(Parcel source) {
            return new UserData(source);
        }

        @Override
        public UserData[] newArray(int size) {
            return new UserData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.subTitle);
        dest.writeString(this.audiodata);
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lng);
        dest.writeString(this.imagedata);
        dest.writeString(this.category);
        dest.writeString(this.datedata);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getAudioData() {
        return audiodata;
    }

    public void setAudioData(String audioData) {
        this.audiodata = audioData;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getImageData() {
        return imagedata;
    }

    public void setImageData(String imageData) {
        this.imagedata = imageData;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDateData() {
        return datedata;
    }

    public void setDateData(String dateData) {
        this.datedata = dateData;
    }

    public static Creator<UserData> getCREATOR() {
        return CREATOR;
    }


}
