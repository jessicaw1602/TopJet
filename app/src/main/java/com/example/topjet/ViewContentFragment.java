package com.example.topjet;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.topjet.Entities.ContentEntity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

// TODO - when the user clicks on the button to go to the next page, then it will transition...

public class ViewContentFragment extends Fragment {

    private static final String TAG = "ViewContent";

    TextView tvTopic, tvHeading, tvContentTitle, tvParaOne, tvParaTwo;
    ImageView ivOne, ivTwo;
    Button btNext, btBack;

    private List<ContentEntity> contentList; // this will be used to store all the retrieved data here...

    private ContentEntity currentPage; // currentPage = the page that the user will be on, and maxPages is the total number of pages
    private int pageCounter = 0; // will be used to count the current page
    private int maxPages; // number of pages listed in the contentList

    // Max is 2 images per page
    private static final String KEY_PAGE_NUM_ID = "pageNumId";
    private static final String KEY_HEADING = "heading";
    private static final String KEY_TOPIC_AREA = "topicArea";
    private static final String KEY_TITLE = "title";
    private static final String KEY_PARA_ONE = "paraOne";
    private static final String KEY_PARA_TWO = "paraTwo";
    private static final String KEY_IV_ONE = "ivOne";
    private static final String KEY_IV_TWO = "ivTwo";

    // Access FireStore & Storage
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_view_content, container, false);

        // Retrieve the fragment from SearchFragment
        String email = getArguments().getString("email");
        Log.d(TAG, "ViewContentFragment Email: " + email);

        String heading = getArguments().getString("heading");
        Log.d(TAG, "ViewContentFragment heading: " + heading); // will pass in 'Symbols', 'Materials',

        tvTopic = view.findViewById(R.id.tvTopic);
        tvHeading = view.findViewById(R.id.tvHeading);
        tvContentTitle = view.findViewById(R.id.tvContentTitle);
        tvParaOne = view.findViewById(R.id.tvParaOne);
        tvParaTwo = view.findViewById(R.id.tvParaTwo);
        btNext = view.findViewById(R.id.btNext);
        btBack = view.findViewById(R.id.btBack);

        // For the images...
        ivOne = view.findViewById(R.id.ivOne);
        ivTwo = view.findViewById(R.id.ivTwo);

        contentList = new ArrayList<>();
        // retrieve all the values from the database, where heading = Collection Reference
        retrieveData(email, heading);

            btNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pageCounter++;
                    getContent(email); // go to the next page. Now page counter = 2
                    Log.d(TAG, "new page counter is: " + pageCounter);
                }
            }); // end of btNext.setOnClickListener

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pageCounter == 0){
                    // go back to SearchFragment
                    goToSearchFragment(email);
                } else if (pageCounter >= 1 && pageCounter < maxPages){
                    Log.d(TAG, "The initial pageCounter is: " + pageCounter);

                    pageCounter-=1;

                    Log.d(TAG, "The after pageCounter is: " + pageCounter);

                    getContent(email); // go to the next page. Now page counter = 2
                }
            }
        }); // end of btBack.setOnClickListener

            return view;
        } // end of onCreate method

    private void retrieveData (String email, String heading) {
        database.collection(heading).get() // return all the values from the collection
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Log.d(TAG, document.getId() + " => " + document.getData());

                                // Create a new Comment
                                ContentEntity contentEntity = new ContentEntity();

                                String pageNumId = document.getString(KEY_PAGE_NUM_ID);
                                String heading = document.getString(KEY_HEADING);
                                String topicArea = document.getString(KEY_TOPIC_AREA);
                                String title = document.getString(KEY_TITLE);
                                String paraOne = document.getString(KEY_PARA_ONE);
                                String paraTwo = document.getString(KEY_PARA_TWO);
                                String imageOne = document.getString(KEY_IV_ONE);
                                String imageTwo = document.getString(KEY_IV_TWO);

                                // Log.d(TAG, "Page Number is " + pageNumId);

                                // create an array list to store all the values in it.
                                contentList.add(new ContentEntity(pageNumId, topicArea, heading, title, paraOne, paraTwo, imageOne, imageTwo));
                            }
                            maxPages = contentList.size(); // get the number of maximum pages
                            Log.d(TAG, "The number of pages is: " + maxPages);

                            // Retrieve all the images and set text
                            getContent(email);

                        } else {
                            Log.d(TAG, "Unsuccessful in Retrieving Data");
                        }
                    }
                }); // end of FireStore Query

    } // end of retrieveData method

    // As the user goes through the content
    private void getContent(String email){
        Log.d(TAG, "The initial pageCounter is: " + pageCounter);

            if (pageCounter < maxPages){ // if 0 < 8 (which is true) then
                currentPage = contentList.get(pageCounter); // get the object at the first index = 0

                // StorageReference storageRef = storage.getReference(currentPage.getPageNumId()).child(currentPage.getPageNumId() + ".png");
                // No longer using StorageReference, and instead we are using drawable, as you cannot retrieve multiple images from Storage

                int imageIdOne = getResources().getIdentifier(
                        "@drawable/" + currentPage.getPageNumId().toLowerCase() + "1",
                        null, getContext().getPackageName());
                Log.d(TAG, "print " + currentPage.getPageNumId().toLowerCase() + "1");

                int imageIdTwo = getResources().getIdentifier(
                        "@drawable/" + currentPage.getPageNumId().toLowerCase() + "2",
                        null, getContext().getPackageName());
                Log.d(TAG, "print " + currentPage.getPageNumId().toLowerCase() + "2");

                tvTopic.setText(currentPage.getTopicArea());
                tvHeading.setText(currentPage.getHeading());
                tvContentTitle.setText(currentPage.getTitle());
                tvParaOne.setText(currentPage.getParaOne());
                tvParaTwo.setText(currentPage.getParaTwo());
                ivOne.setImageResource(imageIdOne);
                ivTwo.setImageResource(imageIdTwo);

            } else {
                Log.d(TAG, "Finished Reading!! Congratulations :)");
                btNext.setText("Go To Quiz.");
                btNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finishReading(email);
                    }
                }); // end of onClickListener
            }
    } // end of getContent method

    private void finishReading(String email){ //TODO - we also want to be able to pass the topicArea in
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        QuizFragment quizFragment = new QuizFragment();

        // send bundle for the Search Fragment
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
//        bundle.putString("topicArea", ) // TODO - finish this off.
        quizFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragment_frame, quizFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    } // end of finishReading method

    private void goToSearchFragment (String email){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SearchFragment searchFragment = new SearchFragment();

        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        searchFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragment_frame, searchFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    } // end of goToSearchFragment

}
