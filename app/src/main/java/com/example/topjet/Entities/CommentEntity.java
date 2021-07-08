package com.example.topjet.Entities;

public class CommentEntity {

    // What would a comment section have?

    String docId, username, date, comment;

    // Constructors
    public CommentEntity(){}

    public CommentEntity(String docId, String username, String date, String comment) {
        this.docId = docId;
        this.username = username;
        this.date = date;
        this.comment = comment;
    }

    public CommentEntity(String username, String date, String comment) {
        this.username = username;
        this.date = date;
        this.comment = comment;
    }

    // Setters

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    // Getters
    public String getDocId() {
        return docId;
    }

    public String getUsername() {
        return username;
    }

    public String getDate() {
        return date;
    }

    public String getComment() {
        return comment;
    }
}
