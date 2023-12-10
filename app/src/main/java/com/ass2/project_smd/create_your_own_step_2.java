package com.ass2.project_smd;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ass2.Helper.CartDBHelper;
import com.ass2.Models.CartModel;

import java.util.ArrayList;
import java.util.Locale;


public class create_your_own_step_2 extends AppCompatActivity {

    Button back;
    ImageView leftHalfPizza,rightHalfPizza;

    TextView txt_left_half_pizza,txt_right_half_pizza;
    TextView priceTextView1, priceTextView2, priceTextView3;
    RelativeLayout create_your_own_rectangle_selected_1_rl, create_your_own_rectangle_selected_2_rl, create_your_own_rectangle_selected_3_rl;

    RelativeLayout floating_cart_rl;
    private RelativeLayout pineappleRl, jalapenosRl, sweetCornRl, pepperoniRl, redOnionsRl, anchoviesRl, groundBeefRl, chickenTikkaRl, mushroomRl, tunaRl;
    private TextView pineappleText, jalapenosText, sweetCornText, pepperoniText, redOnionsText, anchoviesText, groundBeefText, chickenTikkaText, mushroomText, tunaText;

    private TextView txt_cartPrice;
    private int selectedSizeIndex = -1;
    private int selectedCrustIndex = -1;
    private int selectedPizzaToppingsImageIndex = -1;

    private boolean isLeftHalfSelected = true; // Variable to track if the left half is selected
    private int selectedSauceIndexLeft = -1; // Variable to store the selected sauce index
    private int selectedSauceIndexRight = -1; // Variable to store the selected sauce index
    // Variables to store selected toppings for left and right halves
    private final boolean[] selectedToppingsLeft = new boolean[10];
    private final boolean[] selectedToppingsRight = new boolean[10];

    ArrayList<CartModel> cartList = new ArrayList<>();
    float totalPizzaPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_your_own_step2);
        initializeViews();
        getIntentData();
        setupListeners();


        double price = calculateCYOPizzaPrice();
        txt_cartPrice.setText(String.format(Locale.getDefault(), "£%.2f", price)  );



        if (selectedPizzaToppingsImageIndex == -1) {
            selectedPizzaToppingsImageIndex = 0; // Set the default size index to the first item
        }
        if(selectedSauceIndexLeft == -1){
            selectedSauceIndexLeft = 0;
        }
        if(selectedSauceIndexRight == -1){
            selectedSauceIndexRight = 0;
        }




    }

