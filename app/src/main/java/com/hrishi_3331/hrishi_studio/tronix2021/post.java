package com.hrishi_3331.hrishi_studio.tronix2021;

public class post {

    private String author;
    private String date;
    private String question;
    private String image;
    private String id;
    private String category;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String option5;
    private String option1_strength;
    private String option2_strength;
    private String option3_strength;
    private String option4_strength;
    private String option5_strength;
    private String description;

    public post() {
    }


    public post(String author, String date, String question, String image, String id, String category, String option1, String option2, String option3, String option4, String option5, String option1_strength, String option2_strength, String option3_strength, String option4_strength, String option5_strength, String description) {
        this.author = author;
        this.date = date;
        this.question = question;
        this.image = image;
        this.id = id;
        this.category = category;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.option5 = option5;
        this.option1_strength = option1_strength;
        this.option2_strength = option2_strength;
        this.option3_strength = option3_strength;
        this.option4_strength = option4_strength;
        this.option5_strength = option5_strength;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public String getOption5() {
        return option5;
    }

    public void setOption5(String option5) {
        this.option5 = option5;
    }

    public String getOption1_strength() {
        return option1_strength;
    }

    public void setOption1_strength(String option1_strength) {
        this.option1_strength = option1_strength;
    }

    public String getOption2_strength() {
        return option2_strength;
    }

    public void setOption2_strength(String option2_strength) {
        this.option2_strength = option2_strength;
    }

    public String getOption3_strength() {
        return option3_strength;
    }

    public void setOption3_strength(String option3_strength) {
        this.option3_strength = option3_strength;
    }

    public String getOption4_strength() {
        return option4_strength;
    }

    public void setOption4_strength(String option4_strength) {
        this.option4_strength = option4_strength;
    }

    public String getOption5_strength() {
        return option5_strength;
    }

    public void setOption5_strength(String option5_strength) {
        this.option5_strength = option5_strength;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
