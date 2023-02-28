package com.dev.models;

import com.dev.objects.Message;

import java.text.SimpleDateFormat;

public class MessageModel {
    private int id;
    private String title;
    private String content;
    private String sendDate;
    private String senderUsername;
    private boolean sentByMe;

    public MessageModel () {
    }

    public MessageModel (Message message, int userId) {
        this.id = message.getId();
        this.title = message.getTitle();
        this.content = message.getContent();
        this.senderUsername = message.getSender().getUsername();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        this.sendDate = simpleDateFormat.format(message.getSendDate());
        if (userId == message.getSender().getId()) {
            this.sentByMe = true;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public boolean isSentByMe() {
        return sentByMe;
    }

    public void setSentByMe(boolean sentByMe) {
        this.sentByMe = sentByMe;
    }
}
