package com.hrishi_3331.hrishi_studio.tronix2021;

public class Somophore {

    private String name;
    private String enrol;
    private String photourl;
    private String contact;

    public Somophore() {
    }

    public Somophore(String name, String enrol, String photourl, String contact) {
        this.name = name;
        this.enrol = enrol;
        this.photourl = photourl;
        this.contact = contact;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnrol() {
        return enrol;
    }

    public void setEnrol(String enrol) {
        this.enrol = enrol;
    }

    public String getPhotourl() {
        return photourl;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }
}
