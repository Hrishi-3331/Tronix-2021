package com.hrishi_3331.hrishi_studio.tronix2021;

public class notice {
    private String date;
    private String title;
    private String content;

    public notice() {
    }

    public notice(String date, String title, String content) {
        this.date = date;
        this.title = title;
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
}
