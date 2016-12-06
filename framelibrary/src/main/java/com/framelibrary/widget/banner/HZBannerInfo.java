package com.framelibrary.widget.banner;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 焦点图信息
 * Created by liji on 2016/8/3.
 */
public class HZBannerInfo implements Parcelable {
    
    /**
     * id : fc3f5103-01be-4e77-9a42-0efb95784dca
     * catagoryId : 200101
     * type : 0
     * imgId : 19ceb9f8-59ab-4d7b-ac2c-15a13cd6c237
     * title : 专题
     * ext1 : null
     * ext2 : null
     * ext3 : null
     * orderId : 2016.08.02 13:27:57
     * createAccount : null
     * createTime : null
     * modifyAccount : null
     * modifyTime : null
     * srcInfo :
     * srcHtml : null
     * imgUrl : download/19ceb9f8-59ab-4d7b-ac2c-15a13cd6c237
     */
    
    private String id;
    
    private String catagoryId;
    
    private int type;
    
    private String imgId;
    
    private String title;
    
    private String ext1;
    
    private String ext2;
    
    private String ext3;
    
    private String orderId;
    
    private String createAccount;
    
    private String createTime;
    
    private String modifyAccount;
    
    private String modifyTime;
    
    private String srcInfo;
    
    private String srcHtml;
    
    private String imgUrl;
    
    @Override
    public String toString() {
        return "MFocusImg{" + "id='" + id + '\'' + ", catagoryId='" + catagoryId + '\'' + ", type=" + type + ", imgId='"
                + imgId + '\'' + ", title='" + title + '\'' + ", ext1='" + ext1 + '\'' + ", ext2='" + ext2 + '\''
                + ", ext3='" + ext3 + '\'' + ", orderId='" + orderId + '\'' + ", createAccount='" + createAccount + '\''
                + ", createTime='" + createTime + '\'' + ", modifyAccount='" + modifyAccount + '\'' + ", modifyTime='"
                + modifyTime + '\'' + ", srcInfo='" + srcInfo + '\'' + ", srcHtml='" + srcHtml + '\'' + ", imgUrl='"
                + imgUrl + '\'' + '}';
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getCatagoryId() {
        return catagoryId;
    }
    
    public void setCatagoryId(String catagoryId) {
        this.catagoryId = catagoryId;
    }
    
    public int getType() {
        return type;
    }
    
    public void setType(int type) {
        this.type = type;
    }
    
    public String getImgId() {
        return imgId;
    }
    
    public void setImgId(String imgId) {
        this.imgId = imgId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getExt1() {
        return ext1;
    }
    
    public void setExt1(String ext1) {
        this.ext1 = ext1;
    }
    
    public String getExt2() {
        return ext2;
    }
    
    public void setExt2(String ext2) {
        this.ext2 = ext2;
    }
    
    public String getExt3() {
        return ext3;
    }
    
    public void setExt3(String ext3) {
        this.ext3 = ext3;
    }
    
    public String getOrderId() {
        return orderId;
    }
    
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    
    public String getCreateAccount() {
        return createAccount;
    }
    
    public void setCreateAccount(String createAccount) {
        this.createAccount = createAccount;
    }
    
    public String getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    
    public String getModifyAccount() {
        return modifyAccount;
    }
    
    public void setModifyAccount(String modifyAccount) {
        this.modifyAccount = modifyAccount;
    }
    
    public String getModifyTime() {
        return modifyTime;
    }
    
    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }
    
    public String getSrcInfo() {
        return srcInfo;
    }
    
    public void setSrcInfo(String srcInfo) {
        this.srcInfo = srcInfo;
    }
    
    public String getSrcHtml() {
        return srcHtml;
    }
    
    public void setSrcHtml(String srcHtml) {
        this.srcHtml = srcHtml;
    }
    
    public String getImgUrl() {
        return imgUrl;
    }
    
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.catagoryId);
        dest.writeInt(this.type);
        dest.writeString(this.imgId);
        dest.writeString(this.title);
        dest.writeString(this.ext1);
        dest.writeString(this.ext2);
        dest.writeString(this.ext3);
        dest.writeString(this.orderId);
        dest.writeString(this.createAccount);
        dest.writeString(this.createTime);
        dest.writeString(this.modifyAccount);
        dest.writeString(this.modifyTime);
        dest.writeString(this.srcInfo);
        dest.writeString(this.srcHtml);
        dest.writeString(this.imgUrl);
    }
    
    public HZBannerInfo() {
    }
    
    protected HZBannerInfo(Parcel in) {
        this.id = in.readString();
        this.catagoryId = in.readString();
        this.type = in.readInt();
        this.imgId = in.readString();
        this.title = in.readString();
        this.ext1 = in.readString();
        this.ext2 = in.readString();
        this.ext3 = in.readString();
        this.orderId = in.readString();
        this.createAccount = in.readString();
        this.createTime = in.readString();
        this.modifyAccount = in.readString();
        this.modifyTime = in.readString();
        this.srcInfo = in.readString();
        this.srcHtml = in.readString();
        this.imgUrl = in.readString();
    }
    
    public static final Parcelable.Creator<HZBannerInfo> CREATOR = new Parcelable.Creator<HZBannerInfo>() {
        @Override
        public HZBannerInfo createFromParcel(Parcel source) {
            return new HZBannerInfo(source);
        }
        
        @Override
        public HZBannerInfo[] newArray(int size) {
            return new HZBannerInfo[size];
        }
    };
}
