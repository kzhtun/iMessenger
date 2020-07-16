package com.info121.imessenger.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileDetails {
    @SerializedName("DeviceID")
    @Expose
    private String deviceID;
    @SerializedName("HPNo")
    @Expose
    private String hPNo;
    @SerializedName("ProfileName")
    @Expose
    private String profileName;
    @SerializedName("ProfileStatus")
    @Expose
    private String profileStatus;
    @SerializedName("ThumbImage")
    @Expose
    private String thumbImage;

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String gethPNo() {
        return hPNo;
    }

    public void sethPNo(String hPNo) {
        this.hPNo = hPNo;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getProfileStatus() {
        return profileStatus;
    }

    public void setProfileStatus(String profileStatus) {
        this.profileStatus = profileStatus;
    }

    public String getThumbImage() {
        return thumbImage;
    }

    public void setThumbImage(String thumbImage) {
        this.thumbImage = thumbImage;
    }
}
