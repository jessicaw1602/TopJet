package com.example.topjet;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topjet.Entities.TopicEntity;

import java.util.List;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {

    private static final String TAG = "TopicAdapter";

    private List<TopicEntity> myTopic;
    private TopicAdapter.RecyclerViewClickListener myListener;

    public interface RecyclerViewClickListener {
        void onClick(View view, String topicName);
    }

    // Constructor
    public TopicAdapter(List<TopicEntity> topic, TopicAdapter.RecyclerViewClickListener listener){
        myTopic = topic;
        myListener = listener;
    }

    @NonNull
    @Override
    public TopicAdapter.TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_topic_single, parent, false);
        return new TopicAdapter.TopicViewHolder(v, myListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicAdapter.TopicViewHolder holder, int position) {

        TopicEntity topicEntity = myTopic.get(position); // assign each object in the array with a position

        holder.tvTopicArea.setText(topicEntity.getNames());
        holder.ivIcon.setImageResource(topicEntity.getIcons());

        holder.itemView.setTag(topicEntity.getNames());
        // Log.d(TAG, "Name from TopicAdapter: " + topicEntity.getNames());
        // Should return Symbols, Materials, Land, Family, Ceremony, Language, Dreamtime, Sacred Sites, Spirituality
    }

    @Override
    public int getItemCount() {
        return myTopic.size();
    }

    public static class TopicViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvTopicArea;
        private ImageView ivIcon;
        private TopicAdapter.RecyclerViewClickListener myListener;

        public TopicViewHolder(@NonNull View itemView, TopicAdapter.RecyclerViewClickListener listener) {
            super(itemView);
            this.myListener = listener;
            itemView.setOnClickListener(this);

            tvTopicArea = itemView.findViewById(R.id.tvTopicArea);
            ivIcon = itemView.findViewById(R.id.ivIcon);
        }

        @Override
        public void onClick(View v) {
            myListener.onClick(v, (String)(v.getTag()));
            Log.d(TAG, "Topic Adapter onClick: " + v.getTag().toString());
        }

    } // end of TopicViewHolder method
}
