package com.example.topjet.Entities;

public class TopicEntity {
    int icons;
    String names;

    // Constructors
    public TopicEntity() {}

    public TopicEntity(int icons, String names) {
        this.icons = icons;
        this.names = names;
    }

    // Setters
    public void setIcons(int icons) {
        this.icons = icons;
    }

    public void setNames(String names) {
        this.names = names;
    }

    // Getters
    public int getIcons() {
        return icons;
    }

    public String getNames() {
        return names;
    }
}


