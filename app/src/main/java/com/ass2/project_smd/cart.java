package com.ass2.project_smd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ass2.Adapters.CartAdapter;
import com.ass2.Adapters.MainAdapter;
import com.ass2.Helper.DBHelper;
import com.ass2.Models.CartModel;
import com.ass2.Models.MainModel;

import java.util.ArrayList;
import java.util.Arrays;
import com.ass2.Helper.CartDBHelper;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class cart extends AppCompatActivity  {

    RecyclerView cartRecyclerView;
    ImageButton backButton;
    TextView noCartItems;

    LinearLayout totalPriceLayout;

    final int itemCountCreateYourOwn = 0;
    int selectedSize = -1, selectedCrust = -1, selectedToppingsImage = -1,
            selectedSauceLeft = -1, selectedSauceRight = -1;
    boolean[] selectedToppingsLeft = null, selectedToppingsRight = null;
    TextView subtotal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initializeViews();
        setupRecyclerViewViaLocalDB();
        //getTheIntentData();
        updateSubtotal();
        setupBackButton();
    }

    private void initializeViews() {
        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        backButton = findViewById(R.id.back);
        noCartItems = findViewById(R.id.noCartItems);
        subtotal = findViewById(R.id.totalPriceValue);
        totalPriceLayout = findViewById(R.id.totalPriceLayout);
    }

    private void setupRecyclerViewViaLocalDB() {
        CartDBHelper dbHelper = new CartDBHelper(this);
        ArrayList<CartModel> cartItems = dbHelper.getAllCartItems();
        CartAdapter adapter = new CartAdapter(cartItems, this, subtotal);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartRecyclerView.setAdapter(adapter);

        updateCartVisibility(cartItems.size()); // Update cart visibility based on the number of items
    }



    private void updateSubtotal() {
        CartDBHelper dbHelper = new CartDBHelper(this);
        ArrayList<CartModel> cartItems = dbHelper.getAllCartItems();
        float subtotalValue = (float) dbHelper.calculateSubtotal();
        subtotal.setText(String.valueOf(subtotalValue));
    }

    private void updateCartVisibility(int itemCount) {
        if(itemCount == 0){
            noCartItems.setVisibility(TextView.VISIBLE);
            cartRecyclerView.setVisibility(RecyclerView.GONE);
            totalPriceLayout.setVisibility(LinearLayout.GONE);
        } else {
            noCartItems.setVisibility(TextView.GONE);
            cartRecyclerView.setVisibility(RecyclerView.VISIBLE);
            totalPriceLayout.setVisibility(LinearLayout.VISIBLE);
        }
    }



    private void getTheIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            selectedSize = intent.getIntExtra("selectedSize", -1);
            selectedCrust = intent.getIntExtra("selectedCrust", -1);
            selectedToppingsImage = intent.getIntExtra("selectedToppingsImage", -1);
            selectedSauceLeft = intent.getIntExtra("selectedSauceLeft", -1);
            selectedSauceRight = intent.getIntExtra("selectedSauceRight", -1);
            selectedToppingsLeft = intent.getBooleanArrayExtra("selectedToppingsLeft");
            selectedToppingsRight = intent.getBooleanArrayExtra("selectedToppingsRight");
        }
    }

    private void setupBackButton() {
        backButton.setOnClickListener(v -> finish());
    }

    // Inside your cart activity class
    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }



}