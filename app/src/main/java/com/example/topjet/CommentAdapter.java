package com.example.topjet;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topjet.Entities.CommentEntity;
import com.example.topjet.Entities.DiscussionEntity;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private static final String TAG = "DiscussionAdapter";

    private List<CommentEntity> myComment;

    // Constructor
    public CommentAdapter(List<CommentEntity> comment ){
        myComment = comment;
    }

    @NonNull
    @Override
    public CommentAdapter.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_comment_single, parent, false);
        return new CommentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.CommentViewHolder holder, int position) {
        CommentEntity commentEntity = myComment.get(position); // assign each object in the array with a position

        holder.tvCommentUser.setText(commentEntity.getUsername());
        holder.tvCommentDate.setText(commentEntity.getDate());
        holder.tvCommentContent.setText(commentEntity.getComment());

        holder.itemView.setTag(commentEntity.getDocId());
        Log.d(TAG, "DocId from CommentAdapter: " + commentEntity.getDocId());
    }

    @Override
    public int getItemCount() {
        return myComment.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {

        private TextView tvCommentUser, tvCommentDate, tvCommentContent;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCommentUser = itemView.findViewById(R.id.tvCommentUser);
            tvCommentDate = itemView.findViewById(R.id.tvCommentDate);
            tvCommentContent = itemView.findViewById(R.id.tvCommentContent);
        }
    } // end of DiscussionViewHolder method

}
