package com.example.topjet.Entities;

import com.google.firebase.firestore.Exclude;

public class DiscussionEntity {

    private String docId, title, username, date, postTag, shortDesc, content;


    // Constructors
    public DiscussionEntity(){}

    // Constructor without ID
    public DiscussionEntity(String title, String username, String date, String postTag, String shortDesc, String content) {
        this.title = title;
        this.username = username;
        this.date = date;
        this.postTag = postTag;
        this.shortDesc = shortDesc;
        this.content = content;
    }

    // Constructor with ID
    public DiscussionEntity(String docId, String title, String username, String date, String postTag, String shortDesc, String content) {
        this.docId = docId;
        this.title = title;
        this.username = username;
        this.date = date;
        this.postTag = postTag;
        this.shortDesc = shortDesc;
        this.content = content;
    }

    public DiscussionEntity(String title, String username, String date, String postTag, String shortDesc) {
        this.title = title;
        this.username = username;
        this.date = date;
        this.postTag = postTag;
        this.shortDesc = shortDesc;
    }


    // Setters
    public void setDocId(String docId) {
        this.docId = docId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPostTag(String postTag) {
        this.postTag = postTag;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // Getters
    public String getDocId() {
        return docId;
    }

    public String getTitle() {
        return title;
    }

    public String getUsername() {
        return username;
    }

    public String getDate() {
        return date;
    }

    public String getPostTag() {
        return postTag;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public String getContent() {
        return content;
    }
}
