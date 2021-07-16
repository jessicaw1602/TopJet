package com.example.topjet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topjet.Entities.DiscussionEntity;
import com.example.topjet.Entities.TopicEntity;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/* IMAGE RECYCLERVIEW CODE ADAPTED FROM: https://www.youtube.com/watch?v=Ph3Ek6cLS4M */

public class SearchFragment extends Fragment {

    // Initialise RecyclerView
    private RecyclerView rvArts, rvCulture, rvValues;

    private List<TopicEntity> topicArtList;
    private List<TopicEntity> topicCultureList;
    private List<TopicEntity> topicValueList;

    private TopicAdapter artAdapter, cultureAdapter, valueAdapter;

    private static final String TAG = "SearchFragment";

    // Access FireStore
    private FirebaseFirestore database = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false); // inflate Fragment's view

        // Retrieve the fragment from HomeActivity
        String email = getArguments().getString("email");
        Log.d(TAG, "Search Fragment Email: " + email);

        rvArts = view.findViewById(R.id.rvArts);
        rvCulture = view.findViewById(R.id.rvCulture);
        rvValues = view.findViewById(R.id.rvValues);

        // Create new ArrayLists
        topicArtList = new ArrayList<TopicEntity>();
        topicCultureList = new ArrayList<TopicEntity>();
        topicValueList = new ArrayList<TopicEntity>();

        // set Horizontal RecyclerView Layout
        LinearLayoutManager layout = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        rvArts.setLayoutManager(layout);
        rvCulture.setLayoutManager(layout);
        rvValues.setLayoutManager(layout);
        rvArts.setHasFixedSize(true);
        rvCulture.setHasFixedSize(true);
        rvValues.setHasFixedSize(true);
        rvArts.setItemAnimator(new DefaultItemAnimator());
        rvCulture.setItemAnimator(new DefaultItemAnimator());
        rvValues.setItemAnimator(new DefaultItemAnimator());

        //TODO - replace these images with icons for each particular topic
        int[] artIcons = {R.drawable.ic_baseline_home_24, R.drawable.ic_baseline_person_24};
        int[] cultureIcons = {R.drawable.ic_baseline_search_24, R.drawable.ic_baseline_home_24, R.drawable.ic_baseline_person_24, R.drawable.ic_launcher_background};
        int[] valueIcons = {R.drawable.ic_baseline_home_24, R.drawable.ic_baseline_person_24, R.drawable.ic_baseline_search_24};

        String[] artNames = {"Symbols", "Materials"};
        String[] cultureNames = {"Land", "Family and Kinship", "Ceremony", "Language"};
        String[] valueNames = {"Dreamtime Stories", "Sacred Sites", "Spirituality"};

        for (int i = 0; i < artIcons.length; i++){
            TopicEntity topicEntities = new TopicEntity(artIcons[i], artNames[i]);
            topicArtList.add(topicEntities);
        }

        for (int i = 0; i < cultureIcons.length; i++){
            TopicEntity topicEntities = new TopicEntity(cultureIcons[i], cultureNames[i]);
            topicCultureList.add(topicEntities);
        }

        for (int i = 0; i < valueIcons.length; i++){
            TopicEntity topicEntities = new TopicEntity(valueIcons[i], valueNames[i]);
            topicValueList.add(topicEntities);
        }

        TopicAdapter.RecyclerViewClickListener listener = new TopicAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, String topicName) {
                Log.d(TAG, "DiscussionFragment title: " + topicName);
                getCollectionName (topicName, email);
            }
        }; // end of DiscussionAdapter.RecyclerViewClickListener

        artAdapter = new TopicAdapter(topicArtList, listener);
        cultureAdapter = new TopicAdapter(topicCultureList, listener);
        valueAdapter = new TopicAdapter(topicValueList, listener);

        rvArts.setAdapter(artAdapter);
        rvCulture.setAdapter(cultureAdapter);
        rvValues.setAdapter(valueAdapter);

        return view;
    } // end of onCreate method

    private void getCollectionName(String topicName, String email){
        String heading;

        // do a switch statement to determine which collection to go to...
        switch(topicName){
            case "Symbols":
                heading = "Arts Symbols";
                Log.d(TAG, "The Collection Reference is: " + heading);
                openViewContent(heading, email);
                break;

            case "Materials":
                heading = "Arts Materials";
                openViewContent(heading, email);
                break;

            case "Land":
                heading = "Culture Land";
                openViewContent(heading, email);
                break;

            case "Family and Kinship":
                heading = "Culture Family and Kinship";
                openViewContent(heading, email);
                break;

            case "Ceremony":
                heading = "Culture Ceremony";
                openViewContent(heading, email);
                break;

            case "Language":
                heading = "Culture Language";
                openViewContent(heading, email);
                break;

            case "Dreamtime Stories":
                heading = "Values Dreamtime";
                openViewContent(heading, email);
                break;

            case "Sacred Sites":
                heading = "Values Sites";
                openViewContent(heading, email);
                break;

            case "Spirituality":
                heading = "Values Spirituality";
                openViewContent(heading, email);
                break;

        } // end of switch statement

    } // end of openViewContent method

    private void openViewContent(String heading, String email){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ViewContentFragment viewContent = new ViewContentFragment();

        // send bundle to ViewContentFragment
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        bundle.putString("heading", heading); //topicName will be the 'heading' in the collection
        viewContent.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragment_frame, viewContent);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    } // end of openViewContent page
}

