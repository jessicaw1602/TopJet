package com.example.topjet.Entities;

public class ContentEntity {

    String pageNumId, topicArea, heading, paraOne, paraTwo, title, ivOne, ivTwo;

    // Constructors
    public ContentEntity(){}

    // without the images
    public ContentEntity(String pageNumId, String topicArea, String heading, String paraOne, String paraTwo, String title) {
        this.topicArea = topicArea;
        this.pageNumId = pageNumId;
        this.heading = heading;
        this.paraOne = paraOne;
        this.paraTwo = paraTwo;
        this.title = title;
    }

    // Including the images...
    public ContentEntity(String pageNumId, String topicArea, String heading, String title, String paraOne, String paraTwo, String ivOne, String ivTwo) {
        this.pageNumId = pageNumId;
        this.topicArea = topicArea;
        this.heading = heading;
        this.paraOne = paraOne;
        this.paraTwo = paraTwo;
        this.title = title;
        this.ivOne = ivOne;
        this.ivTwo = ivTwo;
    }

    // Setters
    public void setPageNumId(String pageNumId) {
        this.pageNumId = pageNumId;
    }

    public void setTopicArea(String topic) {
        this.topicArea = topicArea;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public void setParaOne(String paraOne) {
        this.paraOne = paraOne;
    }

    public void setParaTwo(String paraTwo) {
        this.paraTwo = paraTwo;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIvOne(String ivOne) {
        this.ivOne = ivOne;
    }

    public void setIvTwo(String ivTwo) {
        this.ivTwo = ivTwo;
    }

    // Getters
    public String getPageNumId() {
        return pageNumId;
    }

    public String getTopicArea() {
        return topicArea;
    }

    public String getHeading() {
        return heading;
    }

    public String getParaOne() {
        return paraOne;
    }

    public String getParaTwo() {
        return paraTwo;
    }

    public String getTitle() {
        return title;
    }

    public String getIvOne() {
        return ivOne;
    }

    public String getIvTwo() {
        return ivTwo;
    }
}
