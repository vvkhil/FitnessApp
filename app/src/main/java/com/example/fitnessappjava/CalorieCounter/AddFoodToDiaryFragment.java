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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitnessappjava.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddFoodToDiaryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFoodToDiaryFragment extends Fragment {

    private View mainView;
    private Cursor listCursorCategory;
    private Cursor listCursorFood;

    //Action buttons on toolbar
    private MenuItem menuItemEdit;
    private MenuItem menuItemDelete;

    //Holder for buttons on toolbar
    private String currentCategoryId;
    private String currentCategoryName;

    private String currentFoodId;
    private String currentFoodName;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddFoodToDiaryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddFoodToDiaryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddFoodToDiaryFragment newInstance(String param1, String param2) {
        AddFoodToDiaryFragment fragment = new AddFoodToDiaryFragment();
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
        ((DietActivity)getActivity()).getSupportActionBar().setTitle("Add food to diary");

        //Get data from fragment
        Bundle bundle = this.getArguments();
        String stringMealNumber = "";
        if (bundle != null) {
            stringMealNumber = bundle.getString("mealNumber");

        }

        //Populate the list of categories
        populateListWithCategories("0", "");

        //Create menu
        //setHasOptionsMenu(true);
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
        mainView = inflater.inflate(R.layout.fragment_add_food_to_diary, container, false);
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

    //Populate list with categories
    public void populateListWithCategories(String stringCategoryParentID, String stringCategoryName) {
        DBAdapter db = new DBAdapter(getActivity());
        db.open();

        //Get categories
        String fields[] = new String[] {
                "_id",
                "category_name",
                "category_parent_id"
        };
        listCursorCategory = db.select("categories", fields, "category_parent_id", stringCategoryParentID, "category_name", "ASC");

        //Create an array
        ArrayList<String> values = new ArrayList<String>();

        //Convert categories to string
        int categoriesCount = listCursorCategory.getCount();
        for (int x = 0; x < categoriesCount; x++) {
            values.add(listCursorCategory.getString(listCursorCategory.getColumnIndexOrThrow("category_name")));

            listCursorCategory.moveToNext();
        }

        //Close cursor
//        categoriesCursor.close();

        //Create adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, values);

        //Set Adapter
        ListView listView = (ListView)getActivity().findViewById(R.id.listViewAddFoodToDiary);
        listView.setAdapter(adapter);

        //OnClick
        if (stringCategoryParentID.equals("0")) {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    categoryListItemClicked(position);
                }
            });
        }

        db.close();

    }

    //Category list item clicked
    public void categoryListItemClicked(int listItemIndexClicked) {

        //Move cursor to ID clicked
        listCursorCategory.moveToPosition(listItemIndexClicked);

        currentCategoryId = listCursorCategory.getString(0);
        currentCategoryName = listCursorCategory.getString(1);
        String parentCategoryID = listCursorCategory.getString(2);

        //Change title
        ((DietActivity)getActivity()).getSupportActionBar().setTitle("Add food from " + currentCategoryName + " to diary");

        //Move to sub class
        populateListWithCategories(currentCategoryId, currentCategoryName);

        //Show food in category
        showFoodInCategory(currentCategoryId, currentCategoryName, parentCategoryID);

    }

    //Show food in category
    public void showFoodInCategory(String categoryId, String categoryName, String categoryParentID) {
        if (!(categoryParentID.equals("0"))) {
            //Change layout
            int id = R.layout.fragment_food_diet;
            setMainView(id);

            //Database
            DBAdapter db = new DBAdapter(getActivity());
            db.open();

            //Get categories
            String fields[] = new String[] {
                    "_id",
                    "food_name",
                    "food_manufactor_name",
                    "food_description",
                    "food_serving_size",
                    "food_serving_mesurment",
                    "food_serving_name_number",
                    "food_serving_name_word",
                    "food_energy_calculated"
            };

            listCursorFood = db.select("food", fields, "food_category_id", categoryId, "food_name", "ASC");

            //Find listView to populate
            ListView listView = (ListView)getActivity().findViewById(R.id.listViewFood);

            //Setup cursor adapter using cursor from last step
            FoodCursorAdapter cursorAdapter = new FoodCursorAdapter(getActivity(), listCursorFood);

            listView.setAdapter(cursorAdapter);

            //OnClick

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    foodInCategoryListItemClicked(position);
                }
            });


            db.close();
        }
    }

    private void foodInCategoryListItemClicked(int listItemFoodIndexClicked) {

        //Change layout
        int id = R.layout.fragment_food_view;
        setMainView(id);

        //Move cursor to ID clicked
        listCursorFood.moveToPosition(listItemFoodIndexClicked);

        currentFoodId = listCursorFood.getString(0);
        currentFoodName = listCursorFood.getString(1);

        //Change title
        ((DietActivity)getActivity()).getSupportActionBar().setTitle("Add " + currentFoodName);

        //Get data from database

        DBAdapter db = new DBAdapter(getActivity());
        db.open();

        String fields[] = new String[] {
                "_id",
                "food_name",
                "food_manufactor_name",
                " food_description",
                " food_serving_size",
                " food_serving_mesurment",
                " food_serving_name_number",
                " food_serving_name_word",
                " food_energy",
                " food_proteins",
                " food_carbohydrates",
                " food_fat",
                " food_energy_calculated",
                " food_proteins_calculated",
                " food_carbohydrates_calculated",
                " food_fat_calculated",
                " food_user_id",
                " food_barcode",
                " food_category_id",
                " food_image_a",
                " food_image_b",
                " food_image_c"
        };

        String currentIdSQL = db.quoteSmart(currentFoodId);

        Cursor foodCursor = db.select("food", fields, "_id", currentIdSQL);

        //Convert cursor to strings
        String stringId = foodCursor.getString(0);
        String stringName = foodCursor.getString(1);
        String stringManufactorName = foodCursor.getString(2);
        String stringDescription = foodCursor.getString(3);
        String stringServingSize = foodCursor.getString(4);
        String stringServingMesurment = foodCursor.getString(5);
        String stringServingNameNumber = foodCursor.getString(6);
        String stringServingNameWord = foodCursor.getString(7);
        String stringEnergy = foodCursor.getString(8);
        String stringProteins = foodCursor.getString(9);
        String stringCarbohydrates = foodCursor.getString(10);
        String stringFat = foodCursor.getString(11);
        String stringEnergyCalculated = foodCursor.getString(12);
        String stringProteinsCalculated = foodCursor.getString(13);
        String stringCarbohydratesCalculated = foodCursor.getString(14);
        String stringFatCalculated = foodCursor.getString(15);
        String stringUserId = foodCursor.getString(16);
        String stringBarcode = foodCursor.getString(17);
        String stringCategoryId = foodCursor.getString(18);
        String stringImageA = foodCursor.getString(19);
        String stringImageB = foodCursor.getString(20);
        String stringImageC = foodCursor.getString(21);


        //Headline
        TextView textViewFoodName = (TextView) getView().findViewById(R.id.textViewFoodName);
        textViewFoodName.setText(stringName);

        //Sub Headline
        TextView textViewFoodManufactorName = (TextView) getView().findViewById(R.id.textViewFoodManufactorName);
        textViewFoodManufactorName.setText(stringManufactorName);

        //Image

        //Calculation line
        TextView textViewFoodAbout = (TextView) getView().findViewById(R.id.textViewFoodAbout);
        String foodAbout = stringServingSize + " " + stringServingMesurment + " = "
                + stringServingNameNumber + " " + stringServingNameWord + ".";
        textViewFoodAbout.setText(foodAbout);

        //Description
        TextView textViewFoodDescription = (TextView) getView().findViewById(R.id.textViewFoodDescription);
        textViewFoodDescription.setText(stringDescription);

        //Calories table
        TextView textViewFoodEnergyPerHundred = (TextView) getView().findViewById(R.id.textViewFoodEnergyPerHundred);
        TextView textViewFoodProteinsPerHundred = (TextView) getView().findViewById(R.id.textViewFoodProteinsPerHundred);
        TextView textViewFoodCarbsPerHundred = (TextView) getView().findViewById(R.id.textViewFoodCarbsPerHundred);
        TextView textViewFoodFatPerHundred = (TextView) getView().findViewById(R.id.textViewFoodFatPerHundred);

        TextView textViewFoodEnergyPerN = (TextView) getView().findViewById(R.id.textViewFoodEnergyPerMeal);
        TextView textViewFoodProteinsPerN = (TextView) getView().findViewById(R.id.textViewFoodProteinsPerMeal);
        TextView textViewFoodCarbsPerN = (TextView) getView().findViewById(R.id.textViewFoodCarbsPerMeal);
        TextView textViewFoodFatPerN = (TextView) getView().findViewById(R.id.textViewFoodFatPerMeal);

        textViewFoodEnergyPerHundred.setText(stringEnergy);
        textViewFoodProteinsPerHundred.setText(stringProteins);
        textViewFoodCarbsPerHundred.setText(stringCarbohydrates);
        textViewFoodFatPerHundred.setText(stringFat);

        textViewFoodEnergyPerN.setText(stringEnergyCalculated);
        textViewFoodProteinsPerN.setText(stringProteinsCalculated);
        textViewFoodCarbsPerN.setText(stringCarbohydratesCalculated);
        textViewFoodFatPerN.setText(stringFatCalculated);


        db.close();

    }


}