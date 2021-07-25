package com.example.topjet;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.app.AlertDialog;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.topjet.Entities.ContentEntity;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class GameFragment extends Fragment {

    private static final String TAG = "QuizFragment";

    private static final String KEY_USERNAME = "username";

    Button didgeridooButton;
    Button headbandButton;
    Button ceremonyButton;

    EditText userInputOne;
    EditText userInputTwo;
    EditText userInputThree;
    Button checkAnswersButton;

    Button uluruButton;
    Button bodyButton;
    Button spearButton;

    Button ochreButton;
    Button artButton;
    Button stencilButton;

    Button nextButton;
    ImageView ivGame;
    TextView tvTitle;

    // Access FireStore
    private FirebaseFirestore database = FirebaseFirestore.getInstance();

    private List<ContentEntity> contentList; // this will be used to store all the retrieved data here...

    private ContentEntity currentPage; // currentPage = the page that the user will be on, and maxPages is the total number of pages
    private int pageCounter = 0; // will be used to count the current page
    private int maxPages = 5; // number of pages listed in the contentList

    // enable action bar

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // inflate Fragment's view
        View view = inflater.inflate(R.layout.fragment_game, container, false);

        // Retrieve the fragment from SearchFragment
        String email = getArguments().getString("email");
        Log.d(TAG, "GameFragment Email: " + email);

        String heading = getArguments().getString("heading");
        Log.d(TAG, "GameFragment heading: " + heading);
        //enable action bar
        setHasOptionsMenu(true);
        /*// Retrieve bundles
        String email = getArguments().getString("email");
        Log.d(TAG, "The user's email is: " + email);

        // I want to be able to receive the topicArea's text from ViewContentFragment, so i can retrieve the correct Quiz questions
        //TODO - finish this off -> from ViewContentFragment
        String topicAreas = getArguments().getString("topicArea");
        Log.d(TAG, "Quiz topicArea is: " + topicAreas);*/

        didgeridooButton = view.findViewById(R.id.didgeridooButton);
        headbandButton = view.findViewById(R.id.headbandButton);
        ceremonyButton = view.findViewById(R.id.ceremonyButton);

        userInputOne = view.findViewById(R.id.tvInputOne);
        userInputTwo = view.findViewById(R.id.tvInputTwo);
        userInputThree = view.findViewById(R.id.tvInputThree);
        checkAnswersButton = view.findViewById(R.id.checkAnswerButton);

        uluruButton = view.findViewById(R.id.uluruButton);
        bodyButton = view.findViewById(R.id.bodyButton);
        spearButton = view.findViewById(R.id.spearButton);

        ochreButton = view.findViewById(R.id.ochreButton);
        artButton = view.findViewById(R.id.artButton);
        stencilButton = view.findViewById(R.id.stencilButton);

        nextButton = view.findViewById(R.id.nextButton);
        ivGame = view.findViewById(R.id.ivGame);
        tvTitle = view.findViewById(R.id.tvTitle);


        didgeridooButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Didgeridoo button works!");
                makeAlert("Didgeridoo", "A wind instrument played to " +
                                "accompany ceremonies and evokes the magic of the Dreamtime",
                        "Done");
                //showPopup(view);
            }
        });

        headbandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Headband button works!");
                makeAlert("Headband", "One type of body adornment and may " +
                                "be worn as everyday items or reserved for ceremonial occasions ",
                        "Done");
                //showPopup(view);
            }
        });

        ceremonyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Ceremony button works!");
                makeAlert("Smoking Ceremony", "Conducted to acknowledge " +
                        "ancestors, ward off evil spirits and heal and cleanse the place " +
                        "and the participants by burning native plants", "Done");
                //showPopup(view);
            }
        });

        userInputOne.setVisibility(View.GONE);
        userInputTwo.setVisibility(View.GONE);
        userInputThree.setVisibility(View.GONE);
        checkAnswersButton.setVisibility(View.GONE);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageCounter++;
                switch(pageCounter){
                    case 1:
                        ivGame.setImageResource(R.drawable.ceremony);
                        tvTitle.setText("Name the objects:");
                        didgeridooButton.setVisibility(View.GONE);
                        headbandButton.setVisibility(View.GONE);
                        ceremonyButton.setVisibility(View.GONE);
                        nextButton.setVisibility(View.GONE);

                        userInputOne.setVisibility(View.VISIBLE);
                        userInputTwo.setVisibility(View.VISIBLE);
                        userInputThree.setVisibility(View.VISIBLE);
                        checkAnswersButton.setVisibility(View.VISIBLE);

                        checkAnswersButton.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                String getUserInputOne = userInputOne.getText().toString();
                                String getUserInputTwo = userInputTwo.getText().toString();
                                String getUserInputThree = userInputThree.getText().toString();
                                if(getUserInputOne.equals("didgeridoo") && getUserInputTwo.equals("headband") &&
                                        getUserInputThree.equals("smoking ceremony")){
                                    makeAlert("Correct!", "Please proceed to the next image.", "Ok");
                                    nextButton.setVisibility(View.VISIBLE);
                                    checkAnswersButton.setVisibility(View.GONE);
                                }else{
                                    makeAlert("Sorry!", "Please try again.", "Ok");
                                }
                            }
                        });
                        break;
                    case 2:
                        ivGame.setImageResource(R.drawable.uluru);
                        tvTitle.setText("Uluru");
                        didgeridooButton.setVisibility(View.GONE);
                        headbandButton.setVisibility(View.GONE);
                        ceremonyButton.setVisibility(View.GONE);
                        checkAnswersButton.setVisibility(View.GONE);

                        userInputOne.setVisibility(View.GONE);
                        userInputTwo.setVisibility(View.GONE);
                        userInputThree.setVisibility(View.GONE);
                        checkAnswersButton.setVisibility(View.GONE);

                        uluruButton.setVisibility(View.VISIBLE);
                        bodyButton.setVisibility(View.VISIBLE);
                        spearButton.setVisibility(View.VISIBLE);

                        uluruButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Log.d(TAG, "Uluru button works!");
                                makeAlert("Uluru", "One of the most well-known sacred sites in Australia. " +
                                                "Indigenous people believe that the spirits of the ancestral beings continue to reside in Uluru making the land a deeply important part of Aboriginal cultural identity.",
                                        "Done");
                                //showPopup(view);
                            }
                        });

                        bodyButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Log.d(TAG, "Body Art button works!");
                                makeAlert("Body Art", "A traditional custom that varies between nations with specific designs revealing relationships to family group, social position, tribe, precise ancestors, totemic fauna and tracts of land",
                                        "Done");
                                //showPopup(view);
                            }
                        });

                        spearButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Log.d(TAG, "Spear button works!");
                                makeAlert("Spear", "Made of wood and primarily used for hunting and combat purposes",
                                        "Done");
                                //showPopup(view);
                            }
                        });
                        break;

                    case 3:
                        ivGame.setImageResource(R.drawable.uluru);
                        tvTitle.setText("Name the objects:");
                        userInputOne.setVisibility(View.VISIBLE);
                        userInputTwo.setVisibility(View.VISIBLE);
                        userInputThree.setVisibility(View.VISIBLE);
                        checkAnswersButton.setVisibility(View.VISIBLE);
                        nextButton.setVisibility(View.GONE);

                        uluruButton.setVisibility(View.GONE);
                        bodyButton.setVisibility(View.GONE);
                        spearButton.setVisibility(View.GONE);

                        checkAnswersButton.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                String getUserInputOne = userInputOne.getText().toString();
                                String getUserInputTwo = userInputTwo.getText().toString();
                                String getUserInputThree = userInputThree.getText().toString();
                                if(getUserInputOne.equals("uluru") && getUserInputTwo.equals("spear") &&
                                        getUserInputThree.equals("body art")){
                                    nextButton.setVisibility(View.VISIBLE);
                                    checkAnswersButton.setVisibility(View.GONE);
                                    makeAlert("Correct!", "Please proceed to the next image.", "Ok");
                                }else{
                                    makeAlert("Sorry!", "Please try again.", "Ok");
                                }
                            }
                        });
                        break;

                    case 4:
                        ivGame.setImageResource(R.drawable.wallpaint);
                        tvTitle.setText("Wallpainting");
                        uluruButton.setVisibility(View.GONE);
                        bodyButton.setVisibility(View.GONE);
                        spearButton.setVisibility(View.GONE);

                        userInputOne.setVisibility(View.GONE);
                        userInputTwo.setVisibility(View.GONE);
                        userInputThree.setVisibility(View.GONE);
                        checkAnswersButton.setVisibility(View.GONE);

                        ochreButton.setVisibility(View.VISIBLE);
                        artButton.setVisibility(View.VISIBLE);
                        stencilButton.setVisibility(View.VISIBLE);

                        ochreButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d(TAG, "Ochre button works!");
                            makeAlert("Ochre", "Ochre was the main material used in creating hand stencils since it chemically reacted with and sunk into the surface of the rock",
                                    "Done");
                            //showPopup(view);
                        }
                    });

                    artButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d(TAG, "Rock Art button works!");
                            makeAlert("Rock Art", "A keystone of Aboriginal heritage and may act as gateways to communication between humans and the Spirit World, to mark territory, record historical events or stories or to help enact rituals",
                                    "Done");
                            //showPopup(view);
                        }
                    });

                    stencilButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d(TAG, "Hand Stencil button works!");
                            makeAlert("Hand Stencil", "Marks a territory and defines a rank so the higher the stencil in relationship to others, the higher the person’s rank",
                                    "Done");
                            //showPopup(view);
                        }
                    });
                    break;
                    case 5:
                        ivGame.setImageResource(R.drawable.wallpaint);
                        tvTitle.setText("Name the objects:");
                        userInputOne.setVisibility(View.VISIBLE);
                        userInputTwo.setVisibility(View.VISIBLE);
                        userInputThree.setVisibility(View.VISIBLE);
                        checkAnswersButton.setVisibility(View.VISIBLE);
                        nextButton.setVisibility(View.GONE);

                        ochreButton.setVisibility(View.GONE);
                        artButton.setVisibility(View.GONE);
                        stencilButton.setVisibility(View.GONE);

                        checkAnswersButton.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                String getUserInputOne = userInputOne.getText().toString();
                                String getUserInputTwo = userInputTwo.getText().toString();
                                String getUserInputThree = userInputThree.getText().toString();
                                if(getUserInputOne.equals("ochre") && getUserInputTwo.equals("hand stencil") &&
                                        getUserInputThree.equals("rock art")){
                                    nextButton.setVisibility(View.VISIBLE);
                                    checkAnswersButton.setVisibility(View.GONE);
                                    makeAlert("Correct!", "Please proceed to the next image.", "Ok");
                                }else{
                                    makeAlert("Sorry!", "Please try again.", "Ok");
                                }
                            }
                        });
                        break;
                    case 6:
                        ivGame.setImageResource(R.drawable.hut);
                        tvTitle.setText("Hut Depression");

                        userInputOne.setVisibility(View.GONE);
                        userInputTwo.setVisibility(View.GONE);
                        userInputThree.setVisibility(View.GONE);
                        checkAnswersButton.setVisibility(View.GONE);

                        break;
                    case 7:
                        ivGame.setImageResource(R.drawable.grindstone);
                        tvTitle.setText("Grindstone");
                        break;
                    case 8:
                        ivGame.setImageResource(R.drawable.serpent);
                        tvTitle.setText("Rainbow Serpent");
                        break;
                }
