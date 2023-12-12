package com.example.fitnessappjava.CalorieCounter;

import android.content.Context;

public class DBSetupInsert {

    //Variables
    private final Context context;

    public DBSetupInsert(Context ctx) {
        this.context = ctx;
    }

    //To insert to category table
    public void setupInsertToCategories(String values) {
        DBAdapter db = new DBAdapter(context);
        db.open();
        db.insert("categories",
                "_id, category_name, category_parent_id, category_icon, category_note",
                values);
        db.close();
    }

    public void insertAllCategories() {
        setupInsertToCategories("NULL, 'Bread', '0', '', NULL");
        setupInsertToCategories("NULL, 'Bread', '1', '', NULL");
        setupInsertToCategories("NULL, 'Cereals', '1', '', NULL");
        setupInsertToCategories("NULL, 'Frozen bread and rolls', '1', '', NULL");
        setupInsertToCategories("NULL, 'Crispbread', '1', '', NULL");

        //Parent id:6
        setupInsertToCategories("NULL, 'Dessert and baking', '0', '', NULL");
        setupInsertToCategories("NULL, 'Baking', '6', '', NULL");
        setupInsertToCategories("NULL, 'Biscuit', '6', '', NULL");

        setupInsertToCategories("NULL, 'Drinks', '0', '', NULL");
        setupInsertToCategories("NULL, 'Soda', '9', '', NULL");

        setupInsertToCategories("NULL, 'Fruit and vegetables', '0', '', NULL");
        setupInsertToCategories("NULL, 'Frozen fruit and vegetables', '11', '', NULL");
        setupInsertToCategories("NULL, 'Fruit', '11', '', NULL");
        setupInsertToCategories("NULL, 'Vegetables', '11', '', NULL");
        setupInsertToCategories("NULL, 'Canned fruit and vegetables', '11', '', NULL");

        setupInsertToCategories("NULL, 'Health', '0', '', NULL");
        setupInsertToCategories("NULL, 'Meal substitutes', '16', '', NULL");
        setupInsertToCategories("NULL, 'Protein bars', '16', '', NULL");
        setupInsertToCategories("NULL, 'Protein powder', '16', '', NULL");

        setupInsertToCategories("NULL, 'Meat, chicken and fish', '0', '', NULL");
        setupInsertToCategories("NULL, 'Meat', '20', '', NULL");
        setupInsertToCategories("NULL, 'Chicken', '20', '', NULL");
        setupInsertToCategories("NULL, 'Seafood', '20', '', NULL");

        setupInsertToCategories("NULL, 'Dairy and eggs', '0', '', NULL");
        setupInsertToCategories("NULL, 'Eggs', '24', '', NULL");
        setupInsertToCategories("NULL, 'Cream and sour cream', '24', '', NULL");
        setupInsertToCategories("NULL, 'Yogurt', '24', '', NULL");

        setupInsertToCategories("NULL, 'Dinner', '0', '', NULL");
        setupInsertToCategories("NULL, 'Ready dinner dishes', '28', '', NULL");
        setupInsertToCategories("NULL, 'Pizza', '28', '', NULL");
        setupInsertToCategories("NULL, 'Noodle', '28', '', NULL");
        setupInsertToCategories("NULL, 'Pasta', '28', '', NULL");
        setupInsertToCategories("NULL, 'Rice', '28', '', NULL");
        setupInsertToCategories("NULL, 'Taco', '28', '', NULL");

        setupInsertToCategories("NULL, 'Cheese', '0', '', NULL");
        setupInsertToCategories("NULL, 'Cream cheese', '35', '', NULL");

        setupInsertToCategories("NULL, 'On bread', '0', '', NULL");
        setupInsertToCategories("NULL, 'Cold meats', '37', '', NULL");
        setupInsertToCategories("NULL, 'Sweet spreads', '37', '', NULL");
        setupInsertToCategories("NULL, 'Jam', '37', '', NULL");

        setupInsertToCategories("NULL, 'Snacks', '0', '', NULL");
        setupInsertToCategories("NULL, 'Nuts', '41', '', NULL");
        setupInsertToCategories("NULL, 'Potato chips', '41', '', NULL");
    }

    //To insert to food table
    public void setupInsertToFood(String values) {

        DBAdapter db = new DBAdapter(context);
        db.open();
        db.insert("food",
                "_id, food_name, food_manufactor_name, food_serving_size, food_serving_mesurment, food_serving_name_number, " +
                "food_serving_name_word, food_energy, food_proteins, food_carbohydrates, food_fat, food_energy_calculated, food_proteins_calculated, " +
                "food_carbohydrates_calculated, food_fat_calculated, food_user_id, food_barcode, food_category_id, food_thumb, food_image_a, food_image_b, " +
                "food_image_c, food_notes",
                values);
        db.close();
    }

    //Insert all food into food database
    public void insertAllFood() {
        setupInsertToFood("NULL, 'Pasta', 'Danone', '600', 'gram', '1', 'portion', '512', '16.1', '37.1', '32', '3000', '97', '230', '192', NULL, NULL, 8, NULL, NULL, NULL, NULL, NULL");
    }

}
