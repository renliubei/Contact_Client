package com.example.contact_client.repository;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class VideoCut implements Parcelable {
    //视频片段，数据库的成员

    public static final Creator<VideoCut> CREATOR = new Creator<VideoCut>() {
        @Override
        public VideoCut createFromParcel(Parcel in) {
            return new VideoCut(in);
        }

        @Override
        public VideoCut[] newArray(int size) {
            return new VideoCut[size];
        }
    };
    //主要属性
    @ColumnInfo(name = "isCut")
    private boolean isCut;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "urlString")
    private String urlString;
    @ColumnInfo(name = "thumbnailPath")
    private String thumbnailPath;

    public VideoCut(boolean isCut, String name, String description, String urlString, String thumbnailPath) {
        this.isCut = isCut;
        this.name = name;
        this.description = description;
        this.urlString = urlString;
        this.thumbnailPath = thumbnailPath;
    }

    //自动生成id
    @PrimaryKey(autoGenerate = true)
    private long id;

    protected VideoCut(Parcel in) {
        id = in.readLong();
        isCut = in.readByte() != 0;
        name = in.readString();
        description = in.readString();
        urlString = in.readString();
        thumbnailPath = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeByte((byte) (isCut ? 1 : 0));
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(urlString);
        dest.writeString(thumbnailPath);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public String getUrlString() {
        return urlString;
    }

    public void setUrlString(String urlString) {
        this.urlString = urlString;
    }

    public boolean isCut() {
        return isCut;
    }

    public void setCut(boolean cut) {
        isCut = cut;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
