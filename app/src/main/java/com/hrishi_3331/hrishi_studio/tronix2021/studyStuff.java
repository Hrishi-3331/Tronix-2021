package com.hrishi_3331.hrishi_studio.tronix2021;

public class studyStuff {

    private String title;
    private String url;
    private String author;
    private String description;

    public studyStuff() {
    }

    public studyStuff(String title, String url, String author, String description) {
        this.title = title;
        this.url = url;
        this.author = author;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription(){
        return description;
    }
}
