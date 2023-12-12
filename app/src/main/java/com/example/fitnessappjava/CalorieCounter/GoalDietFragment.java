package com.example.fitnessappjava.CalorieCounter;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fitnessappjava.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GoalDietFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GoalDietFragment extends Fragment {

    private View mainView;

    //Action buttons on toolbar
    private MenuItem menuItemEdit;
    private MenuItem menuItemDelete;

    //Holder for buttons on toolbar
    private String currentId;
    private String currentName;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

//    private OnFragmentInteractionListener mListener;

    public GoalDietFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static GoalDietFragment newInstance(String param1, String param2) {
        GoalDietFragment fragment = new GoalDietFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Set title
        ((DietActivity)getActivity()).getSupportActionBar().setTitle("Goal");

        //getDataFromDbAndDisplay
        initializeGetDataFromDbAndDisplay();

        //Create menu
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_goal_diet, container, false);
        return mainView;
    }

    //set Main View
    private void setMainView(int id) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainView = inflater.inflate(id, null);
        ViewGroup rootView = (ViewGroup) getView();
        rootView.removeAllViews();
        rootView.addView(mainView);
    }

    //on Create Options Menu
    //Creating action icon on toolbar
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        //Inflate menu
        MenuInflater menuInflater = ((DietActivity)getActivity()).getMenuInflater();
        inflater.inflate(R.menu.menu_goal, menu);

        // Assign menu items to variables
        menuItemEdit = menu.findItem(R.id.menu_action_food_edit);
//        menuItemDelete = menu.findItem(R.id.menu_action_food_delete);
//
//        //Hide as default
//        menuItemEdit.setVisible(false);
//        menuItemDelete.setVisible(false);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.menu_action_goal_edit) {
            goalEdit();
        }
        return super.onOptionsItemSelected(item);
    }

    //Our own methods

    //Get data from db and display
    public void initializeGetDataFromDbAndDisplay() {

        //Get data from database

        //Database
        DBAdapter db = new DBAdapter(getActivity());
        db.open();

        //Get row number one from users
        long rowID = 1;
        String fields[] = new String[] {
                "_id",
                "user_mesurment"
        };
        Cursor c = db.select("users", fields, "_id", rowID);
        String mesurment = c.getString(1);

        //Get Goal data
        String fieldsGoal[] = new String[] {
                "_id",
                "goal_current_weight",
                "goal_target_weight",
                "goal_i_want_to",
                "goal_weekly_goal",
                "goal_activity_level",
                "goal_date",
        };
        Cursor goalCursor = db.select("goal", fieldsGoal, "", "", "_id", "DESC");

        //Ready as variables
        String goalID = goalCursor.getString(0);
        String goalCurrentWeight = goalCursor.getString(1);
        String goalTargetWeight = goalCursor.getString(2);
        String goalIWantTo = goalCursor.getString(3);
        String goalWeeklyGoal = goalCursor.getString(4);
        String goalActivityLevel = goalCursor.getString(5);
        String goalDate = goalCursor.getString(6);

        //Status

        //Current weight
        TextView textViewGoalCurrentWeightNumber = (TextView)getActivity().findViewById(R.id.textViewGoalCurrentWeightNumber);
        if (mesurment.startsWith("m")) {
            //Metric
            textViewGoalCurrentWeightNumber.setText(goalCurrentWeight + " kg (" + goalDate + ")");
        } else {
            //Imperial
            //Kg to pounds
            double currentWeightNumber = 0;

            try {
                currentWeightNumber = Double.parseDouble(goalCurrentWeight);
            } catch (NumberFormatException nfe) {
                System.out.println("Could not parse " + nfe);
            }

            double currentWeightNumberPounds = Math.round(currentWeightNumber / 0.45359237);

            textViewGoalCurrentWeightNumber.setText(currentWeightNumberPounds + " pounds (" + goalDate + ")");
        }

        //Target
        TextView textViewGoalCurrentTargetNumber = (TextView)getActivity().findViewById(R.id.textViewGoalCurrentTargetNumber);
        if (mesurment.startsWith("m")) {
            //Metric
            textViewGoalCurrentTargetNumber.setText(goalTargetWeight + " kg");
        } else {
            //Imperial
            //Kg to pounds
            double targetWeightNumber = 0;

            try {
                targetWeightNumber = Double.parseDouble(goalTargetWeight);
            } catch (NumberFormatException nfe) {
                System.out.println("Could not parse " + nfe);
            }

            double targetWeightNumberPounds = Math.round(targetWeightNumber / 0.45359237);

            textViewGoalCurrentTargetNumber.setText(targetWeightNumberPounds + " pounds");
        }

        //Method
        TextView textViewGoalMethodText = (TextView)getActivity().findViewById(R.id.textViewGoalMethodText);

        String method = "";
        if (goalIWantTo.equals("0")) {
            method = "Loose " + goalWeeklyGoal;
        } else {
            method = "Gain " + goalWeeklyGoal;
        }
        if (mesurment.startsWith("m")) {
            method = method + " kg/week";
        } else {
            method = method + " pounds/week";
        }
        textViewGoalMethodText.setText(method);

        //Activity level

    }

}