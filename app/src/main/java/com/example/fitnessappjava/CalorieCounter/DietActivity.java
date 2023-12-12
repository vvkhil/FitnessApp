package com.example.fitnessappjava.CalorieCounter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.fitnessappjava.R;
import com.example.fitnessappjava.SignInUpProfile.UpdateEmailActivity;
import com.example.fitnessappjava.SignInUpProfile.UpdateProfileActivity;
import com.example.fitnessappjava.SignInUpProfile.UserProfileActivity;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.android.material.navigation.NavigationView;

import okhttp3.OkHttpClient;

public class DietActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Fragment fragment = null;
        Class fragmentClass = null;
        fragmentClass = HomeDietFragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();


        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        NavigationView mNavigationView = (NavigationView) findViewById(R.id.my_drawer_layout);

        if (mNavigationView != null) {
            mNavigationView.setNavigationItemSelectedListener(this);
        }

        //Database
        DBAdapter db = new DBAdapter(this);
        db.open();

        //Count rows in food
        int numberRows = db.count("categories");

        if (numberRows < 1) {
//            db.insert("food",
//                    "food_id, food_name, food_manufactor_name, food_serving_size, food_serving_mesurment, food_energy_calculated",
//                    "NULL, 'Egg, whole, cooked, hard-boiled', 'Prior', '136.0', 'g', '211'");

            Toast.makeText(this, "Loading setup...", Toast.LENGTH_LONG).show();
            DBSetupInsert setupInsert = new DBSetupInsert(this);
//          setupInsert.setupInsertToFood("NULL, 'Pasta', 'Danone', '600', 'gram', '1', 'portion', '512', '16.1', '37.1', '32', '3000', '97', ''230', '192', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL");
            setupInsert.insertAllFood();
            setupInsert.insertAllCategories();
        }

        //Check if there is user in the user table
        //Count rows in user table
        numberRows = db.count("users");
        if(numberRows < 1) {
            Toast.makeText(this, "Sign up...", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(DietActivity.this, SignUpDiet.class);
            startActivity(intent);
        }

        Toast.makeText(this, "There are " + numberRows + " in the table", Toast.LENGTH_LONG).show();

        db.close();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // override the onOptionsItemSelected()
    // function to implement
    // the item click listener callback
    // to open and close the navigation
    // drawer when the icon is clicked

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        Fragment fragment = null;
        Class fragmentClass = null;

        if (id == R.id.nav_home) {
            fragmentClass = HomeDietFragment.class;
        } else if (id == R.id.nav_profile) {
            fragmentClass = ProfileDietFragment.class;
        } else if (id == R.id.nav_goal) {
            fragmentClass = GoalDietFragment.class;
        } else if (id == R.id.nav_categories) {
            fragmentClass = CategoriesDietFragment.class;
        } else if (id == R.id.nav_food) {
            fragmentClass = FoodDietFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}