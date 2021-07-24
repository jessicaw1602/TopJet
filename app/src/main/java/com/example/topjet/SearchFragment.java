package com.example.topjet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
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
    private static final String TAG = "SearchFragment";

    // Initialise RecyclerView
    private RecyclerView rvArts, rvCulture, rvValues;

    private List<TopicEntity> topicArtList;
    private List<TopicEntity> topicCultureList;
    private List<TopicEntity> topicValueList;

    private TopicAdapter artAdapter, cultureAdapter, valueAdapter;

    private TextView tvArtsProgress, tvCultureProgress, tvValuesProgress; // this is used to display the % done
    private ProgressBar artsProgressBar, cultureProgressBar, valuesProgressBar;

    // Count how many quizzes the user has completed
    int artsCounter = 0;
    int cultureCounter = 0;
    int valuesCounter = 0;

    // set the total number of quizzes by each category
    int artsTotal = 2;
    int cultureTotal = 4;
    int valuesTotal = 3;

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

        artsProgressBar = view.findViewById(R.id.artsProgressBar);
        cultureProgressBar = view.findViewById(R.id.cultureProgressBar);
        valuesProgressBar = view.findViewById(R.id.valuesProgressBar);

        tvArtsProgress = view.findViewById(R.id.tvArtsProgress);
        tvCultureProgress = view.findViewById(R.id.tvCultureProgress);
        tvValuesProgress = view.findViewById(R.id.tvValuesProgress);

        // Enable Action bar
        setHasOptionsMenu(true);

        // Create new ArrayLists
        topicArtList = new ArrayList<TopicEntity>();
        topicCultureList = new ArrayList<TopicEntity>();
        topicValueList = new ArrayList<TopicEntity>();

        new Thread(new Runnable() {
            @Override
            public void run() {

                // set Horizontal RecyclerView Layout
                LinearLayoutManager artLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                LinearLayoutManager cultureLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                LinearLayoutManager valuesLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

                rvArts.setLayoutManager(artLayout);
                rvCulture.setLayoutManager(cultureLayout);
                rvValues.setLayoutManager(valuesLayout);
                rvArts.setHasFixedSize(true);
                rvCulture.setHasFixedSize(true);
                rvValues.setHasFixedSize(true);
                rvArts.setItemAnimator(new DefaultItemAnimator());
                rvCulture.setItemAnimator(new DefaultItemAnimator());
                rvValues.setItemAnimator(new DefaultItemAnimator());

                int[] artIcons = {R.drawable.content_arts_symbols, R.drawable.content_arts_material};
                int[] cultureIcons = {R.drawable.content_culture_land, R.drawable.content_culture_family, R.drawable.content_culture_ceremony,R.drawable.content_culture_language};
                int[] valueIcons = {R.drawable.content_values_dreamtime, R.drawable.content_values_sacred, R.drawable.content_values_spirituality};

                String[] artNames = {"Symbols", "Materials"};
                String[] cultureNames = {"Land", "Family and Kinship", "Ceremony", "Language"};
                String[] valueNames = {"Dreamtime Stories", "Sacred Sites", "Spirituality"};

                // Display all the information.
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
            }
        }).start(); // end of new Thread

        // View the progress of each of the text
        getProgress();

        return view;
    } // end of onCreate method

    private void getProgress() {

        artsProgressBar.setMax(2);
        cultureProgressBar.setMax(4);
        valuesProgressBar.setMax(3);

        // set the progress bar...
        // To do this, we want to get the database values...
        artsProgressBar.setProgress(1);
        cultureProgressBar.setProgress(1);
        valuesProgressBar.setProgress(1);


    } // end of getProgress method

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
    //Action bar go back button function
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                    // Navigate to settings screen
                break;
            case R.id.fragment_frame:
                    // Save profile changes
                return true;
            default:
                return false;
            }
            return super.onOptionsItemSelected(item);
        }
}

