package com.example.timeline;

public class Comments {

    private String content;
    private String publisher;
    private int ID;

    public Comments(String content, String publisher) {
        this.content = content;
        this.publisher = publisher;
    }

    public Comments(String content, int ID) {
        this.content = content;
        this.ID = ID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
