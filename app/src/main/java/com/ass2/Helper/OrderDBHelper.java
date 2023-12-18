package com.ass2.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ass2.Models.OrderModel;
import com.ass2.config.LocalDBVersion;

import java.util.ArrayList;

public class OrderDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cheesybites.db";
    private static final int DATABASE_VERSION = LocalDBVersion.DB_VERSION;

    // Table name
    public static final String TABLE_ORDERS = "orders";
    public static final String COLUMN_ORDERED_ITEMS = "ordered_items";

    // Columns
    public static final String COLUMN_ORDER_ID = "order_id";
    public static final String COLUMN_USER_ID = "user_id"; // Assuming there's a user associated with each order

    // Add new columns
    public static final String COLUMN_CUSTOMER_NAME = "customer_name";
    public static final String COLUMN_TOTAL_PRICE = "total_price";
    public static final String COLUMN_ORDER_STATUS = "order_status";
    public static final String COLUMN_ORDER_DATE = "order_date";
    public static final String COLUMN_CUSTOMER_ADDRESS = "customer_address";
    public static final String COLUMN_CUSTOMER_PHONE = "customer_phone";
    public static final String COLUMN_CUSTOMER_EMAIL = "customer_email";

    // Constructor
    public OrderDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        Log.d("DBDebug", "Database deleted for fresh start");

        SQLiteDatabase db = this.getWritableDatabase(); // This should trigger onCreate
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DBDebug", "Creating new database version: " + DATABASE_VERSION);

        String CREATE_ORDERS_TABLE = "CREATE TABLE " + TABLE_ORDERS + "("
                + COLUMN_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_CUSTOMER_NAME + " TEXT,"
                + COLUMN_TOTAL_PRICE + " TEXT,"
                + COLUMN_ORDER_STATUS + " TEXT,"
                + COLUMN_ORDER_DATE + " TEXT,"
                + COLUMN_CUSTOMER_ADDRESS + " TEXT," // Added customer address column
                + COLUMN_CUSTOMER_PHONE + " TEXT," // Added customer phone column
                + COLUMN_CUSTOMER_EMAIL + " TEXT," // Added customer email column
                + COLUMN_ORDERED_ITEMS + " TEXT"
                + ")";
        db.execSQL(CREATE_ORDERS_TABLE);

        Log.d("DBDebug", "Orders table created: " + CREATE_ORDERS_TABLE);
    }

    // Implement methods for inserting, updating, deleting, and querying orders
    public long insertOrder(OrderModel order) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Populate values from the order object
        values.put(COLUMN_CUSTOMER_NAME, order.getCustomerName());
        values.put(COLUMN_TOTAL_PRICE, order.getTotalPrice());
        values.put(COLUMN_ORDER_STATUS, order.getOrderStatus());
        values.put(COLUMN_ORDER_DATE, order.getOrderDate());
        values.put(COLUMN_CUSTOMER_ADDRESS, order.getCustomerAddress()); // Added customer address
        values.put(COLUMN_CUSTOMER_PHONE, order.getCustomerPhone()); // Added customer phone
        values.put(COLUMN_CUSTOMER_EMAIL, order.getCustomerEmail()); // Added customer email
        values.put(COLUMN_ORDERED_ITEMS, order.getOrderedItemsJson()); // Serialize CartModel list

        long newRowId = -1;

        try {
            newRowId = db.insert(TABLE_ORDERS, null, values);

            if (newRowId == -1) {
                Log.e("InsertOrderDebug", "Insertion failed");
            } else {
                Log.d("InsertOrderDebug", "Insertion successful. Row ID: " + newRowId);
            }
        } catch (Exception e) {
            Log.e("InsertOrderDebug", "Error during insertion: " + e.getMessage());

            // Log the SQL statement being executed
            Log.e("InsertOrderDebug", "SQL: " + db.compileStatement("INSERT INTO " + TABLE_ORDERS + " (" +
                            COLUMN_CUSTOMER_NAME + ", " +
                            COLUMN_TOTAL_PRICE + ", " +
                            COLUMN_ORDER_STATUS + ", " +
                            COLUMN_ORDER_DATE + ") VALUES (?, ?, ?, ?)")
                    .toString());

            e.printStackTrace(); // Print the stack trace for more detailed error information
        } finally {
            db.close();
        }

        return newRowId;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DBDebug", "Upgrading database from version " + oldVersion + " to " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        onCreate(db);
    }

    public ArrayList<OrderModel> getAllOrders() {
        ArrayList<OrderModel> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ORDERS, null);


        if (cursor.moveToFirst()) {
            do {

                OrderModel order = new OrderModel();
                order.setOrderId(cursor.getInt(0));
                order.setCustomerName(cursor.getString(1));
                order.setTotalPrice(cursor.getString(2));
                order.setOrderStatus(cursor.getString(3));
                order.setOrderDate(cursor.getString(4));
                order.setCustomerAddress(cursor.getString(5)); // Added customer address
                order.setCustomerPhone(cursor.getString(6)); // Added customer phone
                order.setCustomerEmail(cursor.getString(7)); // Added customer email
                order.setOrderedItemsFromJson(cursor.getString(8)); // Deserialize CartModel list

                list.add(order);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return list;
    }

    public ArrayList<OrderModel> getOrdersWithEmail(String email){
        ArrayList<OrderModel> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ORDERS + " WHERE " + COLUMN_CUSTOMER_EMAIL + " = '" + email + "'", null);

        if (cursor.moveToFirst()) {
            do {

                OrderModel order = new OrderModel();
                order.setOrderId(cursor.getInt(0));
                order.setCustomerName(cursor.getString(1));
                order.setTotalPrice(cursor.getString(2));
                order.setOrderStatus(cursor.getString(3));
                order.setOrderDate(cursor.getString(4));
                order.setCustomerAddress(cursor.getString(5)); // Added customer address
                order.setCustomerPhone(cursor.getString(6)); // Added customer phone
                order.setCustomerEmail(cursor.getString(7)); // Added customer email
                order.setOrderedItemsFromJson(cursor.getString(8)); // Deserialize CartModel list

                list.add(order);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return list;


    }


    // Additional helper methods (insertOrder, updateOrder, deleteOrder, getOrderById, etc.) go here
}
