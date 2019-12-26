package com.hrishi_3331.hrishi_studio.tronix2021;

public class forumAnswer {

    private String author, ans, image, author_id;

    public forumAnswer(String author, String ans, String image, String author_id) {
        this.author = author;
        this.ans = ans;
        this.image = image;
        this.author_id = author_id;
    }

    public forumAnswer() {
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }
}
