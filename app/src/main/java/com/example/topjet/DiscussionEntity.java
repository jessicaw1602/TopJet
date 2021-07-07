package com.example.topjet;

public class DiscussionEntity {

    private String id, title, username, date, postTag, shortDescription, content;

    // Constructors
    public DiscussionEntity(){}

    // Constructor without ID
    public DiscussionEntity(String title, String username, String date, String postTag, String shortDescription, String content) {
        this.title = title;
        this.username = username;
        this.date = date;
        this.postTag = postTag;
        this.shortDescription = shortDescription;
        this.content = content;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
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

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // Getters
    public String getId() {
        return id;
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

    public String getShortDescription() {
        return shortDescription;
    }

    public String getContent() {
        return content;
    }
}