//                getContent(email); // go to the next page. Now page counter = 2
                Log.d(TAG, "new page counter is: " + pageCounter);
            }
        }); // end of btNext.setOnClickListener

//        btBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (pageCounter == 0){
//                    // go back to SearchFragment
//                    goToSearchFragment(email);
//                } else if (pageCounter >= 1 && pageCounter < maxPages){
//                    Log.d(TAG, "The initial pageCounter is: " + pageCounter);
//
//                    pageCounter-=1;
//
//                    Log.d(TAG, "The after pageCounter is: " + pageCounter);
//
//                    getContent(email); // go to the next page. Now page counter = 2
//                }
//            }
//        }); // end of btBack.setOnClickListener

        return view;
    } // end of onCreateView

    public void makeAlert(String title, String description, String done) {

        new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(description)
                .setPositiveButton(done, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("GameFragment", "Alert has appeared");
                    }
                })
                .show();

    }
    //Action bar back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();

                break;
            case R.id.fragment_frame:

                return true;
            default:
                return false;
        }
        return super.onOptionsItemSelected(item);
    }

//    // As the user goes through the content
//    private void getContent(String email){
//        Log.d(TAG, "The initial pageCounter is: " + pageCounter);
//
//        if (pageCounter < maxPages){ // if 0 < 5 (which is true) then
//            currentPage = contentList.get(pageCounter); // get the object at the first index = 0
//
//            // StorageReference storageRef = storage.getReference(currentPage.getPageNumId()).child(currentPage.getPageNumId() + ".png");
//            // No longer using StorageReference, and instead we are using drawable, as you cannot retrieve multiple images from Storage
//
//            int imageIdOne = getResources().getIdentifier(
//                    "@drawable/" + currentPage.getPageNumId().toLowerCase() + "1",
//                    null, getContext().getPackageName());
//            Log.d(TAG, "print " + currentPage.getPageNumId().toLowerCase() + "1");
//
//            int imageIdTwo = getResources().getIdentifier(
//                    "@drawable/" + currentPage.getPageNumId().toLowerCase() + "2",
//                    null, getContext().getPackageName());
//            Log.d(TAG, "print " + currentPage.getPageNumId().toLowerCase() + "2");
//
//        } else {
//            Log.d(TAG, "Finished Pictionary!! Congratulations :)");
//            nextButton.setText("Go To Menu.");
//            nextButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    finishReading(email);
//                    Log.i(TAG, "Home Navigation Successful");
//                    Intent intent = new Intent(v.getContext(), HomeFragment.class);
//                    startActivity(intent);
//                }
//            }); // end of onClickListener
//        }
//    } // end of getContent method

}