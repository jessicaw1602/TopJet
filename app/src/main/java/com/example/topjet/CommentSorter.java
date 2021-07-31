package com.example.topjet;

import com.example.topjet.Entities.CommentEntity;

import java.util.Comparator;

public class CommentSorter implements Comparator<CommentEntity> {

    @Override
    public int compare(CommentEntity o1, CommentEntity o2) {
        return o1.getDate().compareTo(o2.getDate());
    }
}
