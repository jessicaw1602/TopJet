package com.example.topjet.Entities;

public class TopicEntity {
    int icons;
    String names;
    int progress;

    // Constructors
    public TopicEntity() {}

    public TopicEntity(int icons, String names) {
        this.icons = icons;
        this.names = names;
    }

    public TopicEntity(int icons, String names, int progress) {
        this.icons = icons;
        this.names = names;
        this.progress = progress;
    }

    // Setters
    public void setIcons(int icons) {
        this.icons = icons;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    // Getters
    public int getIcons() {
        return icons;
    }

    public String getNames() {
        return names;
    }

    public int getProgress() {
        return progress;
    }
}


