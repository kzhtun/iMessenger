package com.info121.imessenger.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ObjectRes {
	@SerializedName("IsUnderMaintenance")
	@Expose
	private String isUnderMaintenance;
	@SerializedName("LevelDetails")
	@Expose
	private String levelDetails;
	@SerializedName("LevelNotCheckedDetails")
	@Expose
	private String levelNotCheckedDetails;
	@SerializedName("ProfileDetails")
	@Expose
	private List<ProfileDetails> profileDetails;
	@SerializedName("MessageDetails")
	@Expose
	private List<MessageDetails> messageDetails;
	@SerializedName("RoundsDetails")
	@Expose
	private String roundsDetails;
	@SerializedName("UnitDetails")
	@Expose
	private String unitDetails;
	@SerializedName("Version")
	@Expose
	private String version;
	@SerializedName("UserHP")
	@Expose
	private String userhp;
	@SerializedName("lastlogin")
	@Expose
	private String lastlogin;
	@SerializedName("responsemessage")
	@Expose
	private String responsemessage;
	@SerializedName("status")
	@Expose
	private String status;
	@SerializedName("token")
	@Expose
	private String token;

	public String getIsUnderMaintenance() {
		return isUnderMaintenance;
	}

	public void setIsUnderMaintenance(String isUnderMaintenance) {
		this.isUnderMaintenance = isUnderMaintenance;
	}

	public String getLevelDetails() {
		return levelDetails;
	}

	public void setLevelDetails(String levelDetails) {
		this.levelDetails = levelDetails;
	}

	public String getLevelNotCheckedDetails() {
		return levelNotCheckedDetails;
	}

	public void setLevelNotCheckedDetails(String levelNotCheckedDetails) {
		this.levelNotCheckedDetails = levelNotCheckedDetails;
	}

	public String getRoundsDetails() {
		return roundsDetails;
	}

	public void setRoundsDetails(String roundsDetails) {
		this.roundsDetails = roundsDetails;
	}

	public String getUnitDetails() {
		return unitDetails;
	}

	public void setUnitDetails(String unitDetails) {
		this.unitDetails = unitDetails;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getLastlogin() {
		return lastlogin;
	}

	public void setLastlogin(String lastlogin) {
		this.lastlogin = lastlogin;
	}

	public String getResponsemessage() {
		return responsemessage;
	}

	public void setResponsemessage(String responsemessage) {
		this.responsemessage = responsemessage;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUserhp() {
		return userhp;
	}

	public void setUserhp(String userhp) {
		this.userhp = userhp;
	}

	public List<MessageDetails> getMessageDetails() {
		return messageDetails;
	}

	public void setMessageDetails(List<MessageDetails> messageDetails) {
		this.messageDetails = messageDetails;
	}

	public List<ProfileDetails> getProfileDetails() {
		return profileDetails;
	}

	public void setProfileDetails(List<ProfileDetails> profileDetails) {
		this.profileDetails = profileDetails;
	}
}
