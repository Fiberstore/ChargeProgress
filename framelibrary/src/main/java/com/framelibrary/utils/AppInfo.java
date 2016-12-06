package com.framelibrary.utils;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * @2Do: AppInfo
 * Apk信息model
 * @Author M2
 * @Version v 1.0
 * @Date 2016/3/22 0022
 */
public class AppInfo implements Parcelable {
    private String name = "";
    
    private String packag = "";
    
    private String versionName = "";
    
    private Drawable icon;
    
    private int version = 0;
    
    private String path = "";
    
    private String image;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getPackage() {
        return packag;
    }
    
    public void setPackage(String packag) {
        this.packag = packag;
    }
    
    public String getVersionName() {
        return versionName;
    }
    
    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }
    
    public Drawable getIcon() {
        return icon;
    }
    
    public void setIcon(Drawable drawable) {
        this.icon = drawable;
    }
    
    public int getVersion() {
        return version;
    }
    
    public void setVersion(int version) {
        this.version = version;
    }
    
    public String getPath() {
        return path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
    
    public String getImage() {
        return image;
    }
    
    public void setImage(String image) {
        this.image = image;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.packag);
        dest.writeString(this.versionName);
        dest.writeInt(this.version);
        dest.writeString(this.path);
        dest.writeString(this.image);
    }

    public AppInfo() {
    }

    protected AppInfo(Parcel in) {
        this.name = in.readString();
        this.packag = in.readString();
        this.versionName = in.readString();
        this.icon = in.readParcelable(Drawable.class.getClassLoader());
        this.version = in.readInt();
        this.path = in.readString();
        this.image = in.readString();
    }

    public static final Creator<AppInfo> CREATOR = new Creator<AppInfo>() {
        public AppInfo createFromParcel(Parcel source) {
            return new AppInfo(source);
        }

        public AppInfo[] newArray(int size) {
            return new AppInfo[size];
        }
    };
}
