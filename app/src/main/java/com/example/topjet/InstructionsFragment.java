package com.example.topjet;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InstructionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InstructionsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Button instructionsButton;
    TextView tvText;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public InstructionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InstructionsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InstructionsFragment newInstance(String param1, String param2) {
        InstructionsFragment fragment = new InstructionsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_instructions, container, false);
        //return inflater.inflate(R.layout.fragment_instructions, container, false);

        String email = getArguments().getString("email");

        instructionsButton = view.findViewById(R.id.instructionsButton);
        tvText = view.findViewById(R.id.tvText);
        setHasOptionsMenu(true);
        tvText.setText("The aim of this game is to correctly identify and label the three objects in each of the following pictures. \n" +
                "\nYou will firstly be prompted with a picture with three objects highlighted as red dots. Take note of what these objects are by clicking on the red dots that identify each object. \n" +
                "\nUpon navigating to the next page, you will be tested on what the three objects were on the previous page.\n" +
                "\nTry to correctly identify as many objects as you can! \n \n Good luck :) ");

        instructionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToEventsFragment(email);
            }
        });
        return view;
    }

    private void goToEventsFragment(String email){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        GameFragment gameFragment = new GameFragment(); // generate a new searchFragment

        // send bundle for the Game Fragment
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        gameFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragment_frame, gameFragment); // replace the current frame with gameFragment
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        System.out.println("gameFragment works!");
    }

    //Action bar back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();

                break;
//            case R.id.fragment_frame:
//                return true;
            default:
                return false;
        }
        return super.onOptionsItemSelected(item);
    }

}