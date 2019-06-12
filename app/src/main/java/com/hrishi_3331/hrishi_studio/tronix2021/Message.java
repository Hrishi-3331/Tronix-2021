package com.hrishi_3331.hrishi_studio.tronix2021;

public class Message {

    private String Date;
    private String Time;
    private String Content;
    private String Sender;

    public Message() {

    }

    public Message(String date, String time, String content, String sender) {
        Date = date;
        Time = time;
        Content = content;
        Sender = sender;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getSender() {
        return Sender;
    }

    public void setSender(String sender) {
        Sender = sender;
    }
}

