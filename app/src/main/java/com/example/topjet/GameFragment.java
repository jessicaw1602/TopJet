package com.example.topjet;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.app.AlertDialog;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;

public class GameFragment extends Fragment {

    private static final String TAG = "QuizFragment";

    private static final String KEY_USERNAME = "username";

    Button didgeridooButton;
    Button headbandButton;
    Button ceremonyButton;

    // Access FireStore
    private FirebaseFirestore database = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // inflate Fragment's view
        View view = inflater.inflate(R.layout.activity_game, container, false);

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
}