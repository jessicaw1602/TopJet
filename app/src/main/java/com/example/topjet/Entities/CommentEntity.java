package com.example.topjet.Entities;

public class CommentEntity {

    // What would a comment section have?

    String docId, postId, username, date, comment;

    // Constructors
    public CommentEntity(){}

    public CommentEntity(String docId, String postId, String username, String date, String comment) {
        this.docId = docId;
        this.postId = postId;
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

    public void setPostId(String postId) {
        this.postId = postId;
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

    public String getPostId() {
        return postId;
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
