package com.example.fitnessappjava.CalorieCounter;

import android.content.Context;
import android.database.Cursor;
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

import com.example.fitnessappjava.R;

import java.util.ArrayList;

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

        //Breakfast listener
        ImageView imageViewAddBreakfast = (ImageView) getActivity().findViewById(R.id.imageViewBreakfast);
        imageViewAddBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFood(0); //0 == Breakfast
            }
        });

    }

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