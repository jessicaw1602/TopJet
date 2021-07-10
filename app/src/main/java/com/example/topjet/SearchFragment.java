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

    //TODO - delete later
    Button btArts;

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
        LinearLayoutManager artLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager cultureLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager valueLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        rvArts.setLayoutManager(artLayout);
        rvCulture.setLayoutManager(cultureLayout);
        rvValues.setLayoutManager(valueLayout);
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
                // TODO - finish off this
//                openDetailActivity(topicName, email);
            }
        }; // end of DiscussionAdapter.RecyclerViewClickListener

        artAdapter = new TopicAdapter(topicArtList, listener);
        cultureAdapter = new TopicAdapter(topicCultureList, listener);
        valueAdapter = new TopicAdapter(topicValueList, listener);

        rvArts.setAdapter(artAdapter);
        rvCulture.setAdapter(cultureAdapter);
        rvValues.setAdapter(valueAdapter);

        //TODO - delete this later - this will show the Arts > Symbol info...
        btArts = view.findViewById(R.id.btArts);
        btArts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create Bundle
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                ViewContentFragment viewContent = new ViewContentFragment();

                // send bundle for the Search Fragment
                Bundle bundle = new Bundle();
                bundle.putString("email", email);
                viewContent.setArguments(bundle);

                fragmentTransaction.replace(R.id.fragment_frame, viewContent);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }); // end of setOnClickListener

        return view;
    } // end of onCreate method

}

