package com.example.fitnessappjava.CalorieCounter;

import android.content.Context;
import android.database.Cursor;
import android.net.ipsec.ike.SaProposal;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitnessappjava.R;
import com.google.android.material.navigation.NavigationView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GoalDietFragment#newInstance} factory method to
 * {@link GoalDietFragment}
 * create an instance of this fragment.
 */
public class GoalDietFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {

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

    //set Main View
    private void setMainView(int id) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainView = inflater.inflate(id, null);
        ViewGroup rootView = (ViewGroup) getView();
        rootView.removeAllViews();
        rootView.addView(mainView);
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

    //on Create Options Menu
    //Creating action icon on toolbar
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        //Inflate menu
        ((DietActivity)getActivity()).getMenuInflater().inflate(R.menu.menu_goal, menu);

        // Assign menu items to variables
        menuItemEdit = menu.findItem(R.id.menu_action_goal_edit);
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
        TextView textViewActivityLevel = (TextView) getActivity().findViewById(R.id.textViewActivityLevel);
        if(goalActivityLevel.equals("0")) {
            textViewActivityLevel.setText("Little no exercise");
        } else if (goalActivityLevel.equals("1")) {
            textViewActivityLevel.setText("Light exercise (1-3 days per week)");
        } else if (goalActivityLevel.equals("2")) {
            textViewActivityLevel.setText("Moderate exercise (3-5 days per week)");
        } else if (goalActivityLevel.equals("3")) {
            textViewActivityLevel.setText("Heavy exercise (6-7 days per week)");
        } else if (goalActivityLevel.equals("4")) {
            textViewActivityLevel.setText("Very heavy exercise (twice per day, extra heavy workouts)");
        }

        //Numbers
        updateNumberTable();

        //Hide fields
        toggleNumbersViewGoal(false);

        //Checkbox toogle
        CheckBox checkBoxAdvanced = (CheckBox) getActivity().findViewById(R.id.checkBoxGoalToggle);

