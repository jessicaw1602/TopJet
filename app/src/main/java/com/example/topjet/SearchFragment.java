package com.example.topjet;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topjet.Entities.TopicEntity;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/*
* IMAGE RECYCLERVIEW CODE ADAPTED FROM: https://www.youtube.com/watch?v=Ph3Ek6cLS4M
* QUERY SNAPSHOT DOCUMENTATION: https://googleapis.dev/nodejs/firestore/3.7.5/QuerySnapshot.html#isEqual
* */

public class SearchFragment extends Fragment {
    private static final String TAG = "SearchFragment";

    // Access FireStore
    private FirebaseFirestore database = FirebaseFirestore.getInstance();

    private static final String KEY_EMAIL = "email";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_IDENTIFY = "cbIdentify";
    private static final String KEY_SCORE = "score";

    private static final String KEY_QUIZ_TOPIC = "quizTopic";

    // Initialise RecyclerView
    private RecyclerView rvArts, rvCulture, rvValues;

    private List<TopicEntity> topicArtList;
    private List<TopicEntity> topicCultureList;
    private List<TopicEntity> topicValueList;

    private TopicAdapter artAdapter, cultureAdapter, valueAdapter;

    private TextView tvArtsProgress, tvCultureProgress, tvValuesProgress; // this is used to display the % done
    private ProgressBar artsProgressBar, cultureProgressBar, valuesProgressBar;

    // Count how many quizzes the user has completed
    int symbolsCounter, materialsCounter = 0, ceremonyCounter = 0, landCounter = 0, familyCounter = 0;
    int langCounter = 0, dreamCounter = 0, sitesCounter = 0, spiritCounter = 0;

//    int symbolsCounter, materialsCounter, ceremonyCounter, landCounter, familyCounter;
//    int langCounter, dreamCounter, sitesCounter, spiritCounter;

    // set the total number of quizzes by each category
    final String artsTotal = "2";
    final String cultureTotal = "4";
    final String valuesTotal = "3";

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

// View the progress of each of the text
//         getArtsData(email);
//        getCultureData(email);
//        getValuesData(email);

        getSymbolData(email);

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

        artsProgressBar.setMax(2);
        cultureProgressBar.setMax(4);
        valuesProgressBar.setMax(3);

        Log.d(TAG, "counter in main = " + symbolsCounter);

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

