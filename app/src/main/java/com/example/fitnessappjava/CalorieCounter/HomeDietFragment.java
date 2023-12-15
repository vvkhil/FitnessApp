package com.example.fitnessappjava.CalorieCounter;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

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
    private Cursor listCursorCategory;
    private Cursor listCursorFood;

    //Action buttons on toolbar
    private MenuItem menuItemAddFood;
    private MenuItem menuItemDelete;

    //Holder
    private String currentMealNumber;
    private String currentCategoryId;
    private String currentCategoryName;

    private String currentFoodId;
    private String currentFoodName;

    private String currentFdId;

    private String currentPortionSizePcs;
    private String currentPortionSizeGram;
    private boolean lockPortionSizeByPcs;
    private boolean lockPortionSizeByGram;

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
        setHasOptionsMenu(true);
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
        ((DietActivity)getActivity()).getMenuInflater().inflate(R.menu.menu_home, menu);

        // Assign menu items to variables
        menuItemAddFood = menu.findItem(R.id.menu_action_add_food);
//        menuItemDelete = menu.findItem(R.id.menu_action_food_delete);
//
//        //Hide as default
//        menuItemEdit.setVisible(false);
//        menuItemDelete.setVisible(false);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.menu_action_add_food) {
            addFoodToDiarySelectMealNumber();
        }
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
            int day = calendar.get(Calendar.DAY_OF_MONTH);

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
        String stringFdDate = currentDateYear + "/" + currentDateMonth + "/" + currentDateDay;


        //Fill table
        updateTableItems(stringFdDate, "0");
        updateTableItems(stringFdDate, "1");
        updateTableItems(stringFdDate, "2");
        updateTableItems(stringFdDate, "3");
        updateTableItems(stringFdDate, "4");
        updateTableItems(stringFdDate, "5");
        updateTableItems(stringFdDate, "6");

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
    public void updateTableItems(String stringDate, String stringMealNumber) {

        //Database
        DBAdapter db = new DBAdapter(getActivity());
        db.open();

        String stringDateSQL = db.quoteSmart(stringDate);

        String stringMealNumberSQL = db.quoteSmart(stringMealNumber);

        //Select
        String fdFields[] = new String[] {
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

        String fdWhereClause[] = new String[] {
                "fd_date",
                "fd_meal_number"
        };

        String fdWhereCondition[] = new String[] {
                stringDateSQL,
                stringMealNumberSQL
        };

        String fdWhereAndOr[] = new String[] {
                "AND"
        };

        Cursor cursorFd = db.select("food_diary", fdFields, fdWhereClause, fdWhereCondition, fdWhereAndOr);

        //Select for food name
        String fieldsFood[] = new String[] {
                " _id",
                " food_name",
                " food_manufactor_name"
        };
        Cursor cursorFood;

        //Select for food_diary_cal_eaten
        Cursor cursorFdce;
        String fieldsFdce[] = new String[] {
                "_id",
                "fdce_id",
                "fdce_meal_no",
                "fdce_energy",
                "fdce_proteins",
                "fdce_carbs",
                "fdce_fat"
        };

        String whereClause[] = new String[] {
                "fdce_date",
                "fdce_meal_no"
        };

        String whereCondition[] = new String[] {
                stringDateSQL,
                stringMealNumberSQL
        };

        String whereAndOr[] = new String[] {
                "AND"
        };

        //cursorFdce = db.select("food_diary_cal_eaten", fieldsFdce, "fdce_date", stringDateSQL);
        cursorFdce = db.select("food_diary_cal_eaten", fieldsFdce, whereClause, whereCondition, whereAndOr);
        int cursorFdceCount = cursorFdce.getCount();

        if (cursorFdceCount == 0) {
            String insFields = "_id, fdce_date, fdce_meal_no, fdce_energy, fdce_proteins, fdce_carbs, fdce_fat";
            String insValues = "NULL, " + stringDateSQL + ", " + stringMealNumberSQL + ", '0', '0', '0', '0'";
            db.insert("food_diary_cal_eaten", insFields, insValues);

            cursorFdce = db.select("food_diary_cal_eaten", fieldsFdce, whereClause, whereCondition, whereAndOr);
        }
        String stringFdceId = cursorFdce.getString(0);
        long longFdceId = Long.parseLong(stringFdceId);

        //Ready variables for sum
        int intFdceEnergy = 0;
        int intFdceProteins = 0;
        int intFdceCarbs = 0;
        int intFdceFat = 0;

        //Loop through cursor
        int intCursorFdCount = cursorFd.getCount();
        for (int x = 0; x < intCursorFdCount; x++) {

            //Variables from food diary
            String fdFoodId = cursorFd.getString(1);
            String fdFoodIdSQL = db.quoteSmart(fdFoodId);

            String fdServingSizeGram = cursorFd.getString(2);
            String fdServingSizeGramMesurment = cursorFd.getString(3);
            String fdServingSizePcs = cursorFd.getString(4);
            String fdServingSizePcsMesurment = cursorFd.getString(5);
            String fdEnergyCalculated = cursorFd.getString(6);
            String fdProteinsCalculated = cursorFd.getString(7);
            String fdCarbsCalculated = cursorFd.getString(8);
            String fdFatCalculated = cursorFd.getString(9);

            int intFdEnergyCalculated = Integer.parseInt(fdEnergyCalculated);
            int intFdProteinsCalculated = Integer.parseInt(fdProteinsCalculated);
            int intFdCarbsCalculated = Integer.parseInt(fdCarbsCalculated);
            int intFdFatCalculated = Integer.parseInt(fdFatCalculated);


            //Get food name
            cursorFood = db.select("food", fieldsFood, "_id", fdFoodIdSQL);

            //Variables from food
            String foodID = cursorFood.getString(0);
            String foodName = cursorFood.getString(1);
            String foodManufactorName = cursorFood.getString(2);

            String subLine = foodManufactorName + ", " +
                    fdServingSizeGram  + ", " +
                    fdServingSizeGramMesurment  + ", " +
                    fdServingSizePcs  + ", " +
                    fdServingSizePcsMesurment;

            //Add table rows
            TableLayout t1 = null;
            if (stringMealNumber.equals("0")) {
                t1 = (TableLayout) getActivity().findViewById(R.id.tableLayoutBreakfastItems);
            }
            else if (stringMealNumber.equals("1")) {
                t1 = (TableLayout) getActivity().findViewById(R.id.tableLayoutLunchItems);
            }
            else if (stringMealNumber.equals("2")) {
                t1 = (TableLayout) getActivity().findViewById(R.id.tableLayoutBeforeTrainingItems);
            }
            else if (stringMealNumber.equals("3")) {
                t1 = (TableLayout) getActivity().findViewById(R.id.tableLayoutAfterTrainingItems);
            }
            else if (stringMealNumber.equals("4")) {
                t1 = (TableLayout) getActivity().findViewById(R.id.tableLayoutDinnerItems);
            }
            else if (stringMealNumber.equals("5")) {
                t1 = (TableLayout) getActivity().findViewById(R.id.tableLayoutSnacksItems);
            }
            else if (stringMealNumber.equals("6")) {
                t1 = (TableLayout) getActivity().findViewById(R.id.tableLayoutSupperItems);
            }

            TableRow tr1 = new TableRow(getActivity()); //Crete a new row to be added
            tr1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            TableRow tr2 = new TableRow(getActivity()); //Crete a new row to be added
            tr2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            //Table row: TextView Name
            TextView textViewName = new TextView(getActivity());
            textViewName.setText(foodName);
            tr1.addView(textViewName);

            //Table row: TextView Energy
            TextView textViewEnergy = new TextView(getActivity());
            textViewEnergy.setText(fdEnergyCalculated);
            tr1.addView(textViewEnergy);

            //Table row: TextView subLine
            TextView textViewSubLine = new TextView(getActivity());
            textViewSubLine.setText(subLine);
            tr2.addView(textViewSubLine);

            //Add row to table
            t1.addView(tr1, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            t1.addView(tr2, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

            //Add Listener
            tr1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));

                    //Get the row text
                    Context context = getContext();
                    TableRow row = (TableRow) v;
                    TextView tv = (TextView) row.getChildAt(0);

                    //Send it to edit
                    String tvText = "" + tv.getText();
                    rowOnClickDeleteFdLine(tvText);

                }
            });

            //Add Listener
            tr2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));

                    //Get the row text
                    Context context = getContext();
                    TableRow row = (TableRow) v;
                    TextView tv = (TextView) row.getChildAt(0);

                    //Send it to edit
                    String tvText = "" + tv.getText();
                    rowOnClickDeleteFdLine(tvText);

                }
            });

            //Sum fields
            intFdceEnergy = intFdceEnergy + intFdEnergyCalculated;
            intFdceProteins = intFdceProteins + intFdProteinsCalculated;
            intFdceCarbs = intFdceCarbs + intFdCarbsCalculated;
            intFdceFat = intFdceFat + intFdFatCalculated;

            cursorFd.moveToNext();

        }

        //Update fdce
        TextView textViewEnergyX = null;
        if (stringMealNumber.equals("0")) {
            textViewEnergyX = (TextView) getActivity().findViewById(R.id.textViewEnergyBreakfast);
            textViewEnergyX.setText("" + intFdceEnergy);
        }
        else if (stringMealNumber.equals("1")) {
            textViewEnergyX = (TextView) getActivity().findViewById(R.id.textViewEnergyLunch);
            textViewEnergyX.setText("" + intFdceEnergy);
        }
        else if (stringMealNumber.equals("2")) {
            textViewEnergyX = (TextView) getActivity().findViewById(R.id.textViewEnergyBeforeTraining);
            textViewEnergyX.setText("" + intFdceEnergy);
        }
        else if (stringMealNumber.equals("3")) {
            textViewEnergyX = (TextView) getActivity().findViewById(R.id.textViewEnergyAfterTraining);
            textViewEnergyX.setText("" + intFdceEnergy);
        }
        else if (stringMealNumber.equals("4")) {
            textViewEnergyX = (TextView) getActivity().findViewById(R.id.textViewEnergyDinner);
            textViewEnergyX.setText("" + intFdceEnergy);
        }
        else if (stringMealNumber.equals("5")) {
            textViewEnergyX = (TextView) getActivity().findViewById(R.id.textViewEnergySnacks);
            textViewEnergyX.setText("" + intFdceEnergy);
        }
        else if (stringMealNumber.equals("6")) {
            textViewEnergyX = (TextView) getActivity().findViewById(R.id.textViewEnergySupper);
            textViewEnergyX.setText("" + intFdceEnergy);
        }

        String updateFields[] = new String[] {
                "fdce_energy",
                "fdce_proteins",
                "fdce_carbs",
                "fdce_fat"
        };

        String updateValues[] = new String[] {
                "'" + intFdceEnergy + "'",
                "'" + intFdceProteins + "'",
                "'" + intFdceCarbs + "'",
                "'" + intFdceFat + "'"
        };

        db.update("food_diary_cal_eaten", "_id", longFdceId, updateFields, updateValues);

        db.close();

    }

    // addFoodToDiarySelectMealNumber
    private void addFoodToDiarySelectMealNumber() {

        //Change layout
        int newViewID = R.layout.fragment_home_select_meal_number;
        setMainView(newViewID);

        TextView textViewBreakfast = (TextView) getActivity().findViewById(R.id.textViewBreakfast);
        textViewBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFood(0);
            }
        });

        TextView textViewLunch = (TextView) getActivity().findViewById(R.id.textViewLunch);
        textViewLunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFood(1);
            }
        });

        TextView textViewBeforeTraining = (TextView) getActivity().findViewById(R.id.textViewBeforeTraining);
        textViewBeforeTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFood(2);
            }
        });

        TextView textViewAfterTraining = (TextView) getActivity().findViewById(R.id.textViewAfterTraining);
        textViewAfterTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFood(3);
            }
        });

        TextView textViewDinner = (TextView) getActivity().findViewById(R.id.textViewDinner);
        textViewDinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFood(4);
            }
        });

        TextView textViewSnacks = (TextView) getActivity().findViewById(R.id.textViewSnacks);
        textViewSnacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFood(5);
            }
        });

        TextView textViewSupper = (TextView) getActivity().findViewById(R.id.textViewSupper);
        textViewSupper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFood(6);
            }
        });

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

        //Need to pass meal number
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

    }

    //Edit or delete fd line
    private void rowOnClickDeleteFdLine(String stringTableRowTextName) {

        //Database
        DBAdapter db = new DBAdapter(getActivity());
        db.open();

        //Change layout
        int newViewID = R.layout.fragment_home_edit_or_delete;
        setMainView(newViewID);

        //Find information
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
        String stringFdDate = currentDateYear + "/" + currentDateMonth + "/" + currentDateDay;
        String stringFdDateSQL = db.quoteSmart(stringFdDate);
        Cursor cursorFd = db.select("food_diary", fields, "fd_date", stringFdDateSQL);
        String stringFdId = cursorFd.getString(0);

        //Select for food name
        String fieldsFood[] = new String[] {
                " _id",
                " food_name",
                " food_manufactor_name"
        };
        Cursor cursorFood;

        //Ready variables
        String stringFdFoodId = "";
        String stringFdFoodIdSQL = "";

        String stringFdServingSizeGram = "";
        String stringFdServingSizeGramMesurment = "";
        String stringFdServingSizePcs = "";
        String stringFdServingSizePcsMesurment = "";
        String stringFdEnergyCalculated = "";

        String stringFoodID = "";
        String stringFoodName = "";
        String stringFoodManufactorName = "";

        //Loop through cursor, find the corresponding line that has been clicked
        int intCursorFdCount = cursorFd.getCount();
        for (int x = 0; x < intCursorFdCount; x++) {

            //Variables from food diary
            stringFdFoodId = cursorFd.getString(1);
            stringFdFoodIdSQL = db.quoteSmart(stringFdFoodId);

            stringFdServingSizeGram = cursorFd.getString(2);
            stringFdServingSizeGramMesurment = cursorFd.getString(3);
            stringFdServingSizePcs = cursorFd.getString(4);
            stringFdServingSizePcsMesurment = cursorFd.getString(5);
            stringFdEnergyCalculated = cursorFd.getString(6);

            //Get food name
            cursorFood = db.select("food", fieldsFood, "_id", stringFdFoodIdSQL);

            //Variables from food
            stringFoodID = cursorFood.getString(0);
            stringFoodName = cursorFood.getString(1);
            stringFoodManufactorName = cursorFood.getString(2);

            String subLine = stringFoodManufactorName + ", " +
                    stringFdServingSizeGram + " " +
                    stringFdServingSizeGramMesurment + ", " +
                    stringFdServingSizePcs + " " +
                    stringFdServingSizePcsMesurment;

            cursorFd.moveToNext();

        }

        if (stringFoodName.equals("")) {
            Toast.makeText(getActivity(), "Error: Could not load food name", Toast.LENGTH_LONG).show();
        } else {

            //Add to current
            currentFoodName = stringFoodName;
            currentFoodId = stringFoodID;
            currentFdId = stringFdId;

            TextView textViewFoodName = (TextView) getActivity().findViewById(R.id.textViewFoodName);
            textViewFoodName.setText(stringFoodName);

            TextView textViewFoodManufactorName = (TextView) getActivity().findViewById(R.id.textViewFoodManufactorName);
            textViewFoodManufactorName.setText(stringFoodManufactorName);

            EditText editTextServingSizePcs = (EditText) getActivity().findViewById(R.id.editTextServingSizePcs);
            editTextServingSizePcs.setText(stringFdServingSizePcs);

            TextView textViewServingSizePcsMesurment = (TextView) getActivity().findViewById(R.id.textViewServingSizePcsMesurment);
            textViewServingSizePcsMesurment.setText(stringFdServingSizePcsMesurment);

            EditText editTextServingSizeGram = (EditText) getActivity().findViewById(R.id.editTextServingSizeGram);
            editTextServingSizeGram.setText(stringFdServingSizeGram);

            TextView textViewServingSizeGramMesurment = (TextView) getActivity().findViewById(R.id.textViewServingSizeGramMesurment);
            textViewServingSizeGramMesurment.setText(stringFdServingSizeGramMesurment);

            editTextServingSizePcs.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!(s.toString().equals(""))) {
                        editTextPortionSizePcsOnChange();
                    }
                }
            });
            editTextServingSizePcs.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                    } else {
                        String lock = "portionSizePcs";
                        releaseLock(lock);
                    }
                }
            });

            //Listener for editTextPortionSizeGram
            editTextServingSizeGram.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!(s.toString().equals(""))) {
                        editTextPortionSizeGramOnChange();
                    }
                }
            });
            editTextServingSizeGram.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                    } else {
                        String lock = "portionSizeGram";
                        releaseLock(lock);
                    }
                }
            });

            //Watcher
            Button buttonSubmitEdit = (Button) getActivity().findViewById(R.id.buttonSubmitEdit);
            buttonSubmitEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnClickEditFdLineSubmit();
                }
            });
            Button buttonSubmitDelete = (Button) getActivity().findViewById(R.id.buttonSubmitDelete);
            buttonSubmitEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnClickDeleteFdLineSubmit();
                }
            });

        }

        db.close();

    }

    private void releaseLock(String lock) {
        if (lock.equals("portionSizeGram")) {
            lockPortionSizeByGram = false;
        } else {
            lockPortionSizeByPcs = false;
        }
    }

    //editTextPortionSizePcsOnChange
    public void editTextPortionSizePcsOnChange() {

        if (!(lockPortionSizeByGram)) {

            //Lock
            lockPortionSizeByPcs = true;

            //Get value of pcs
            EditText editTextPortionSizePcs = (EditText) getActivity().findViewById(R.id.editTextServingSizePcs);
            String stringPortionSizePcs = editTextPortionSizePcs.getText().toString();

            double doublePortionSizePcs = 0;

            if (stringPortionSizePcs.equals("")) {
                doublePortionSizePcs = 0;
            } else {

                try {
                    doublePortionSizePcs = Double.parseDouble(stringPortionSizePcs);
                } catch (NumberFormatException nfe) {
                    System.out.println("Could not parse " + nfe);
                }

            }

            //Get data from database

            DBAdapter db = new DBAdapter(getActivity());
            db.open();

            String fields[] = new String[] {
                    " food_serving_size_gram"
            };

            String currentIdSQL = db.quoteSmart(currentFoodId);

            Cursor foodCursor = db.select("food", fields, "_id", currentIdSQL);

            //Convert cursor to strings
            String stringServingSize = foodCursor.getString(0);

            db.close();

            //Convert cursor to double
            double doubleServingSize = 0;
            try {
                doubleServingSize = Double.parseDouble(stringServingSize);
            } catch (NumberFormatException nfe) {
                System.out.println("Could not parse " + nfe);
            }

            //Calculate how much n portion size in gram
            // We are changing pcs
            //Update gram
            double doublePortionSizeGram = Math.round(doublePortionSizePcs * doubleServingSize);

            //Update portion size gram
            EditText editTextPortionSizeGram = (EditText) getActivity().findViewById(R.id.editTextServingSizeGram);
            editTextPortionSizeGram.setText("" + doublePortionSizeGram);


        }

    }

    //editTextPortionSizeGramOnChange
    public void editTextPortionSizeGramOnChange() {

        if (!(lockPortionSizeByPcs)) {

            lockPortionSizeByGram = true;

            //Get value of gram
            EditText editTextPortionSizeGram = (EditText) getActivity().findViewById(R.id.editTextServingSizeGram);
            String stringPortionSizeGram = editTextPortionSizeGram.getText().toString();

            double doublePortionSizeGram = 0;

            if (stringPortionSizeGram.equals("")) {
                doublePortionSizeGram = 0;
            } else {

                try {
                    doublePortionSizeGram = Double.parseDouble(stringPortionSizeGram);
                } catch (NumberFormatException nfe) {
                    System.out.println("Could not parse " + nfe);
                }

            }

            //Get data from database

            DBAdapter db = new DBAdapter(getActivity());
            db.open();

            String fields[] = new String[] {
                    " food_serving_size_gram"
            };

            String currentIdSQL = db.quoteSmart(currentFoodId);

            Cursor foodCursor = db.select("food", fields, "_id", currentIdSQL);

            //Convert cursor to strings
            String stringServingSizeGram = foodCursor.getString(0);

            db.close();

            //Convert cursor to double
            double doubleServingSizeGram = 0;
            try {
                doubleServingSizeGram = Double.parseDouble(stringServingSizeGram);
            } catch (NumberFormatException nfe) {
                System.out.println("Could not parse " + nfe);
            }

            //Calculate pcs
            double doublePortionSizePcs = Math.round(doublePortionSizeGram / doubleServingSizeGram);

            //Update portion size gram
            EditText editTextPortionSizePcs = (EditText) getActivity().findViewById(R.id.editTextServingSizePcs);
            editTextPortionSizePcs.setText("" + doublePortionSizePcs);

        }

    }

    //Edit fd line submit
    public void OnClickEditFdLineSubmit() {

        DBAdapter db = new DBAdapter(getActivity());
        db.open();

        //FdID
        long longFdID = 0;
        try {
            longFdID = Long.parseLong(currentFdId);
        } catch (NumberFormatException nfe) {
            System.out.println(nfe);
        }

        //Get food info
        String fields[] = new String[] {
                "food_serving_size_gram",
                "food_energy",
                "food_proteins",
                "food_carbohydrates",
                "food_fat"
        };
        String currentIdSQL = db.quoteSmart(currentFoodId);
        Cursor foodCursor = db.select("food", fields, "_id", currentIdSQL);

        //Convert cursor to strings
        String stringGetFromSQLFoodServingSizeGram = foodCursor.getString(0);
        double doubleGetFromSQLFoodServingSizeGram = Double.parseDouble(stringGetFromSQLFoodServingSizeGram);

        String stringGetFromSQLFoodEnergy = foodCursor.getString(1);
        double doubleGetFromSQLFoodEnergy = Double.parseDouble(stringGetFromSQLFoodEnergy);

        String stringGetFromSQLFoodProteins = foodCursor.getString(2);
        double doubleGetFromSQLFoodProteins = Double.parseDouble(stringGetFromSQLFoodProteins);

        String stringGetFromSQLFoodCarbohydrates = foodCursor.getString(3);
        double doubleGetFromSQLFoodCarbohydrates = Double.parseDouble(stringGetFromSQLFoodCarbohydrates);

        String stringGetFromSQLFoodFat = foodCursor.getString(4);
        double doubleGetFromSQLFoodFat = Double.parseDouble(stringGetFromSQLFoodFat);

        //Update fd serving size gram
        EditText editTextServingSizeGram = (EditText) getActivity().findViewById(R.id.editTextServingSizeGram);
        String stringFdServingSizeGram = editTextServingSizeGram.getText().toString();
        String stringFdServingSizeGramSQL = db.quoteSmart(stringFdServingSizeGram);
        db.update("food_diary", "_id", longFdID, "fd_serving_size_gram", stringFdServingSizeGramSQL);
        double doubleFdServingSizeGram = Double.parseDouble(stringFdServingSizeGram);

        //Update fd serving size pcs
        double doubleFdServingSizePcs = Math.round(doubleFdServingSizeGram / doubleGetFromSQLFoodServingSizeGram);
        String stringFdServingSizePcs = "" + doubleFdServingSizePcs;
        String stringFdServingSizePcsSQL = db.quoteSmart(stringFdServingSizePcs);
        db.update("food_diary", "_id", longFdID, "fd_serving_size_pcs", stringFdServingSizePcsSQL);

        //Update fd energy calculated
        double doubleFdEnergyCalculated = Math.round((doubleFdServingSizeGram * doubleGetFromSQLFoodEnergy) / 100);
        String stringFdEnergyCalculated = "" + doubleFdEnergyCalculated;
        String stringFdEnergyCalculatedSQL = db.quoteSmart(stringFdEnergyCalculated);
        db.update("food_diary", "_id", longFdID, "fd_energy_calculated", stringFdEnergyCalculatedSQL);

        //Update proteins calculated
        double doubleFdProteinsCalculated = Math.round((doubleFdServingSizeGram * doubleGetFromSQLFoodProteins) / 100);
        String stringFdProteinsCalculated = "" + doubleFdProteinsCalculated;
        String stringFdProteinsCalculatedSQL = db.quoteSmart(stringFdProteinsCalculated);
        db.update("food_diary", "_id", longFdID, "fd_protein_calculated", stringFdProteinsCalculatedSQL);

        //Update carbohydrates calculated
        double doubleFdCarbohydratesCalculated = Math.round((doubleFdServingSizeGram * doubleGetFromSQLFoodCarbohydrates) / 100);
        String stringFdCarbohydratesCalculated = "" + doubleFdCarbohydratesCalculated;
        String stringFdCarbohydratesCalculatedSQL = db.quoteSmart(stringFdCarbohydratesCalculated);
        db.update("food_diary", "_id", longFdID, "fd_carbohydrates_calculated", stringFdCarbohydratesCalculatedSQL);

        //Update fat calculated
        double doubleFdFatCalculated = Math.round((doubleFdServingSizeGram * doubleGetFromSQLFoodFat) / 100);
        String stringFdFatCalculated = "" + doubleFdFatCalculated;
        String stringFdFatCalculatedSQL = db.quoteSmart(stringFdFatCalculated);
        db.update("food_diary", "_id", longFdID, "fd_fat_calculated", stringFdFatCalculatedSQL);

        db.close();

        Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, new HomeDietFragment(), HomeDietFragment.class.getName()).commit();

    }

    //Delete fd line submit
    public void OnClickDeleteFdLineSubmit() {

        Toast.makeText(getActivity(), "Deleted " + currentFoodName, Toast.LENGTH_SHORT).show();

        //Database
        DBAdapter db = new DBAdapter(getActivity());
        db.open();

        long longPrimaryKey = 0;
        try {
            longPrimaryKey = Long.parseLong(currentFdId);
        } catch (NumberFormatException nfe) {
            System.out.println(nfe);
        }

        db.delete("food_diary", "_id", longPrimaryKey);

        db.close();

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, new HomeDietFragment(), HomeDietFragment.class.getName()).commit();

    }

}