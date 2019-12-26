package com.hrishi_3331.hrishi_studio.tronix2021;

public class Message {

    private String content;
    private String sender;
    private String date;

    public Message() {

    }

    public Message(String content, String sender, String date) {
        this.content = content;
        this.sender = sender;
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