        checkBoxAdvanced.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                toggleNumbersViewGoal(isChecked);
            }
        });

        db.close();

    }

    public void toggleNumbersViewGoal (boolean isChecked) {

        //Remove table rows
//        TableRow textViewGoalMethodRowA = (TableRow) getActivity().findViewById(R.id.textViewGoalMethodRowA);
//
//        TableRow textViewGoalMethodRowB = (TableRow) getActivity().findViewById(R.id.textViewGoalMethodRowB);

        //Hide fields
        TextView textViewGoalHeadcellEnergy = (TextView) getActivity().findViewById(R.id.textViewGoalHeadcellEnergy);
        TextView textViewGoalHeadcellProteins = (TextView) getActivity().findViewById(R.id.textViewGoalHeadcellProteins);
        TextView textViewGoalHeadcellCarbs = (TextView) getActivity().findViewById(R.id.textViewGoalHeadcellCarbs);
        TextView textViewGoalHeadcellFat = (TextView) getActivity().findViewById(R.id.textViewGoalHeadcellFat);

        TextView textViewGoalHeadcellEnergyKeep = (TextView) getActivity().findViewById(R.id.textViewGoalHeadcellEnergyKeep);
        TextView textViewGoalHeadcellProteinsKeep = (TextView) getActivity().findViewById(R.id.textViewGoalHeadcellProteinsKeep);
        TextView textViewGoalHeadcellCarbsKeep = (TextView) getActivity().findViewById(R.id.textViewGoalHeadcellCarbsKeep);
        TextView textViewGoalHeadcellFatKeep = (TextView) getActivity().findViewById(R.id.textViewGoalHeadcellFatKeep);

        TextView textViewGoalProteinsBMR = (TextView) getActivity().findViewById(R.id.textViewGoalProteinsBMR);
        TextView textViewGoalCarbsBMR = (TextView) getActivity().findViewById(R.id.textViewGoalCarbsBMR);
        TextView textViewGoalFatBMR = (TextView) getActivity().findViewById(R.id.textViewGoalFatBMR);

        TextView textViewGoalProteinsDiet = (TextView) getActivity().findViewById(R.id.textViewGoalProteinsDiet);
        TextView textViewGoalCarbsDiet = (TextView) getActivity().findViewById(R.id.textViewGoalCarbsDiet);
        TextView textViewGoalFatDiet = (TextView) getActivity().findViewById(R.id.textViewGoalFatDiet);

        TextView textViewGoalProteinsWithActivity = (TextView) getActivity().findViewById(R.id.textViewGoalProteinsWithActivity);
        TextView textViewGoalCarbsWithActivity = (TextView) getActivity().findViewById(R.id.textViewGoalCarbsWithActivity);
        TextView textViewGoalFatWithActivity = (TextView) getActivity().findViewById(R.id.textViewGoalFatWithActivity);

        TextView textViewGoalProteinsWithActivityAndDiet = (TextView) getActivity().findViewById(R.id.textViewGoalProteinsWithActivityAndDiet);
        TextView textViewGoalCarbsWithActivityAndDiet = (TextView) getActivity().findViewById(R.id.textViewGoalCarbsWithActivityAndDiet);
        TextView textViewGoalFatWithActivityAndDiet = (TextView) getActivity().findViewById(R.id.textViewGoalFatWithActivityAndDiet);

        if (isChecked == false) {
//            textViewGoalMethodRowA.setVisibility(View.GONE);
//            textViewGoalMethodRowB.setVisibility(View.GONE);
            textViewGoalHeadcellEnergy.setVisibility(View.GONE);
            textViewGoalHeadcellProteins.setVisibility(View.GONE);
            textViewGoalHeadcellCarbs.setVisibility(View.GONE);
            textViewGoalHeadcellFat.setVisibility(View.GONE);
            textViewGoalHeadcellEnergyKeep.setVisibility(View.GONE);
            textViewGoalHeadcellProteinsKeep.setVisibility(View.GONE);
            textViewGoalHeadcellCarbsKeep.setVisibility(View.GONE);
            textViewGoalHeadcellFatKeep.setVisibility(View.GONE);
            textViewGoalProteinsBMR.setVisibility(View.GONE);
            textViewGoalCarbsBMR.setVisibility(View.GONE);
            textViewGoalFatBMR.setVisibility(View.GONE);
            textViewGoalProteinsDiet.setVisibility(View.GONE);
            textViewGoalCarbsDiet.setVisibility(View.GONE);
            textViewGoalFatDiet.setVisibility(View.GONE);
            textViewGoalProteinsWithActivity.setVisibility(View.GONE);
            textViewGoalCarbsWithActivity.setVisibility(View.GONE);
            textViewGoalFatWithActivity.setVisibility(View.GONE);
            textViewGoalProteinsWithActivityAndDiet.setVisibility(View.GONE);
            textViewGoalCarbsWithActivityAndDiet.setVisibility(View.GONE);
            textViewGoalFatWithActivityAndDiet.setVisibility(View.GONE);
        } else {
//            textViewGoalMethodRowA.setVisibility(View.VISIBLE);
//            textViewGoalMethodRowB.setVisibility(View.VISIBLE);
            textViewGoalHeadcellEnergy.setVisibility(View.VISIBLE);
            textViewGoalHeadcellProteins.setVisibility(View.VISIBLE);
            textViewGoalHeadcellCarbs.setVisibility(View.VISIBLE);
            textViewGoalHeadcellFat.setVisibility(View.VISIBLE);
            textViewGoalHeadcellEnergyKeep.setVisibility(View.VISIBLE);
            textViewGoalHeadcellProteinsKeep.setVisibility(View.VISIBLE);
            textViewGoalHeadcellCarbsKeep.setVisibility(View.VISIBLE);
            textViewGoalHeadcellFatKeep.setVisibility(View.VISIBLE);
            textViewGoalProteinsBMR.setVisibility(View.VISIBLE);
            textViewGoalCarbsBMR.setVisibility(View.VISIBLE);
            textViewGoalFatBMR.setVisibility(View.VISIBLE);
            textViewGoalProteinsDiet.setVisibility(View.VISIBLE);
            textViewGoalCarbsDiet.setVisibility(View.VISIBLE);
            textViewGoalFatDiet.setVisibility(View.VISIBLE);
            textViewGoalProteinsWithActivity.setVisibility(View.VISIBLE);
            textViewGoalCarbsWithActivity.setVisibility(View.VISIBLE);
            textViewGoalFatWithActivity.setVisibility(View.VISIBLE);
            textViewGoalProteinsWithActivityAndDiet.setVisibility(View.VISIBLE);
            textViewGoalCarbsWithActivityAndDiet.setVisibility(View.VISIBLE);
            textViewGoalFatWithActivityAndDiet.setVisibility(View.VISIBLE);
        }
    }

    //Goal edit
    public void goalEdit() {

        //Change layout
        int id = R.layout.fragment_goal_edit;
        setMainView(id);

        //Get data from database
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
                "goal_activity_level"
        };
        Cursor goalCursor = db.select("goal", fieldsGoal, "", "", "_id", "DESC");

        //Ready as variables
        String goalID = goalCursor.getString(0);
        String goalCurrentWeight = goalCursor.getString(1);
        String goalTargetWeight = goalCursor.getString(2);
        String goalIWantTo = goalCursor.getString(3);
        String goalWeeklyGoal = goalCursor.getString(4);
        String goalActivityLevel = goalCursor.getString(5);

        //Current weight
        EditText editTextGoalCurrentWeight = (EditText)getActivity().findViewById(R.id.editTextGoalCurrentWeight);
        if (mesurment.startsWith("m")) {
            //Metric
            editTextGoalCurrentWeight.setText(goalCurrentWeight);
        } else {
            //Imperial
            //Kg to pounds
            double currentWeightNumber = 0;

            try {
                currentWeightNumber = Double.parseDouble(goalCurrentWeight);
            } catch (NumberFormatException nfe) {
                System.out.println(nfe);
            }
            //kg to pounds
            double currentWeightNumberPounds = Math.round(currentWeightNumber / 0.45359237);

            editTextGoalCurrentWeight.setText(currentWeightNumberPounds + "");

            //Edit kg to pounds
            TextView textViewGoalCurrentWeightType = (TextView) getActivity().findViewById(R.id.textViewGoalCurrentWeightType);
            textViewGoalCurrentWeightType.setText("pounds");

        }

        //Target
        EditText editTextGoalTargetWeight = (EditText) getActivity().findViewById(R.id.editTextGoalTargetWeight);
        if (mesurment.startsWith("m")) {
            //Metric
            editTextGoalTargetWeight.setText(goalTargetWeight);
        } else {
            //Imperial
            //Kg to pounds
            double targetWeightNumber = 0;

            try {
                targetWeightNumber = Double.parseDouble(goalTargetWeight);
            } catch (NumberFormatException nfe) {
                System.out.println(nfe);
            }
            //kg to pounds
            double targetWeightNumberPounds = Math.round(targetWeightNumber / 0.45359237);

            editTextGoalTargetWeight.setText(targetWeightNumberPounds + "");

            //Edit kg to pounds
            TextView textViewGoalTargetWeightType = (TextView) getActivity().findViewById(R.id.textViewGoalTargetWeightType);
            textViewGoalTargetWeightType.setText("pounds/week");

        }

        //I want to
        Spinner spinnerIWantTo = (Spinner) getActivity().findViewById(R.id.spinnerIWantTo);
        if (goalIWantTo.equals("0")) {
            spinnerIWantTo.setSelection(0);
        } else {
            spinnerIWantTo.setSelection(1);
        }

        //Weekly goal
        Spinner spinnerWeeklyGoal = (Spinner) getActivity().findViewById(R.id.spinnerWeeklyGoal);
        if (goalWeeklyGoal.equals("0.5")) {
            spinnerWeeklyGoal.setSelection(0);
        } else if (goalWeeklyGoal.equals("1")) {
            spinnerWeeklyGoal.setSelection(1);
        } else if (goalWeeklyGoal.equals("1.5")) {
            spinnerWeeklyGoal.setSelection(2);
        }

        //Activity level
        Spinner spinnerActivityLevel = (Spinner) getActivity().findViewById(R.id.spinnerActivityLevel);
        int intActivityLevel = 0;
        try {
            intActivityLevel = Integer.parseInt(goalActivityLevel);
        } catch (NumberFormatException nfe) {
            System.out.println(nfe);
        }
        spinnerActivityLevel.setSelection(intActivityLevel);

