package com.example.topjet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DiscussionAdapter extends RecyclerView.Adapter<DiscussionAdapter.DiscussionViewHolder> {

    private static final String TAG = "DiscussionAdapter";

    private List<DiscussionEntity> myDiscussion;

    // Constructor
    public DiscussionAdapter(List<DiscussionEntity> discussion){
        myDiscussion = discussion;
    }

    public static class DiscussionViewHolder extends RecyclerView.ViewHolder{

        private TextView tvTitle, tvUserAndDate, tvPostTags, tvShortDesc;

        public DiscussionViewHolder(@NonNull View itemView) {
            super(itemView);

            // Connect with the xml file of the layout file rv_discussion_single
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvUserAndDate = itemView.findViewById(R.id.tvUserAndDate);
            tvPostTags = itemView.findViewById(R.id.tvPostTag);
            tvShortDesc = itemView.findViewById(R.id.tvContent);
        }

    } // end of DiscussionViewHolder method

    @NonNull
    @Override
    public DiscussionAdapter.DiscussionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_discussion_single, parent, false);
        return new DiscussionViewHolder(v); // this is the view that will be created and must match from DiscussionViewHolder
    }

    @Override
    public void onBindViewHolder(@NonNull DiscussionAdapter.DiscussionViewHolder holder, int position) {
        // What do you want to display - connect and display all the FireStore information

        DiscussionEntity discussionEntity = myDiscussion.get(position); // assign each object in the array with a position
        // Assign each discussionEntity with the
        holder.tvTitle.setText(discussionEntity.getTitle());
        holder.tvUserAndDate.setText(discussionEntity.getUsername() + " | " + discussionEntity.getDate());
        holder.tvPostTags.setText(discussionEntity.getPostTag());
        holder.tvShortDesc.setText(discussionEntity.getShortDescription());

        holder.itemView.setTag(discussionEntity.getTitle());
    }

    @Override
    public int getItemCount() {
        return myDiscussion.size(); // return all the elements in the list
    }

}
