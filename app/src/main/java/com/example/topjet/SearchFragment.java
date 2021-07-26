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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* IMAGE RECYCLERVIEW CODE ADAPTED FROM: https://www.youtube.com/watch?v=Ph3Ek6cLS4M */

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
    int symbolsCounter = 0, materialsCounter = 0, ceremonyCounter = 0, landCounter = 0, familyCounter = 0;
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

        artsProgressBar.setMax(2);
        cultureProgressBar.setMax(4);
        valuesProgressBar.setMax(3);

        // View the progress of each of the text
        getArtsData(email);
        getCultureData(email);
        getValuesData(email);

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

    private void getArtsData (String email) {

        CollectionReference postsRef = database.collection("Quiz Attempts");
        // perform query to get all the attempts made by the user.
        Query quizArtSymbols = postsRef.whereEqualTo("email", email).whereEqualTo("quizTopic", "Arts Symbols Quiz");
        Query quizArtsMaterials = postsRef.whereEqualTo("email", email).whereEqualTo("quizTopic", "Arts Materials Quiz");

        // You cannot perform unique queries with Firestore database...
        quizArtSymbols.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (!value.isEmpty()){
                    symbolsCounter = 1;
                } else {
                    symbolsCounter = 0;
                } setArtData(symbolsCounter);
            }
        });

        quizArtsMaterials.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (!value.isEmpty()){
                    materialsCounter = 1;
                } else {
                    materialsCounter = 0;
                }
            }
        });


    } // end of getArtsData method

    private void getCultureData(String email){
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                CollectionReference postsRefs = database.collection("Quiz Attempts");

                Query quizCultureCeremony = postsRefs.whereEqualTo("email", email).whereEqualTo("quizTopic", "Culture Ceremony Quiz");
                Query quizCultureFamily = postsRefs.whereEqualTo("email", email).whereEqualTo("quizTopic", "Culture Family and Kinship Quiz");
                Query quizCultureLand = postsRefs.whereEqualTo("email", email).whereEqualTo("quizTopic", "Culture Land Quiz");
                Query quizCultureLan = postsRefs.whereEqualTo("email", email).whereEqualTo("quizTopic", "Culture Language Quiz");

                quizCultureCeremony.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (!value.isEmpty()){
                            ceremonyCounter = 1;
                        } else {
                            ceremonyCounter = 0;
                        }
                        setCultureData(ceremonyCounter);
                    }
                });

                quizCultureFamily.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (!value.isEmpty()){
                            familyCounter = 1;
                        } else {
                            familyCounter = 0;
                        }
                    }
                });

                quizCultureLand.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (!value.isEmpty()){
                            landCounter = 1;
                        } else {
                            landCounter = 0;
                        }// end of if-statement

                    }
                });

                quizCultureLan.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (!value.isEmpty()){
                            langCounter = 1;
                        } else {
                            langCounter = 0;
                        }// end of if-statement
                    }
                });
            }
        }); // end of thread

    } // end of getCultureData method

    private void getValuesData(String email) {
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                CollectionReference postsRef = database.collection("Quiz Attempts");

                Query quizValuesDream = postsRef.whereEqualTo("email", email).whereEqualTo("quizTopic", "Values Dreamtime Quiz");
                Query quizValuesSites = postsRef.whereEqualTo("email", email).whereEqualTo("quizTopic", "Values Sites Quiz");
                Query quizValuesSpirit = postsRef.whereEqualTo("email", email).whereEqualTo("quizTopic", "Values Spirituality Quiz");

                quizValuesDream.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (!value.isEmpty()){
                            dreamCounter = 1;
                        } else {
                            dreamCounter = 0;
                        } setValueData(dreamCounter);
                    }
                });

                quizValuesSites.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (!value.isEmpty()){
                            sitesCounter = 1;
                        } else {
                            sitesCounter = 0;
                        }
                    }
                });

                quizValuesSpirit.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (!value.isEmpty()){
                            spiritCounter = 1;
                        } else {
                            spiritCounter = 0;
                        }
                    }
                });

            }
        }); // end of thread

    } // end of getValuesData method

    private void setArtData (int symbolsCounter){
        String artProgressTotal = String.valueOf(materialsCounter + symbolsCounter);
        artsProgressBar.setProgress((materialsCounter + symbolsCounter));
        tvArtsProgress.setText(artProgressTotal + "/" + artsTotal);
    }

    private void setCultureData(int ceremonyCounter) {
        String cultureProgressTotal = String.valueOf(ceremonyCounter + familyCounter + landCounter + langCounter);
        cultureProgressBar.setProgress((ceremonyCounter + familyCounter + landCounter + langCounter));
        tvCultureProgress.setText(cultureProgressTotal + "/" + cultureTotal);
    }

    private void setValueData(int dreamCounter) {
        String valueProgressTotal = String.valueOf((dreamCounter + sitesCounter + spiritCounter));
        valuesProgressBar.setProgress((dreamCounter + sitesCounter + spiritCounter));
        tvValuesProgress.setText(valueProgressTotal + "/" + valuesTotal);

    }

}

