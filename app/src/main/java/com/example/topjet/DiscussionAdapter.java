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
    private RecyclerViewClickListener myListener;

    public interface RecyclerViewClickListener {
        void onClick(View view, String title);
    }

    // Constructor
    public DiscussionAdapter(List<DiscussionEntity> discussion, RecyclerViewClickListener listener){
        myDiscussion = discussion;
        myListener = listener;
    }

    public DiscussionAdapter(List<DiscussionEntity> myDiscussion) {
        this.myDiscussion = myDiscussion;
    }

    public static class DiscussionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvTitle, tvUserAndDate, tvPostTag, tvShortDesc;
        private RecyclerViewClickListener myListener;

        public DiscussionViewHolder(@NonNull View itemView, RecyclerViewClickListener listener) {
            super(itemView);
            this.myListener = listener;
            itemView.setOnClickListener(this);

            // Connect with the xml file of the layout file rv_discussion_single
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvUserAndDate = itemView.findViewById(R.id.tvUserAndDate);
            tvPostTag = itemView.findViewById(R.id.tvPostTag);
            tvShortDesc = itemView.findViewById(R.id.tvContent);
        }

        @Override
        public void onClick(View v) {
            myListener.onClick(v, v.getTag().toString());
        }

    } // end of DiscussionViewHolder method

    @NonNull
    @Override
    public DiscussionAdapter.DiscussionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_discussion_single, parent, false);
        return new DiscussionViewHolder(v, myListener); // this is the view that will be created and must match from DiscussionViewHolder
    }

    @Override
    public void onBindViewHolder(@NonNull DiscussionAdapter.DiscussionViewHolder holder, int position) {
        // What do you want to display - connect and display all the FireStore information

        DiscussionEntity discussionEntity = myDiscussion.get(position); // assign each object in the array with a position
        // Assign each discussionEntity with the
        holder.tvTitle.setText(discussionEntity.getTitle());
        holder.tvUserAndDate.setText(discussionEntity.getUsername() + " | " + discussionEntity.getDate());
        holder.tvPostTag.setText(discussionEntity.getPostTag());
        holder.tvShortDesc.setText(discussionEntity.getShortDesc());

        holder.itemView.setTag(discussionEntity.getTitle());
    }

    @Override
    public int getItemCount() {
        return myDiscussion.size(); // return all the elements in the list
    }

}
