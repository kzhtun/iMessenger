package com.info121.imessenger.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessageDetails {
    @SerializedName("GroupID")
    @Expose
    private String groupID;
    @SerializedName("MessageID")
    @Expose
    private String messageID;
    @SerializedName("Messages")
    @Expose
    private String messages;
    @SerializedName("MsgDate")
    @Expose
    private String msgDate;
    @SerializedName("MsgStatus")
    @Expose
    private String msgStatus;
    @SerializedName("Sender")
    @Expose
    private String sender;
    @SerializedName("SenderID")
    @Expose
    private String senderID;


    public MessageDetails(String msgDate, String msgStatus) {
        this.msgDate = msgDate;
        this.msgStatus = msgStatus;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public String getMsgDate() {
        return msgDate;
    }

    public void setMsgDate(String msgDate) {
        this.msgDate = msgDate;
    }

    public String getMsgStatus() {
        return msgStatus;
    }

    public void setMsgStatus(String msgStatus) {
        this.msgStatus = msgStatus;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }
}