//    private void passTheIntentDate(){
//        Intent intent = new Intent(this, cart.class);
//        intent.putExtra("SELECTED_SIZE", selectedSizeIndex);
//        intent.putExtra("SELECTED_CRUST", selectedCrustIndex);
//        intent.putExtra("SELECTED_PIZZA_TOPPINGS_IMAGE", selectedPizzaToppingsImageIndex);
//        intent.putExtra("SELECTED_SAUCE_LEFT", selectedSauceIndexLeft);
//        intent.putExtra("SELECTED_SAUCE_RIGHT", selectedSauceIndexRight);
//        intent.putExtra("SELECTED_TOPPINGS_LEFT", selectedToppingsLeft);
//        intent.putExtra("SELECTED_TOPPINGS_RIGHT", selectedToppingsRight);
//        startActivity(intent);
//    }

    private void getIntentData() {
        // Retrieve the selected data from the intent
        Intent intentTwo = getIntent();
        if (intentTwo != null) {
            selectedSizeIndex = intentTwo.getIntExtra("SELECTED_SIZE", -1);
            selectedCrustIndex = intentTwo.getIntExtra("SELECTED_CRUST", -1);
            selectedPizzaToppingsImageIndex = intentTwo.getIntExtra("SELECTED_PIZZA_TOPPINGS_IMAGE", -1);

            // Use the selected data as needed
            // ...
        }
    }

    private double calculateCYOPizzaPrice(){
        double calculatedPrice = 0.0;

        switch (selectedSizeIndex) {
            case 0: calculatedPrice += 5.99;  break; // 7 inch pizza
            case 1: calculatedPrice += 7.99;  break; // 9 inch pizza
            case 2: calculatedPrice += 9.99;  break; // 12 inch pizza
            case 3: calculatedPrice += 15.99; break; // 13.5 inch pizza
            default: break; // invalid size
        }

        switch (selectedCrustIndex) {
            case 0: break; // Classic Crust
            case 1: calculatedPrice += 1.00; break; // Italian Crust
            default: break; // Unknown Crust
        }

        return calculatedPrice;
    }

    private void setupListeners() {
        pineappleRl.setOnClickListener(view -> toggleToppingsSelectionSecond(pineappleRl, pineappleText, isLeftHalfSelected));
        jalapenosRl.setOnClickListener(view -> toggleToppingsSelectionSecond(jalapenosRl, jalapenosText, isLeftHalfSelected));
        sweetCornRl.setOnClickListener(view -> toggleToppingsSelectionSecond(sweetCornRl, sweetCornText, isLeftHalfSelected));
        pepperoniRl.setOnClickListener(view -> toggleToppingsSelectionSecond(pepperoniRl, pepperoniText, isLeftHalfSelected));
        redOnionsRl.setOnClickListener(view -> toggleToppingsSelectionSecond(redOnionsRl, redOnionsText, isLeftHalfSelected));
        anchoviesRl.setOnClickListener(view -> toggleToppingsSelectionSecond(anchoviesRl, anchoviesText, isLeftHalfSelected));
        groundBeefRl.setOnClickListener(view -> toggleToppingsSelectionSecond(groundBeefRl, groundBeefText, isLeftHalfSelected));
        chickenTikkaRl.setOnClickListener(view -> toggleToppingsSelectionSecond(chickenTikkaRl, chickenTikkaText, isLeftHalfSelected));
        mushroomRl.setOnClickListener(view -> toggleToppingsSelectionSecond(mushroomRl, mushroomText, isLeftHalfSelected));
        tunaRl.setOnClickListener(view -> toggleToppingsSelectionSecond(tunaRl, tunaText, isLeftHalfSelected));

        floating_cart_rl.setOnClickListener(view -> {

            if(selectedSauceIndexLeft == -1){selectedSauceIndexLeft = 0; } // "BBQ"
            if(selectedSauceIndexRight == -1){selectedSauceIndexRight = 0;}// "BBQ"

            if (selectedSizeIndex != -1 && selectedCrustIndex != -1 && selectedPizzaToppingsImageIndex != -1 &&
                    selectedSauceIndexLeft != -1 && selectedSauceIndexRight != -1 &&
                    selectedToppingsLeft != null && selectedToppingsRight != null) {
                // Add the item to the database
                boolean dbSuccess = addItemCreateYourOwnPizzaToLocalDB(selectedSizeIndex, selectedCrustIndex, selectedPizzaToppingsImageIndex,
                        selectedSauceIndexLeft, selectedSauceIndexRight,
                        selectedToppingsLeft, selectedToppingsRight,
                        1,R.drawable.pizza4);
                if(dbSuccess){
                    Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show();
                    //navigate to the nav_bar
                    Intent intent = new Intent(this, nav_bar.class);
                    startActivity(intent);


                    //passTheIntentDate();
                }
                else{
                    Toast.makeText(this, "Data Not Inserted", Toast.LENGTH_SHORT).show();
                }
            }
        });

        leftHalfPizza.setOnClickListener(view -> toggleToppingsSelection(true));
        rightHalfPizza.setOnClickListener(view -> toggleToppingsSelection(false));

        back.setOnClickListener(v -> {
            // Navigate to the previous activity
            finish();
        });


        priceTextView1.setOnClickListener(view -> toggleSauceSelection(0,isLeftHalfSelected));
        priceTextView2.setOnClickListener(view -> toggleSauceSelection(1,isLeftHalfSelected));
        priceTextView3.setOnClickListener(view -> toggleSauceSelection(2,isLeftHalfSelected));



    }
    private void initializeViews() {
        back = findViewById(R.id.back);

        leftHalfPizza = findViewById(R.id.img_left_half_pizza);
        rightHalfPizza = findViewById(R.id.img_right_half_pizza);
        txt_left_half_pizza = findViewById(R.id.txt_left_half_pizza);
        txt_right_half_pizza = findViewById(R.id.txt_right_half_pizza);

        priceTextView1 = findViewById(R.id.priceTextView1);
        priceTextView2 = findViewById(R.id.priceTextView2);
        priceTextView3 = findViewById(R.id.priceTextView3);
        create_your_own_rectangle_selected_1_rl = findViewById(R.id.create_your_own_rectangle_selected_1_rl);
        create_your_own_rectangle_selected_2_rl = findViewById(R.id.create_your_own_rectangle_selected_2_rl);
        create_your_own_rectangle_selected_3_rl = findViewById(R.id.create_your_own_rectangle_selected_3_rl);
        txt_cartPrice = findViewById(R.id.add_to_cart_floating_price_text);


        pineappleRl = findViewById(R.id.pineaple_rl);
        jalapenosRl = findViewById(R.id.jalapenos_rl);
        sweetCornRl = findViewById(R.id.sweet_corn_rl);
        pepperoniRl = findViewById(R.id.pepperoni_rl);
        redOnionsRl = findViewById(R.id.red_onions_rl);
        anchoviesRl = findViewById(R.id.anchovies_rl);
        groundBeefRl = findViewById(R.id.ground_beef_rl);
        chickenTikkaRl = findViewById(R.id.chicken_tikka_rl);
        mushroomRl = findViewById(R.id.mushroom_rl);
        tunaRl = findViewById(R.id.tuna_rl);

        pineappleText = findViewById(R.id.pineaple_text);
        jalapenosText = findViewById(R.id.jalapenos_text);
        sweetCornText = findViewById(R.id.sweet_corn_text);
        pepperoniText = findViewById(R.id.pepperoni_text);
        redOnionsText = findViewById(R.id.red_onions_text);
        anchoviesText = findViewById(R.id.anchovies_text);
        groundBeefText = findViewById(R.id.ground_beef_text);
        chickenTikkaText = findViewById(R.id.chicken_tikka_text);
        mushroomText = findViewById(R.id.mushroom_text);
        tunaText = findViewById(R.id.tuna_text);

        floating_cart_rl = findViewById(R.id.floating_cart_rl);



    }
    private boolean addItemCreateYourOwnPizzaToLocalDB(int Size, int Crust , int ToppingsImage,
                                                       int SauceLeft, int SauceRight,
                                                       boolean[] ToppingsLeft, boolean[] ToppingsRight,
                                                       int quantity,int image){

        String[] toppingsNames = {
                "Pineapple", "Jalapenos", "Sweet Corn", "Pepperoni", "Red Onions",
                "Anchovies", "Ground Beef", "Chicken Tikka", "Mushroom", "Tuna"
        };

        totalPizzaPrice = 0;


        // Check if data exists from intents and add to the list accordingly
        ArrayList<String> returnToppingsLeftList = new ArrayList<>();
        ArrayList<String> returnToppingsRightList = new ArrayList<>();

        float returnSize;
        switch (Size) {
            case 0: returnSize = 7.0f;  totalPizzaPrice += 5.99;  break; // 7 inch pizza
            case 1: returnSize = 9.0f;  totalPizzaPrice += 7.99;  break; // 9 inch pizza
            case 2: returnSize = 12.0f; totalPizzaPrice += 9.99;  break; // 12 inch pizza
            case 3: returnSize = 13.5f; totalPizzaPrice += 15.99; break; // 13.5 inch pizza
            default: returnSize = -1; break; // invalid size
        }



        // Process Crust
        String returnCrust;
        switch (Crust) {
            case 0: returnCrust = "Classic Crust"; break;
            case 1:
                returnCrust = "Italian Crust"; //add extra 1.00 if italian crust
                totalPizzaPrice += 1.00;
                break;
            default: returnCrust = "Unknown Crust"; break;
        }

        // Process ToppingsImage
        String returnToppingsImage;
        switch (ToppingsImage) {
            case 0: returnToppingsImage = "Vegetable Toppings"; break;
            case 1: returnToppingsImage = "Cheese toppings"; break;
            default: returnToppingsImage = "Unknown toppings"; break;
        }

        // Process SauceLeft
        String returnSauceLeft;
        switch (SauceLeft) {
            case 0: returnSauceLeft = "BBQ"; break;
            case 1: returnSauceLeft = "Tomato"; break;
            case 2: returnSauceLeft = "Garlic Herb"; break;
            default: returnSauceLeft = "Unknown Sauce"; break;
        }

        // Process SauceRight
        String returnSauceRight;
        switch (SauceRight) {
            case 0: returnSauceRight = "BBQ"; break;
            case 1: returnSauceRight = "Tomato"; break;
            case 2: returnSauceRight = "Garlic Herb"; break;
            default: returnSauceRight = "Unknown Sauce"; break;
        }

        // Process ToppingsLeft
        if (ToppingsLeft != null && ToppingsLeft.length > 0) {
            for (int i = 0; i < ToppingsLeft.length; i++) {
                if (ToppingsLeft[i]) {
                    // Ensure index i corresponds to a valid topping name
                    if (i < toppingsNames.length) {
                        returnToppingsLeftList.add(toppingsNames[i]);
                    }
                }
            }
        }

        // Process ToppingsRight
        if (ToppingsRight != null && ToppingsRight.length > 0) {
            for (int i = 0; i < ToppingsRight.length; i++) {
                if (ToppingsRight[i]) {
                    // Ensure index i corresponds to a valid topping name
                    if (i < toppingsNames.length) {
                        returnToppingsRightList.add(toppingsNames[i]);
                    }
                }
            }
        }


        totalPizzaPrice *= quantity;

        CartModel newCartItem = new CartModel(
                quantity,
                "Create Your Own",
                String.format(Locale.getDefault(), "%.2f", totalPizzaPrice),
                image,
                0,
                returnSize,
                returnCrust,
                returnToppingsImage,
                returnSauceLeft,
                returnSauceRight,
                returnToppingsLeftList,
                returnToppingsRightList);

        // Add the item to the database
        CartDBHelper dbHelper = new CartDBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long insertSuccess = dbHelper.insertCartItem(newCartItem);

        db.close(); // Close the database connection
        if(insertSuccess == -1)
            return false;
        else
            return true;


    }



    private void toggleToppingsSelectionLeft(RelativeLayout selectedLayout, TextView selectedTextView) {
        boolean isSelected = selectedLayout.getTag() != null && (boolean) selectedLayout.getTag();
        int index = getToppingIndex(selectedLayout);

        if (isSelected) {
            selectedLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.create_your_own_rectangle_unselected));
            selectedTextView.setTextColor(Color.parseColor("#24262F"));
            selectedTextView.setAlpha(0.5f);
            selectedLayout.setTag(false);
            selectedToppingsLeft[index] = false;
        } else {
            selectedLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.create_your_own_rectangle_selected));
            selectedTextView.setTextColor(Color.parseColor("#FE724C"));
            selectedTextView.setAlpha(1.0f);
            selectedLayout.setTag(true);
            selectedToppingsLeft[index] = true;
        }
    }

    private void toggleToppingsSelectionRight(RelativeLayout selectedLayout, TextView selectedTextView) {
        boolean isSelected = selectedLayout.getTag() != null && (boolean) selectedLayout.getTag();
        int index = getToppingIndex(selectedLayout);

        if (isSelected) {
            selectedLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.create_your_own_rectangle_unselected));
            selectedTextView.setTextColor(Color.parseColor("#24262F"));
            selectedTextView.setAlpha(0.5f);
            selectedLayout.setTag(false);
            selectedToppingsRight[index] = false;
        } else {
            selectedLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.create_your_own_rectangle_selected));
            selectedTextView.setTextColor(Color.parseColor("#FE724C"));
            selectedTextView.setAlpha(1.0f);
            selectedLayout.setTag(true);
            selectedToppingsRight[index] = true;
        }
    }
    // Method to get topping index based on the selected layout ID

    private int getToppingIndex(RelativeLayout selectedLayout) {
        int index =-1;
        if (selectedLayout.getId() == R.id.pineaple_rl) index = 0; // Pineapple
        else if (selectedLayout.getId() == R.id.jalapenos_rl) index = 1; // Jalapenos
        else if (selectedLayout.getId() == R.id.sweet_corn_rl) index = 2; // Sweet Corn
        else if (selectedLayout.getId() == R.id.pepperoni_rl) index = 3; // Pepperoni
        else if (selectedLayout.getId() == R.id.red_onions_rl) index = 4; // Red Onions
        else if (selectedLayout.getId() == R.id.anchovies_rl) index = 5; // Anchovies
        else if (selectedLayout.getId() == R.id.ground_beef_rl) index = 6; // Ground Beef
        else if (selectedLayout.getId() == R.id.chicken_tikka_rl) index = 7; // Chicken Tikka
        else if (selectedLayout.getId() == R.id.mushroom_rl) index = 8; // Mushroom
        else if (selectedLayout.getId() == R.id.tuna_rl) index = 9; // Tuna
        return index;
    }


    private void toggleToppingsSelection(boolean isLeftSelected) {

        isLeftHalfSelected = isLeftSelected;
        if (isLeftSelected) {
            leftHalfPizza.setAlpha(1.0f);
            rightHalfPizza.setAlpha(0.5f);
            txt_right_half_pizza.setTextColor(Color.parseColor("#24262F"));
            txt_left_half_pizza.setTextColor(Color.parseColor("#FE724C"));

            toggleSauceSelectionForHalf(selectedSauceIndexLeft, true); // Update UI for left half sauce
            toggleToppingsSelectionForHalf(selectedToppingsLeft);

        } else {
            leftHalfPizza.setAlpha(0.5f);
            rightHalfPizza.setAlpha(1.0f);
            txt_left_half_pizza.setTextColor(Color.parseColor("#24262F"));
            txt_right_half_pizza.setTextColor(Color.parseColor("#FE724C"));

            toggleSauceSelectionForHalf(selectedSauceIndexRight, false); // Update UI for right half sauce
            toggleToppingsSelectionForHalf(selectedToppingsRight);
        }
    }
    private void toggleToppingsSelectionForHalf(boolean[] selectedToppings) {
        // Toppings for left half
        pineappleRl.setBackground(ContextCompat.getDrawable(this, selectedToppings[0] ? R.drawable.create_your_own_rectangle_selected : R.drawable.create_your_own_rectangle_unselected));
        pineappleText.setTextColor(selectedToppings[0] ? Color.parseColor("#FE724C") : Color.parseColor("#24262F"));
        pineappleText.setAlpha(selectedToppings[0] ? 1.0f : 0.5f);
        pineappleRl.setTag(selectedToppings[0]);

        jalapenosRl.setBackground(ContextCompat.getDrawable(this, selectedToppings[1] ? R.drawable.create_your_own_rectangle_selected : R.drawable.create_your_own_rectangle_unselected));
        jalapenosText.setTextColor(selectedToppings[1] ? Color.parseColor("#FE724C") : Color.parseColor("#24262F"));
        jalapenosText.setAlpha(selectedToppings[1] ? 1.0f : 0.5f);
        jalapenosRl.setTag(selectedToppings[1]);

        sweetCornRl.setBackground(ContextCompat.getDrawable(this, selectedToppings[2] ? R.drawable.create_your_own_rectangle_selected : R.drawable.create_your_own_rectangle_unselected));
        sweetCornText.setTextColor(selectedToppings[2] ? Color.parseColor("#FE724C") : Color.parseColor("#24262F"));
        sweetCornText.setAlpha(selectedToppings[2] ? 1.0f : 0.5f);
        sweetCornRl.setTag(selectedToppings[2]);

        pepperoniRl.setBackground(ContextCompat.getDrawable(this, selectedToppings[3] ? R.drawable.create_your_own_rectangle_selected : R.drawable.create_your_own_rectangle_unselected));
        pepperoniText.setTextColor(selectedToppings[3] ? Color.parseColor("#FE724C") : Color.parseColor("#24262F"));
        pepperoniText.setAlpha(selectedToppings[3] ? 1.0f : 0.5f);

        redOnionsRl.setBackground(ContextCompat.getDrawable(this, selectedToppings[4] ? R.drawable.create_your_own_rectangle_selected : R.drawable.create_your_own_rectangle_unselected));
        redOnionsText.setTextColor(selectedToppings[4] ? Color.parseColor("#FE724C") : Color.parseColor("#24262F"));
        redOnionsText.setAlpha(selectedToppings[4] ? 1.0f : 0.5f);

        anchoviesRl.setBackground(ContextCompat.getDrawable(this, selectedToppings[5] ? R.drawable.create_your_own_rectangle_selected : R.drawable.create_your_own_rectangle_unselected));
        anchoviesText.setTextColor(selectedToppings[5] ? Color.parseColor("#FE724C") : Color.parseColor("#24262F"));
        anchoviesText.setAlpha(selectedToppings[5] ? 1.0f : 0.5f);

        groundBeefRl.setBackground(ContextCompat.getDrawable(this, selectedToppings[6] ? R.drawable.create_your_own_rectangle_selected : R.drawable.create_your_own_rectangle_unselected));
        groundBeefText.setTextColor(selectedToppings[6] ? Color.parseColor("#FE724C") : Color.parseColor("#24262F"));
        groundBeefText.setAlpha(selectedToppings[6] ? 1.0f : 0.5f);

        chickenTikkaRl.setBackground(ContextCompat.getDrawable(this, selectedToppings[7] ? R.drawable.create_your_own_rectangle_selected : R.drawable.create_your_own_rectangle_unselected));
        chickenTikkaText.setTextColor(selectedToppings[7] ? Color.parseColor("#FE724C") : Color.parseColor("#24262F"));
        chickenTikkaText.setAlpha(selectedToppings[7] ? 1.0f : 0.5f);

        mushroomRl.setBackground(ContextCompat.getDrawable(this, selectedToppings[8] ? R.drawable.create_your_own_rectangle_selected : R.drawable.create_your_own_rectangle_unselected));
        mushroomText.setTextColor(selectedToppings[8] ? Color.parseColor("#FE724C") : Color.parseColor("#24262F"));
        mushroomText.setAlpha(selectedToppings[8] ? 1.0f : 0.5f);

        tunaRl.setBackground(ContextCompat.getDrawable(this, selectedToppings[9] ? R.drawable.create_your_own_rectangle_selected : R.drawable.create_your_own_rectangle_unselected));
        tunaText.setTextColor(selectedToppings[9] ? Color.parseColor("#FE724C") : Color.parseColor("#24262F"));
        tunaText.setAlpha(selectedToppings[9] ? 1.0f : 0.5f);


        // Continue the pattern for other toppings similarly
        // Update the UI for all toppings based on the boolean values in the selectedToppings array
    }

    // Inside toggleToppingsSelectionSecond(RelativeLayout selectedLayout, TextView selectedTextView, boolean isLeftHalfSelected) method
    private void toggleToppingsSelectionSecond(RelativeLayout selectedLayout, TextView selectedTextView, boolean isLeftHalfSelected) {
        if (isLeftHalfSelected) {
            toggleToppingsSelectionLeft(selectedLayout, selectedTextView);
        } else {
            toggleToppingsSelectionRight(selectedLayout, selectedTextView);
        }
    }
    // Inside toggleSauceSelectionForHalf(int sauceNumber, boolean isLeftHalfSelected) method
    private void toggleSauceSelectionForHalf(int sauceNumber, boolean isLeftHalfSelected) {
        switch (sauceNumber) {
            case 0:
                setSauceSelection(create_your_own_rectangle_selected_1_rl, priceTextView1, isLeftHalfSelected);
                updateSauceIndex(0, isLeftHalfSelected);
                break;
            case 1:
                setSauceSelection(create_your_own_rectangle_selected_2_rl, priceTextView2, isLeftHalfSelected);
                updateSauceIndex(1, isLeftHalfSelected);
                break;
            case 2:
                setSauceSelection(create_your_own_rectangle_selected_3_rl, priceTextView3, isLeftHalfSelected);
                updateSauceIndex(2, isLeftHalfSelected);
                break;
        }
    }


    // Inside toggleSauceSelection(int sauceNumber, boolean isLeftHalfSelected) method
    private void toggleSauceSelection(int sauceNumber, boolean isLeftHalfSelected) {
        toggleSauceSelectionForHalf(sauceNumber, isLeftHalfSelected);
    }
    private void updateSauceIndex(int sauceIndex, boolean isLeftHalfSelected) {
        if (isLeftHalfSelected) {
            selectedSauceIndexLeft = sauceIndex;
        } else {
            selectedSauceIndexRight = sauceIndex;
        }
    }
    private void setSauceSelection(RelativeLayout selectedLayout, TextView selectedTextView, boolean isLeftHalfSelected) {
        create_your_own_rectangle_selected_1_rl.setBackground(ContextCompat.getDrawable(this, R.drawable.create_your_own_rectangle_unselected));
        create_your_own_rectangle_selected_2_rl.setBackground(ContextCompat.getDrawable(this, R.drawable.create_your_own_rectangle_unselected));
        create_your_own_rectangle_selected_3_rl.setBackground(ContextCompat.getDrawable(this, R.drawable.create_your_own_rectangle_unselected));

        priceTextView1.setTextColor(Color.parseColor("#24262F"));
        priceTextView2.setTextColor(Color.parseColor("#24262F"));
        priceTextView3.setTextColor(Color.parseColor("#24262F"));

        selectedLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.create_your_own_rectangle_selected));
        selectedTextView.setTextColor(Color.parseColor("#FE724C"));

        priceTextView1.setAlpha(0.5f);
        priceTextView2.setAlpha(0.5f);
        priceTextView3.setAlpha(0.5f);

        selectedTextView.setAlpha(1.0f);

        // Update sauce index based on selected half
        int sauceIndex = getSauceIndex(selectedLayout);
        updateSauceIndex(sauceIndex, isLeftHalfSelected);
    }

    // Method to get sauce index based on the selected layout ID

    private int getSauceIndex(RelativeLayout selectedLayout) {
        int index = -1;
        if (selectedLayout.getId() == R.id.create_your_own_rectangle_selected_1_rl) index = 0; // Pineapple
        else if (selectedLayout.getId() == R.id.create_your_own_rectangle_selected_2_rl) index = 1; // Jalapenos
        else if (selectedLayout.getId() == R.id.create_your_own_rectangle_selected_3_rl) index = 2; // Sweet Corn

        return index;
        }
    }
