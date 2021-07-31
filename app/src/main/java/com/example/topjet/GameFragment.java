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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.topjet.Entities.ContentEntity;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class GameFragment extends Fragment {

    private static final String TAG = "QuizFragment";

    private static final String KEY_USERNAME = "username";

    Button didgeridooButton, headbandButton, ceremonyButton;

    EditText userInputOne, userInputTwo, userInputThree;
    Button checkAnswersButton;

    Button uluruButton, bodyButton, spearButton;
    Button ochreButton, artButton, stencilButton;
    Button hutDepressionButton, basketButton, shellfishButton;
    Button tjiwaButton, seedsButton, tjungariButton;

    Button nextButton;
    Button finishButton;
    ImageView ivGame;
    TextView tvTitle;

    // Access FireStore
    private FirebaseFirestore database = FirebaseFirestore.getInstance();

    private List<ContentEntity> contentList; // this will be used to store all the retrieved data here...

    private ContentEntity currentPage; // currentPage = the page that the user will be on, and maxPages is the total number of pages
    private int pageCounter = 0; // will be used to count the current page
    private int maxPages = 20; // number of pages listed in the contentList

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

        hutDepressionButton = view.findViewById(R.id.hutDepressionButton);
        basketButton = view.findViewById(R.id.basketButton);
        shellfishButton = view.findViewById(R.id.shellfishButton);

        tjiwaButton = view.findViewById(R.id.tjiwaButton);
        seedsButton = view.findViewById(R.id.seedsButton);
        tjungariButton = view.findViewById(R.id.tjungariButton);

        nextButton = view.findViewById(R.id.nextButton);
        finishButton = view.findViewById(R.id.finishButton);
        ivGame = view.findViewById(R.id.ivGame);
        tvTitle = view.findViewById(R.id.tvTitle);


        didgeridooButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Didgeridoo button works!");
                makeAlert("Didgeridoo", "A wind instrument played to " +
                                "accompany ceremonies and evokes the magic of the Dreamtime.",
                        "Done");
                //showPopup(view);
            }
        });

        headbandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Headband button works!");
                makeAlert("Headband", "One type of body adornment and may " +
                                "be worn as everyday items or reserved for ceremonial occasions.",
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
                        "and the participants by burning native plants.", "Done");
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
                        ivGame.setImageResource(R.drawable.ceremony_numbers);
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
                                /*if(getUserInputOne.equals("didgeridoo") || getUserInputOne.equals("Didgeridoo") && getUserInputTwo.equals("headband")
                                        || getUserInputTwo.equals("Headband") && getUserInputThree.equals("Smoking Ceremony") ||
                                        getUserInputThree.equals("smoking ceremony") || getUserInputThree.equals("Smoking ceremony")){
                                    makeAlert("Correct!", "Please proceed to the next image.", "Ok");
                                    nextButton.setVisibility(View.VISIBLE);
                                    checkAnswersButton.setVisibility(View.GONE);
                                }else{
                                    makeAlert("Sorry!", "Please try again.", "Ok");
                                }*/

                                if(!getUserInputOne.equals("didgeridoo") && !getUserInputOne.equals("Didgeridoo")) {
                                    makeAlert("Sorry!", "Object 1 is incorrect. Please try again.", "Ok");
                                }else if(!getUserInputTwo.equals("headband") && !getUserInputTwo.equals("Headband")) {
                                    makeAlert("Sorry!", "Object 2 is incorrect. Please try again.", "Ok");
                                }else if(!getUserInputThree.equals("Smoking Ceremony") && !getUserInputThree.equals("smoking ceremony")
                                            && !getUserInputThree.equals("Smoking ceremony") && !getUserInputThree.equals("smoking Ceremony")) {
                                    makeAlert("Sorry!", "Object 3 is incorrect. Please try again.", "Ok");
                                }else{
                                    makeAlert("Correct!", "Please proceed to the next image.", "Ok");
                                    nextButton.setVisibility(View.VISIBLE);
                                    checkAnswersButton.setVisibility(View.GONE);
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
                                makeAlert("Body Art", "A traditional custom that varies between nations with specific designs revealing relationships to family group, social position, tribe, precise ancestors, totemic fauna and tracts of land.",
                                        "Done");
                                //showPopup(view);
                            }
                        });

                        spearButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Log.d(TAG, "Spear button works!");
                                makeAlert("Spear", "Made of wood and primarily used for hunting and combat purposes.",
                                        "Done");
                                //showPopup(view);
                            }
                        });
                        break;

                    case 3:
                        ivGame.setImageResource(R.drawable.uluru_numbers);
                        tvTitle.setText("Name the objects:");
                        userInputOne.setVisibility(View.VISIBLE);
                        userInputTwo.setVisibility(View.VISIBLE);
                        userInputThree.setVisibility(View.VISIBLE);
                        checkAnswersButton.setVisibility(View.VISIBLE);
                        nextButton.setVisibility(View.GONE);

                        uluruButton.setVisibility(View.GONE);
                        bodyButton.setVisibility(View.GONE);
                        spearButton.setVisibility(View.GONE);

                        userInputOne.getText().clear();
                        userInputTwo.getText().clear();
                        userInputThree.getText().clear();

                        checkAnswersButton.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                String getUserInputOne = userInputOne.getText().toString();
                                String getUserInputTwo = userInputTwo.getText().toString();
                                String getUserInputThree = userInputThree.getText().toString();

                                if(!getUserInputOne.equals("uluru") && !getUserInputOne.equals("Uluru")) {
                                    makeAlert("Sorry!", "Object 1 is incorrect. Please try again.", "Ok");
                                }else if(!getUserInputTwo.equals("spear") && !getUserInputTwo.equals("Spear")) {
                                    makeAlert("Sorry!", "Object 2 is incorrect. Please try again.", "Ok");
                                }else if(!getUserInputThree.equals("body art") && !getUserInputThree.equals("Body Art")
                                        && !getUserInputThree.equals("body Art") && !getUserInputThree.equals("Body art")) {
                                    makeAlert("Sorry!", "Object 3 is incorrect. Please try again.", "Ok");
                                }else{
                                    makeAlert("Correct!", "Please proceed to the next image.", "Ok");
                                    nextButton.setVisibility(View.VISIBLE);
                                    checkAnswersButton.setVisibility(View.GONE);
                                }
                            }
                        });
                        break;

                    case 4:
                        ivGame.setImageResource(R.drawable.rockart);
                        tvTitle.setText("Rock Art");
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
                            makeAlert("Ochre", "Ochre was the main material used in creating hand stencils since it chemically reacted with and sunk into the surface of the rock.",
                                    "Done");
                            //showPopup(view);
                        }
                    });

                    artButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d(TAG, "Rock Art button works!");
                            makeAlert("Rock Art", "A keystone of Aboriginal heritage and may act as gateways to communication between humans and the Spirit World, to mark territory, record historical events or stories or to help enact rituals.",
                                    "Done");
                            //showPopup(view);
                        }
                    });

                    stencilButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d(TAG, "Hand Stencil button works!");
                            makeAlert("Hand Stencil", "Marks a territory and defines a rank so the higher the stencil in relationship to others, the higher the person’s rank.",
                                    "Done");
                            //showPopup(view);
                        }
                    });
                    break;

                    case 5:
                        ivGame.setImageResource(R.drawable.rockart_numbers);
                        tvTitle.setText("Name the objects:");
                        userInputOne.setVisibility(View.VISIBLE);
                        userInputTwo.setVisibility(View.VISIBLE);
                        userInputThree.setVisibility(View.VISIBLE);
                        checkAnswersButton.setVisibility(View.VISIBLE);
                        nextButton.setVisibility(View.GONE);

                        ochreButton.setVisibility(View.GONE);
                        artButton.setVisibility(View.GONE);
                        stencilButton.setVisibility(View.GONE);

                        userInputOne.getText().clear();
                        userInputTwo.getText().clear();
                        userInputThree.getText().clear();

                        checkAnswersButton.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                String getUserInputOne = userInputOne.getText().toString();
                                String getUserInputTwo = userInputTwo.getText().toString();
                                String getUserInputThree = userInputThree.getText().toString();

                                if(!getUserInputOne.equals("ochre") && !getUserInputOne.equals("Ochre")) {
                                    makeAlert("Sorry!", "Object 1 is incorrect. Please try again.", "Ok");
                                }else if(!getUserInputTwo.equals("hand stencil") && !getUserInputTwo.equals("Hand stencil")
                                        && !getUserInputTwo.equals("Hand Stencil") && !getUserInputTwo.equals("hand Stencil")) {
                                    makeAlert("Sorry!", "Object 2 is incorrect. Please try again.", "Ok");
                                }else if(!getUserInputThree.equals("rock art") && !getUserInputThree.equals("Rock art")
                                        && !getUserInputThree.equals("Rock Art")) {
                                    makeAlert("Sorry!", "Object 3 is incorrect. Please try again.", "Ok");
                                }else{
                                    makeAlert("Correct!", "Please proceed to the next image.", "Ok");
                                    nextButton.setVisibility(View.VISIBLE);
                                    checkAnswersButton.setVisibility(View.GONE);
                                }
                            }
                        });
                        break;
                    case 6:
                        ivGame.setImageResource(R.drawable.hut_depression);
                        tvTitle.setText("Hut Depression");

                        userInputOne.setVisibility(View.GONE);
                        userInputTwo.setVisibility(View.GONE);
                        userInputThree.setVisibility(View.GONE);
                        checkAnswersButton.setVisibility(View.GONE);

                        hutDepressionButton.setVisibility(View.VISIBLE);
                        basketButton.setVisibility(View.VISIBLE);
                        shellfishButton.setVisibility(View.VISIBLE);
                        nextButton.setVisibility(View.VISIBLE);

                        hutDepressionButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                makeAlert("Hut Depression", "A form of shelter for the " +
                                                "Aboriginals which take the form of a circular hollow. " +
                                                "These huts are typically built using tea tree wood on top " +
                                                "of small hills and dunes.",
                                        "Done");
                            }
                        });

                        shellfishButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                makeAlert("Shellfish", "Forms an essential part of the Aboriginal diet. " +
                                                "Typically consists of a variety of shellfish such as abalone, mussels, oysters, limpets.",
                                        "Done");
                            }
                        });

                        basketButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                makeAlert("Basket", "a traditional tool used by the aboriginals to collect, store and hold " +
                                                "material. These are manually weaved using lomandra (also known as basket grass).",
                                        "Done");
                            }
                        });
                        break;

                    case 7:
                        ivGame.setImageResource(R.drawable.hut_numbers);
                        tvTitle.setText("Name the objects:");
                        hutDepressionButton.setVisibility(View.GONE);
                        basketButton.setVisibility(View.GONE);
                        shellfishButton.setVisibility(View.GONE);
                        checkAnswersButton.setVisibility(View.VISIBLE);
                        userInputOne.setVisibility(View.VISIBLE);
                        userInputTwo.setVisibility(View.VISIBLE);
                        userInputThree.setVisibility(View.VISIBLE);
                        nextButton.setVisibility(View.GONE);

                        userInputOne.getText().clear();
                        userInputTwo.getText().clear();
                        userInputThree.getText().clear();

                        checkAnswersButton.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                String getUserInputOne = userInputOne.getText().toString();
                                String getUserInputTwo = userInputTwo.getText().toString();
                                String getUserInputThree = userInputThree.getText().toString();

                                if(!getUserInputOne.equals("hut depression") && !getUserInputOne.equals("Hut depression")
                                        && !getUserInputTwo.equals("Hut Depression") && !getUserInputTwo.equals("hut Depression")) {
                                    makeAlert("Sorry!", "Object 1 is incorrect. Please try again.", "Ok");
                                }else if(!getUserInputTwo.equals("Shellfish") && !getUserInputTwo.equals("shellfish")) {
                                    makeAlert("Sorry!", "Object 2 is incorrect. Please try again.", "Ok");
                                }else if(!getUserInputThree.equals("basket") && !getUserInputThree.equals("Basket")) {
                                    makeAlert("Sorry!", "Object 3 is incorrect. Please try again.", "Ok");
                                }else{
                                    makeAlert("Correct!", "Please proceed to the next image.", "Ok");
                                    nextButton.setVisibility(View.VISIBLE);
                                    checkAnswersButton.setVisibility(View.GONE);
                                }
                            }
                        });
                        break;

                    case 8:
                        ivGame.setImageResource(R.drawable.rock);
                        tvTitle.setText("Grindstone");

                        userInputOne.setVisibility(View.GONE);
                        userInputTwo.setVisibility(View.GONE);
                        userInputThree.setVisibility(View.GONE);
                        checkAnswersButton.setVisibility(View.GONE);

                        tjiwaButton.setVisibility(View.VISIBLE);
                        seedsButton.setVisibility(View.VISIBLE);
                        tjungariButton.setVisibility(View.VISIBLE);
                        nextButton.setVisibility(View.VISIBLE);

                        tjiwaButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                makeAlert("Tjiwa", "The base rock that acts as a mortar " +
                                                "for the tjungari to grind material.",
                                        "Done");
                            }
                        });

                        seedsButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                makeAlert("Seeds", "Seeds such as native millet, spinifex and " +
                                                "wattleseed are ground into flour to create key aboriginal dishes such as damper.",
                                        "Done");
                            }
                        });

                        tjungariButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                makeAlert("Tjungari", "A grindstone that acts as a pestle " +
                                                "to process material such as seeds into flour and grindfruit.",
                                        "Done");
                            }
                        });
                        break;

                    case 9:
                        ivGame.setImageResource(R.drawable.rock_numbers);
                        tvTitle.setText("Name the objects:");

                        tjiwaButton.setVisibility(View.GONE);
                        seedsButton.setVisibility(View.GONE);
                        tjungariButton.setVisibility(View.GONE);
                        checkAnswersButton.setVisibility(View.VISIBLE);
                        userInputOne.setVisibility(View.VISIBLE);
                        userInputTwo.setVisibility(View.VISIBLE);
                        userInputThree.setVisibility(View.VISIBLE);
                        nextButton.setVisibility(View.GONE);

                        userInputOne.getText().clear();
                        userInputTwo.getText().clear();
                        userInputThree.getText().clear();

                        checkAnswersButton.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                String getUserInputOne = userInputOne.getText().toString();
                                String getUserInputTwo = userInputTwo.getText().toString();
                                String getUserInputThree = userInputThree.getText().toString();

                                if(!getUserInputOne.equals("Tjiwa") && !getUserInputOne.equals("tjiwa")) {
                                    makeAlert("Sorry!", "Object 1 is incorrect. Please try again.", "Ok");
                                }else if(!getUserInputTwo.equals("seeds") && !getUserInputTwo.equals("Seeds")) {
                                    makeAlert("Sorry!", "Object 2 is incorrect. Please try again.", "Ok");
                                }else if(!getUserInputThree.equals("tjungari") && !getUserInputThree.equals("Tjungari")) {
                                    makeAlert("Sorry!", "Object 3 is incorrect. Please try again.", "Ok");
                                }else{
                                    makeAlert("Correct!", "Please proceed to the next image.", "Ok");
                                    nextButton.setVisibility(View.VISIBLE);
                                    checkAnswersButton.setVisibility(View.GONE);
                                }
                            }
                        });
                            break;

                    case 10:
                        nextButton.setVisibility(View.GONE);
                        finishButton.setVisibility(View.VISIBLE);

                        finishButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                goToHomeFragment(email);
                            }
                        });
                }
//                getContent(email); // go to the next page. Now page counter = 2
                Log.d(TAG, "new page counter is: " + pageCounter);
            }
        }); // end of btNext.setOnClickListener

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

    private void goToHomeFragment(String email){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        HomeFragment homeFragment = new HomeFragment(); // generate a new homeFragment

        // send bundle for the Game Fragment
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        homeFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragment_frame, homeFragment); // replace the current frame with gameFragment
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        System.out.println("homeFragment works!");
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