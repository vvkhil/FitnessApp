package com.example.fitnessappjava.CalorieCounter;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.fitnessappjava.R;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeDietFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeDietFragment extends Fragment {

    private View mainView;
    private Cursor listCursor;

    //Action buttons on toolbar
    private MenuItem menuItemEdit;
    private MenuItem menuItemDelete;

    //Holder for buttons on toolbar
    private String currentId;
    private String currentName;

    //Holding variables
    private String currentDateYear = "";
    private String currentDateMonth = "";
    private String currentDateDay = "";


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeDietFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeDietFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeDietFragment newInstance(String param1, String param2) {
        HomeDietFragment fragment = new HomeDietFragment();
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
        ((DietActivity)getActivity()).getSupportActionBar().setTitle("Home");

        //getDataFromDbAndDisplay
        initializeHome();

        //Create menu
        //setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_home_diet, container, false);
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
//        if (id == R.id.menu_action_goal_edit) {
//
//        }
        return super.onOptionsItemSelected(item);
    }

    //Our own methods

    //Initialize home
    private void initializeHome() {

        //Database
        DBAdapter db = new DBAdapter(getActivity());
        db.open();

        //Find date
        if (currentDateYear.equals("") || currentDateMonth.equals("") || currentDateDay.equals("")) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_YEAR);

            currentDateYear = "" + year;

            //Month
            month = month + 1; //Month starts with 0
            if (month < 10) {
                currentDateMonth = "0" + month;
            } else {
                currentDateMonth = "" + month;
            }

            //Day
            if (day < 10) {
                currentDateDay = "0" + day;
            } else {
                currentDateDay = "" + day;
            }

        }
        String stringFdDate = currentDateYear + "-" + currentDateMonth + "-" + currentDateDay;


        //Fill table
        updateTable(stringFdDate, "0");

        //Breakfast listener
        ImageView imageViewAddBreakfast = (ImageView) getActivity().findViewById(R.id.imageViewBreakfast);
        imageViewAddBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFood(0); //0 == Breakfast
            }
        });

        db.close();

    }

    //Update table
    public void updateTable(String stringDate, String stringMealNumber) {

        //Database
        DBAdapter db = new DBAdapter(getActivity());
        db.open();

        //Select
        String fields[] = new String[] {
                " _id",
                " fd_food_id",
                " fd_serving_size_gram",
                " fd_serving_size_gram_mesurment",
                " fd_serving_size_pcs",
                " fd_serving_size_pcs_mesurment",
                " fd_energy_calculated",
                " fd_protein_calculated",
                " fd_carbohydrates_calculated",
                " fd_fat_calculated"
        };
        Cursor cursorFd = db.select("food_diary", fields, "fd_date", stringDate);

        //Loop through cursor
        int intCursorFdCount = cursorFd.getCount();
        for (int x = 0; x < intCursorFdCount; x++) {

            cursorFd.moveToNext();

        }

        //Update table
        TextView textViewBreakfastItemsName = (TextView) getActivity().findViewById(R.id.textViewBreakfastItemsName);
        //textViewBreakfastItemsName.setText(cursorFd.getString(2));

        db.close();

    }

    //Add food
    private void addFood(int mealNumber) {

        //Initialize fragment
        Fragment fragment = null;
        Class fragmentClass = null;
        fragmentClass = AddFoodToDiaryFragment.class;

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Send variable
        Bundle bundle = new Bundle();
        bundle.putString("mealNumber", "" + mealNumber);
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

    }

}