//        //Update table
//        updateNumberTable();

        //SubmitButton listener
        Button buttonGoalSubmit = (Button) getActivity().findViewById(R.id.buttonGoalSubmit);
        buttonGoalSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editGoalSubmitOnClick();
            }
        });

        db.close();

    }

    //editGoalSubmitOnClick
    public void editGoalSubmitOnClick() {

        //Get data from database
        //Database
        DBAdapter db = new DBAdapter(getActivity());
        db.open();

        //Get row number one from users
        long rowID = 1;
        String fields[] = new String[] {
                "_id",
                "user_dob",
                "user_gender",
                "user_height",
                "user_mesurment"
        };
        Cursor c = db.select("users", fields, "_id", rowID);
        String stringUserDob = c.getString(1);
        String stringUserGender = c.getString(2);
        String stringUserHeight = c.getString(3);
        String mesurment = c.getString(4);

        //Get age
        String[] items1 = stringUserDob.split("/");
        String stringYear = items1[0];
        String stringMonth = items1[1];
        String stringDay = items1[2];

        int intYear = 0;
        try {
            intYear = Integer.parseInt(stringYear);
        } catch (NumberFormatException nfe) {
            System.out.println(nfe);
        }
        int intMonth = 0;
        try {
            intMonth = Integer.parseInt(stringMonth);
        } catch (NumberFormatException nfe) {
            System.out.println(nfe);
        }
        int intDay = 0;
        try {
            intDay = Integer.parseInt(stringDay);
        } catch (NumberFormatException nfe) {
            System.out.println(nfe);
        }

        String stringUserAge = getAge(intYear, intMonth, intDay);

        int intUserAge = 0;
        try {
            intUserAge = Integer.parseInt(stringUserAge);
        } catch (NumberFormatException nfe) {
            System.out.println(nfe);
        }

        //Get height
        double doubleUserHeight = 0;

        try {
            doubleUserHeight = Double.parseDouble(stringUserHeight);
        } catch (NumberFormatException nfe) {
            System.out.println(nfe);
        }

        //Current weight
        EditText editTextGoalCurrentWeight = (EditText) getActivity().findViewById(R.id.editTextGoalCurrentWeight);
        String stringCurrentWeight = editTextGoalCurrentWeight.getText().toString();
        double doubleCurrentWeight = 0;
        try {
            doubleCurrentWeight = Double.parseDouble(stringCurrentWeight);
        } catch (NumberFormatException e) {
            System.out.println(e);
        }

        //Target weight
        EditText editTextGoalTargetWeight = (EditText) getActivity().findViewById(R.id.editTextGoalTargetWeight);
        String stringTargetWeight = editTextGoalTargetWeight.getText().toString();
        double doubleTargetWeight = 0;
        try {
            doubleTargetWeight = Double.parseDouble(stringTargetWeight);
        } catch (NumberFormatException e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        //I want to
        // 0 - Loose weight
        // 1 - Gain Weight
        Spinner spinnerIWantTo = (Spinner)getActivity().findViewById(R.id.spinnerIWantTo);
        int intIWantTo = spinnerIWantTo.getSelectedItemPosition();
        String stringIWantTo = "" + intIWantTo;

        //Spinner weeklyGoal
        Spinner spinnerWeeklyGoal = (Spinner)getActivity().findViewById(R.id.spinnerWeeklyGoal);
        String stringWeeklyGoal = spinnerWeeklyGoal.getSelectedItem().toString();

        //Activity level
        Spinner spinnerActivityLevel = (Spinner)getActivity().findViewById(R.id.spinnerActivityLevel);
//        0: Little no exercise</item>
//        1: Light exercise (1-3 days per week)
//        2: Moderate exercise (3-5 days per week)
//        3: Heavy exercise (6-7 days per week)
//        4: Very heavy exercise (twice per day, extra heavy workouts)
        int intActivityLevel = spinnerActivityLevel.getSelectedItemPosition();
        String stringActivityLevel = "" + intActivityLevel;

        //TextView Calculation
//        TextView textViewCalculation = (TextView) getActivity().findViewById(R.id.textViewCalculation);

        if (TextUtils.isEmpty(stringCurrentWeight)) {
            Toast.makeText(getActivity(), "Current weight is needed", Toast.LENGTH_SHORT).show();
            editTextGoalCurrentWeight.setError("Please enter your current weight");
            editTextGoalCurrentWeight.requestFocus();
        }
        else if (TextUtils.isEmpty(stringTargetWeight)) {
            Toast.makeText(getActivity(), "Target weight is needed", Toast.LENGTH_SHORT).show();
            editTextGoalTargetWeight.setError("Please enter your target weight");
            editTextGoalTargetWeight.requestFocus();
        } else {

            String stringCurrentWeightSQL = db.quoteSmart(stringCurrentWeight);
            String stringTargetWeightSQL = db.quoteSmart(stringTargetWeight);
            String stringIWantToSQL = db.quoteSmart(stringIWantTo);
            String stringWeeklyGoalSQL = db.quoteSmart(stringWeeklyGoal);
            String stringActivityLevelSQL = db.quoteSmart(stringActivityLevel);

            //Date
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String goalDate = dateFormat.format(Calendar.getInstance().getTime());

//                        Calendar cc = Calendar.getInstance();
//                        int goalYear = cc.get(Calendar.YEAR);
//                        int goalMonth = cc.get(Calendar.MONTH);
//                        int goalDay = cc.get(Calendar.DAY_OF_MONTH);
//                        String goalDate = goalYear + "-" + goalMonth + "-" + goalDay;
            String goalDateSQL = db.quoteSmart(goalDate);

            //1. BMR:Energy
            double goalEnergyBMR = 0;

            if (stringUserGender.startsWith("m")) {
                // Male
                // BMR = 66.5 + (13.75 * kg body weight) + (5.003 * height in cm) - (6.775 * age)
                goalEnergyBMR = 66.5 + (13.75 * doubleCurrentWeight) + (5.003 * doubleUserHeight) - (6.775 * intUserAge);
                //goalEnergyBMR = Math.round(goalEnergyBMR);
            } else {
                // Female
                // BMR = 55.1 + (9.563 * kg body weight) + (1.850 * height in cm) - (4.676 * age)
                goalEnergyBMR = 55.1 + (9.563 * doubleCurrentWeight) + (1.850 * doubleUserHeight) - (4.676 * intUserAge);
                //goalEnergyBMR = Math.round(goalEnergyBMR);
            }
            goalEnergyBMR = Math.round(goalEnergyBMR);
            String goalEnergyBMRSQL = db.quoteSmart("" + goalEnergyBMR);

            //BMR: Proteins, carbs, fat
            // Calculate elements with BMR
            // 20-25 % protein
            // 40-50 % carbs
            // 25-35 % fat
            double proteinsBMR = Math.round(goalEnergyBMR * 0.25);
            double carbsBMR = Math.round(goalEnergyBMR * 0.5);
            double fatBMR = Math.round(goalEnergyBMR * 0.25);

            double proteinsBMRSQL = db.quoteSmart(proteinsBMR);
            double carbsBMRSQL = db.quoteSmart(carbsBMR);
            double fatBMRSQL = db.quoteSmart(fatBMR);

            //2: Diet

            double doubleWeeklyGoal = 0;
            try {
                doubleWeeklyGoal = Double.parseDouble(stringWeeklyGoal);
            } catch (NumberFormatException e) {
                System.out.println(e);
            }

            // 1 kg fat = 7700 kcal
            double kcal = 0;
            double energyDiet = 0;
            kcal = 7700 * doubleWeeklyGoal;
            if (intIWantTo == 0) {
                // Loose weight
                energyDiet = Math.round((goalEnergyBMR - (kcal / 7)) * 1.2);
            } else {
                // Gain weight
                energyDiet = Math.round((goalEnergyBMR + (kcal / 7)) * 1.2);
            }

            // Update database
            double energyDietSQL = db.quoteSmart(energyDiet);

            // Calculate elements with Diet
            // 20-25 % protein
            // 40-50 % carbs
            // 25-35 % fat
            double proteinsDiet = Math.round(energyDiet * 0.25);
            double carbsDiet = Math.round(energyDiet * 0.5);
            double fatDiet = Math.round(energyDiet * 0.25);

            double proteinsDietSQL = db.quoteSmart(proteinsDiet);
            double carbsDietSQL = db.quoteSmart(carbsDiet);
            double fatDietSQL = db.quoteSmart(fatDiet);

            //3: With activity

            // Taking in to account activity
            double energyWithActivity = 0;

            if (stringActivityLevel.equals("0")) {
                energyWithActivity = goalEnergyBMR * 1.2;
            } else if (stringActivityLevel.equals("1")) {
                energyWithActivity = goalEnergyBMR * 1.375;
            } else if (stringActivityLevel.equals("2")) {
                energyWithActivity = goalEnergyBMR * 1.55;
            } else if (stringActivityLevel.equals("3")) {
                energyWithActivity = goalEnergyBMR * 1.725;
            } else if (stringActivityLevel.equals("4")) {
                energyWithActivity = goalEnergyBMR * 1.9;
            }

            energyWithActivity = Math.round(energyWithActivity);
            double energyWithActivitySQL = db.quoteSmart(energyWithActivity);

            // Calculate elements with Activity
            // 20-25 % protein
            // 40-50 % carbs
            // 25-35 % fat
            double proteinsWithActivity = Math.round(energyWithActivity * 0.25);
            double carbsWithActivity = Math.round(energyWithActivity * 0.5);
            double fatWithActivity = Math.round(energyWithActivity * 0.25);

            double proteinsWithActivitySQL = db.quoteSmart(proteinsWithActivity);
            double carbsWithActivitySQL = db.quoteSmart(carbsWithActivity);
            double fatWithActivitySQL = db.quoteSmart(fatWithActivity);

            // 4: With activity and diet

            // Loose or gain weight?

            // 1 kg fat = 7700 kcal
            kcal = 0;
            double energyWithActivityAndDiet = 0;
            kcal = 7700 * doubleWeeklyGoal;
            if (intIWantTo == 0) {
                // Loose weight
                energyWithActivityAndDiet = goalEnergyBMR - (kcal / 7);
            } else {
                // Gain weight
                energyWithActivityAndDiet = goalEnergyBMR + (kcal / 7);
            }
            if (stringActivityLevel.equals("0")) {
                energyWithActivityAndDiet = energyWithActivityAndDiet * 1.2;
            } else if (stringActivityLevel.equals("1")) {
                energyWithActivityAndDiet = energyWithActivityAndDiet * 1.375;
            } else if (stringActivityLevel.equals("2")) {
                energyWithActivityAndDiet = energyWithActivityAndDiet * 1.55;
            } else if (stringActivityLevel.equals("3")) {
                energyWithActivityAndDiet = energyWithActivityAndDiet * 1.725;
            } else if (stringActivityLevel.equals("4")) {
                energyWithActivityAndDiet = energyWithActivityAndDiet * 1.9;
            }

            energyWithActivityAndDiet = Math.round(energyWithActivityAndDiet);

            // Update database
            double energyWithActivityAndDietSQL = db.quoteSmart(energyWithActivityAndDiet);

            // Calculate elements
            // 20-25 % protein
            // 40-50 % carbs
            // 25-35 % fat
            double proteinsWithActivityAndDiet = Math.round(energyWithActivityAndDiet * 0.25);
            double carbsWithActivityAndDiet = Math.round(energyWithActivityAndDiet * 0.5);
            double fatWithActivityAndDiet = Math.round(energyWithActivityAndDiet * 0.25);

            double proteinsWithActivityAndDietSQL = db.quoteSmart(proteinsWithActivityAndDiet);
            double carbsWithActivityAndDietSQL = db.quoteSmart(carbsWithActivityAndDiet);
            double fatWithActivityAndDietSQL = db.quoteSmart(fatWithActivityAndDiet);

            //Insert
            String inpFields = "'_id'," +
                    " 'goal_current_weight' ," +
                    " 'goal_target_weight' ," +
                    " 'goal_i_want_to' ," +
                    " 'goal_weekly_goal' ," +
                    " 'goal_date' ," +
                    " 'goal_activity_level' ," +
                    " 'goal_energy_bmr' ," +
                    " 'goal_proteins_bmr' ," +
                    " 'goal_carbs_bmr' ," +
                    " 'goal_fat_bmr' ," +
                    " 'goal_energy_diet' ," +
                    " 'goal_proteins_diet' ," +
                    " 'goal_carbs_diet' ," +
                    " 'goal_fat_diet' ," +
                    " 'goal_energy_with_activity' ," +
                    " 'goal_proteins_with_activity' ," +
                    " 'goal_carbs_with_activity' ," +
                    " 'goal_fat_with_activity' ," +
                    " 'goal_energy_with_activity_and_diet' ," +
                    " 'goal_proteins_with_activity_and_diet' ," +
                    " 'goal_carbs_with_activity_and_diet' ," +
                    " 'goal_fat_with_activity_and_diet' ,";

            String intValues = "NULL, " +
                    stringCurrentWeightSQL + ", " +
                    stringTargetWeightSQL + ", " +
                    stringIWantToSQL + ", " +
                    stringWeeklyGoalSQL + ", " +
                    goalDateSQL + ", " +
                    stringActivityLevelSQL + ", " +
                    goalEnergyBMRSQL + ", " +
                    proteinsBMRSQL + ", " +
                    carbsBMRSQL + ", " +
                    fatBMRSQL + ", " +

                    energyDietSQL + ", " +
                    proteinsDietSQL + ", " +
                    carbsDietSQL + ", " +
                    fatDietSQL + ", " +

                    energyWithActivitySQL + ", " +
                    proteinsWithActivitySQL + ", " +
                    carbsWithActivitySQL + ", " +
                    fatWithActivitySQL + ", " +

                    energyWithActivityAndDietSQL + ", " +
                    proteinsWithActivityAndDietSQL + ", " +
                    carbsWithActivityAndDietSQL + ", " +
                    fatWithActivityAndDietSQL;

            db.insert("goal", inpFields, intValues);

            updateNumberTable();

            Toast.makeText(getActivity(), "Changes saved", Toast.LENGTH_SHORT).show();

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        }

    }

    //getAge
    private String getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }

    //Update table
    private void updateNumberTable() {
        DBAdapter db = new DBAdapter(getActivity());
        db.open();

        //Get Goal data
        String fieldsGoal[] = new String[] {
                " goal_energy_bmr",
                " goal_proteins_bmr",
                " goal_carbs_bmr",
                " goal_fat_bmr",
                " goal_energy_diet",
                " goal_proteins_diet",
                " goal_carbs_diet",
                " goal_fat_diet",
                " goal_energy_with_activity",
                " goal_proteins_with_activity",
                " goal_carbs_with_activity",
                " goal_fat_with_activity",
                " goal_energy_with_activity_and_diet",
                " goal_proteins_with_activity_and_diet",
                " goal_carbs_with_activity_and_diet",
                " goal_fat_with_activity_and_diet"
        };

        Cursor goalCursor = db.select("goal", fieldsGoal, "", "", "_id", "DESC");

        //Ready as variables
        String goalEnergyBmr = goalCursor.getString(0);
        String goalProteinsBmr = goalCursor.getString(1);
        String goalCarbsBmr = goalCursor.getString(2);
        String goalFatBmr = goalCursor.getString(3);
        String goalEnergyDiet = goalCursor.getString(4);
        String goalProteinsDiet = goalCursor.getString(5);
        String goalCarbsDiet = goalCursor.getString(6);
        String goalFatDiet = goalCursor.getString(7);
        String goalEnergyWithActivity = goalCursor.getString(8);
        String goalProteinsWithActivity = goalCursor.getString(9);
        String goalCarbsWithActivity = goalCursor.getString(10);
        String goalFatWithActivity = goalCursor.getString(11);
        String goalEnergyWithActivityAndDiet = goalCursor.getString(12);
        String goalProteinsWithActivityAndDiet = goalCursor.getString(13);
        String goalCarbsWithActivityAndDiet = goalCursor.getString(14);
        String goalFatWithActivityAndDiet = goalCursor.getString(15);

        //Numbers

        //1 Diet
        TextView textViewGoalEnergyDiet = (TextView) getActivity().findViewById(R.id.textViewGoalEnergyDiet);
        textViewGoalEnergyDiet.setText(goalEnergyDiet);
        TextView textViewGoalProteinsDiet = (TextView) getActivity().findViewById(R.id.textViewGoalProteinsDiet);
        textViewGoalProteinsDiet.setText(goalProteinsDiet);
        TextView textViewGoalCarbsDiet = (TextView) getActivity().findViewById(R.id.textViewGoalCarbsDiet);
        textViewGoalCarbsDiet.setText(goalCarbsDiet);
        TextView textViewGoalFatDiet = (TextView) getActivity().findViewById(R.id.textViewGoalFatDiet);
        textViewGoalFatDiet.setText(goalFatDiet);

        //2 WithActivityAndDiet
        TextView textViewGoalEnergyWithActivityAndDiet = (TextView) getActivity().findViewById(R.id.textViewGoalEnergyWithActivityAndDiet);
        textViewGoalEnergyWithActivityAndDiet.setText(goalEnergyWithActivityAndDiet);
        TextView textViewGoalProteinsWithActivityAndDiet = (TextView) getActivity().findViewById(R.id.textViewGoalProteinsWithActivityAndDiet);
        textViewGoalProteinsWithActivityAndDiet.setText(goalProteinsWithActivityAndDiet);
        TextView textViewGoalCarbsWithActivityAndDiet = (TextView) getActivity().findViewById(R.id.textViewGoalCarbsWithActivityAndDiet);
        textViewGoalCarbsWithActivityAndDiet.setText(goalCarbsWithActivityAndDiet);
        TextView textViewGoalFatWithActivityAndDiet = (TextView) getActivity().findViewById(R.id.textViewGoalFatWithActivityAndDiet);
        textViewGoalFatWithActivityAndDiet.setText(goalFatWithActivityAndDiet);

        //3 BMR
        TextView textViewGoalEnergyBMR = (TextView) getActivity().findViewById(R.id.textViewGoalEnergyBMR);
        textViewGoalEnergyBMR.setText(goalEnergyBmr);
        TextView textViewGoalProteinsBMR = (TextView) getActivity().findViewById(R.id.textViewGoalProteinsBMR);
        textViewGoalProteinsBMR.setText(goalProteinsBmr);
        TextView textViewGoalCarbsBMR = (TextView) getActivity().findViewById(R.id.textViewGoalCarbsBMR);
        textViewGoalCarbsBMR.setText(goalCarbsBmr);
        TextView textViewGoalFatBMR = (TextView) getActivity().findViewById(R.id.textViewGoalFatBMR);
        textViewGoalFatBMR.setText(goalFatBmr);

        //4 WithActivity
        TextView textViewGoalEnergyWithActivity = (TextView) getActivity().findViewById(R.id.textViewGoalEnergyWithActivity);
        textViewGoalEnergyWithActivity.setText(goalEnergyWithActivity);
        TextView textViewGoalProteinsWithActivity = (TextView) getActivity().findViewById(R.id.textViewGoalProteinsWithActivity);
        textViewGoalProteinsWithActivity.setText(goalProteinsWithActivity);
        TextView textViewGoalCarbsWithActivity = (TextView) getActivity().findViewById(R.id.textViewGoalCarbsWithActivity);
        textViewGoalCarbsWithActivity.setText(goalCarbsWithActivity);
        TextView textViewGoalFatWithActivity = (TextView) getActivity().findViewById(R.id.textViewGoalFatWithActivity);
        textViewGoalFatWithActivity.setText(goalFatWithActivity);

        db.close();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}