    private void getSymbolData (String email) {
        CollectionReference postsRef = database.collection("Quiz Attempts");
        // perform query to get all the attempts made by the user.
        Query quizArtSymbols = postsRef.whereEqualTo("email", email).whereEqualTo("quizTopic", "Arts Symbols Quiz");

        // You cannot perform unique queries with Firestore database...
        quizArtSymbols.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (!value.isEmpty()){
                    symbolsCounter = 1;
                } else {
                    symbolsCounter = 0;
                } getMaterialData(email, symbolsCounter);
            }
        });


    } // end of getArtsData method

    private void getMaterialData(String email, int symbolsCounter) {
        CollectionReference postsRef = database.collection("Quiz Attempts");
        // perform query to get all the attempts made by the user.
        Query quizArtMats = postsRef.whereEqualTo("email", email).whereEqualTo("quizTopic", "Arts Materials Quiz");

        // You cannot perform unique queries with Firestore database...
        quizArtMats.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (!value.isEmpty()){
                    materialsCounter = 1;
                } else {
                    materialsCounter = 0;
                } getCeremonyData(email, symbolsCounter, materialsCounter);
            }
        });
    }

    private void getCeremonyData(String email, int symbolsCounter, int materialsCounter) {
        CollectionReference postsRefs = database.collection("Quiz Attempts");
        Query quizCultureCeremony = postsRefs.whereEqualTo("email", email).whereEqualTo("quizTopic", "Culture Ceremony Quiz");

        quizCultureCeremony.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (!value.isEmpty()){
                    ceremonyCounter = 1;
                } else {
                    ceremonyCounter = 0;
                } getFamilyData(email, symbolsCounter, materialsCounter, ceremonyCounter);
            }
        });

    }

    private void getFamilyData(String email, int symbolsCounter, int materialsCounter, int ceremonyCounter) {
        CollectionReference postsRefs = database.collection("Quiz Attempts");
        Query quizCultureFamily = postsRefs.whereEqualTo("email", email).whereEqualTo("quizTopic", "Culture Family and Kinship Quiz");

        quizCultureFamily.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (!value.isEmpty()){
                    familyCounter = 1;
                } else {
                    familyCounter = 0;
                } getLandData(email, symbolsCounter, materialsCounter, ceremonyCounter, familyCounter);
            }
        });
    }

    private void getLandData(String email, int symbolsCounter, int materialsCounter, int ceremonyCounter, int familyCounter) {
        CollectionReference postsRefs = database.collection("Quiz Attempts");
        Query quizCultureLand = postsRefs.whereEqualTo("email", email).whereEqualTo("quizTopic", "Culture Land Quiz");
        quizCultureLand.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (!value.isEmpty()){
                    landCounter = 1;
                } else {
                    landCounter = 0;
                } getLangData(email, symbolsCounter, materialsCounter, ceremonyCounter, familyCounter, landCounter);
            }
        });
    }

    private void getLangData(String email, int symbolsCounter, int materialsCounter, int ceremonyCounter, int familyCounter, int landCounter) {
        CollectionReference postsRefs = database.collection("Quiz Attempts");
        Query quizCultureLang = postsRefs.whereEqualTo("email", email).whereEqualTo("quizTopic", "Culture Language Quiz");
        quizCultureLang.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (!value.isEmpty()){
                    langCounter = 1;
                } else {
                    langCounter = 0;
                } getDreamData(email, symbolsCounter, materialsCounter, ceremonyCounter, familyCounter, landCounter, langCounter);
            }
        });
    }

    private void getDreamData(String email, int symbolsCounter, int materialsCounter, int ceremonyCounter, int familyCounter, int landCounter, int langCounter) {
        CollectionReference postsRefs = database.collection("Quiz Attempts");
        Query quizValuesDream = postsRefs.whereEqualTo("email", email).whereEqualTo("quizTopic", "Values Dreamtime Quiz");

        quizValuesDream.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (!value.isEmpty()){
                    dreamCounter = 1;
                } else {
                    dreamCounter = 0;
                } getSiteData(email, symbolsCounter, materialsCounter, ceremonyCounter, familyCounter, landCounter, langCounter, dreamCounter);
            }
        });

    }

    private void getSiteData(String email, int symbolsCounter, int materialsCounter, int ceremonyCounter, int familyCounter, int landCounter, int langCounter, int dreamCounter) {
         CollectionReference postsRefs = database.collection("Quiz Attempts");
         Query quizValuesSites = postsRefs.whereEqualTo("email", email).whereEqualTo("quizTopic", "Values Sites Quiz");

         quizValuesSites.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (!value.isEmpty()){
                    sitesCounter = 1;
                } else {
                    sitesCounter = 0;
                } getSpiritData(email, symbolsCounter, materialsCounter, ceremonyCounter, familyCounter, landCounter, langCounter, dreamCounter, sitesCounter);
            }
        });

    }

    private void getSpiritData(String email, int symbolsCounter, int materialsCounter, int ceremonyCounter, int familyCounter, int landCounter, int langCounter, int dreamCounter, int sitesCounter) {
        CollectionReference postsRefs = database.collection("Quiz Attempts");
        Query quizValuesSpirit = postsRefs.whereEqualTo("email", email).whereEqualTo("quizTopic", "Values Spirituality Quiz");

        quizValuesSpirit.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (!value.isEmpty()){
                    spiritCounter = 1;
                } else {
                    spiritCounter = 0;
                } setListValues(email, symbolsCounter, materialsCounter, ceremonyCounter, familyCounter, landCounter, langCounter, dreamCounter, sitesCounter, spiritCounter);
            }
        });
    }


    // process the values for the RecyclerView

    private void setListValues(String email, int symbolsCounter, int materialsCounter, int ceremonyCounter, int familyCounter, int landCounter, int langCounter, int dreamCounter, int sitesCounter, int spiritCounter){
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


        // Progress Bar Values
        int[] artProgress = {symbolsCounter, materialsCounter};
        int[] cultureProgress = {landCounter, familyCounter, ceremonyCounter, langCounter};
        int[] valueProgress = {dreamCounter, sitesCounter, spiritCounter};

        // Display all the information.
        for (int i = 0; i < artIcons.length; i++){
            TopicEntity topicEntities = new TopicEntity(artIcons[i], artNames[i], artProgress[i]);
            topicArtList.add(topicEntities);
        }

        for (int i = 0; i < cultureIcons.length; i++){
            TopicEntity topicEntities = new TopicEntity(cultureIcons[i], cultureNames[i], cultureProgress[i]);
            topicCultureList.add(topicEntities);
        }

        for (int i = 0; i < valueIcons.length; i++){
            TopicEntity topicEntities = new TopicEntity(valueIcons[i], valueNames[i], valueProgress[i]);
            topicValueList.add(topicEntities);
        }

    } // end of setListValues Method




}

