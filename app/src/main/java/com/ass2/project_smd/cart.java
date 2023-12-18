package com.ass2.project_smd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ass2.Adapters.CartAdapter;
import com.ass2.Adapters.MainAdapter;
import com.ass2.Helper.DBHelper;
import com.ass2.Helper.OrderDBHelper;
import com.ass2.Models.CartModel;
import com.ass2.Models.MainModel;

import java.util.ArrayList;
import java.util.Arrays;
import com.ass2.Helper.CartDBHelper;
import com.ass2.Models.OrderModel;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class cart extends AppCompatActivity  {

    RecyclerView cartRecyclerView;
    ImageButton backButton;
    Button checkoutButton;
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
        setupCheckoutButton();
    }

    private void setupCheckoutButton() {
        checkoutButton.setOnClickListener(v -> {
            CartDBHelper cartDbHelper = new CartDBHelper(cart.this);
            ArrayList<CartModel> cartItems = cartDbHelper.getAllCartItems();

            if (cartItems.size() == 0) {
                Toast.makeText(cart.this, "No items in the cart", Toast.LENGTH_SHORT).show();
                return;
            }
            SharedPreferences sharedPrefs = getSharedPreferences("userPrefs", MODE_PRIVATE);
            boolean isLogged = sharedPrefs.getBoolean("isLogged", false);
            String loginMethod = sharedPrefs.getString("loginMethod", "");
            String email = sharedPrefs.getString("email", "");
            String name = sharedPrefs.getString("name", "");
            String delivery_address = sharedPrefs.getString("delivery_address", "");
            String phone = sharedPrefs.getString("phone", "");


            if(isLogged){
                OrderModel order = createOrderFromCart(cartItems,name,delivery_address,email,phone);
                OrderDBHelper orderDbHelper = new OrderDBHelper(cart.this);
                long orderId = orderDbHelper.insertOrder(order);

                if (orderId != -1) {
                    cartDbHelper.clearCart();
                    Toast.makeText(cart.this, "Order placed successfully! Order ID: " + orderId, Toast.LENGTH_LONG).show();

                    sendCheckoutNotification(orderId);
                    // Optionally navigate to a new activity or update UI
                    Intent intent = new Intent(cart.this, nav_bar.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(cart.this, "Failed to place order", Toast.LENGTH_SHORT).show();
                }

            }

            //OrderDBHelper orderDbHelper = new OrderDBHelper(cart.this);


        });
    }
    private void sendCheckoutNotification(long orderId) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // Create a NotificationChannel if API level 26 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("OrderChannel", "Order Notifications", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Notifications for order updates");
            channel.enableLights(true);
            channel.setLightColor(Color.BLUE);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
        }
        // Create an Intent to open the activity that hosts MyOrdersFragment
        Intent intent = new Intent(this, nav_bar.class); // Replace NavBarActivity.class with your actual activity class that hosts the MyOrdersFragment
        intent.putExtra("openFragment", "MyOrdersFragment"); // Extra information to specify which fragment to open
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(this)
                .setContentTitle("Order Placed")
                .setContentText("Your order has been placed successfully. Order ID: " + orderId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setChannelId("OrderChannel")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true) // Automatically removes the notification when tapped
                .build();

        notificationManager.notify((int) orderId, notification);

    }

    private void initializeViews() {
        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        backButton = findViewById(R.id.back);
        noCartItems = findViewById(R.id.noCartItems);
        subtotal = findViewById(R.id.totalPriceValue);
        totalPriceLayout = findViewById(R.id.totalPriceLayout);
        checkoutButton = findViewById(R.id.checkoutButton);
    }

    private void setupRecyclerViewViaLocalDB() {
        CartDBHelper dbHelper = new CartDBHelper(this);
        ArrayList<CartModel> cartItems = dbHelper.getAllCartItems();
        CartAdapter adapter = new CartAdapter(cartItems, this, subtotal);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartRecyclerView.setAdapter(adapter);

        updateCartVisibility(cartItems.size()); // Update cart visibility based on the number of items
    }

    private OrderModel createOrderFromCart(ArrayList<CartModel> cartItems,String customerName,String customerAddress,String customerEmail,String customerPhone){
        OrderModel order = new OrderModel();

        // Set the customer name, ordered items, total price, order status, and order date based on cart items
        order.setCustomerName(customerName); // TODO: Get the customer name from the logged-in user
        order.setCustomerAddress(customerAddress);
        order.setCustomerPhone(customerPhone);
        order.setCustomerEmail(customerEmail);
        order.setOrderedItems(cartItems);
        order.setTotalPrice(subtotal.getText().toString());
        order.setOrderStatus("Pending");
        order.setOrderDate(getCurrentDateTime());
        return order;
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
        backButton.setOnClickListener(
                v -> finish()
        );
    }

    // Inside your cart activity class
    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }



}