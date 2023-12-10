package com.ass2.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ass2.Models.CartModel;

import java.util.ArrayList;

public class CartDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cheesybites.db";
    private static final int DATABASE_VERSION = 1;

    // Table name
    public static final String TABLE_CART = "cart";

    // Columns
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ITEM_TYPE = "item_type";
    public static final String COLUMN_ITEM_NAME = "item_name";
    public static final String COLUMN_ITEM_IMAGE_URL = "item_image_url";
    public static final String COLUMN_ITEM_PRICE = "item_price";
    public static final String COLUMN_ITEM_DESCRIPTION = "item_description";

    public static final String COLUM_ITEM_QUANTITY = "item_quantity";
    public static final String COLUM_PIZZA_SIZE = "pizza_size";
    public static final String COLUM_PIZZA_CRUST = "pizza_crust";
    public static final String COLUM_PIZZA_TOPPINGS = "pizza_toppings";

    public static final String COLUMN_LEFT_SAUCE = "left_sauce";
    public static final String COLUMN_RIGHT_SAUCE = "right_sauce";
    public static final String COLUMN_TOPPINGS_LEFT = "left_toppings";
    public static final String COLUMN_TOPPINGS_RIGHT = "right_toppings";
    public static final String COLUMN_DATE = "date"; // New column for the date
    // Add other columns based on your discussed structure

    public CartDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create cart table
        String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_CART + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_ITEM_TYPE + " INTEGER,"
                + COLUMN_ITEM_NAME + " TEXT,"
                + COLUM_ITEM_QUANTITY + " INTEGER,"
                + COLUM_PIZZA_SIZE + " FLOAT,"
                + COLUM_PIZZA_CRUST + " TEXT,"
                + COLUM_PIZZA_TOPPINGS + " TEXT,"
                + COLUMN_ITEM_IMAGE_URL + " INTEGER,"
                + COLUMN_ITEM_PRICE + " TEXT,"
                + COLUMN_ITEM_DESCRIPTION + " TEXT,"
                + COLUMN_LEFT_SAUCE + " TEXT,"
                + COLUMN_RIGHT_SAUCE + " TEXT,"
                + COLUMN_TOPPINGS_LEFT + " TEXT,"
                + COLUMN_TOPPINGS_RIGHT + " TEXT,"
                + COLUMN_DATE + " TEXT"
                + ")";
        db.execSQL(CREATE_CART_TABLE);
    }


    public ArrayList<CartModel> getAllCartItems() {
        ArrayList<CartModel> cartItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_CART,
                null,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null) {
            int columnIndexId = cursor.getColumnIndex(COLUMN_ID);
            int columnIndexItemType = cursor.getColumnIndex(COLUMN_ITEM_TYPE);
            int columnIndexItemName = cursor.getColumnIndex(COLUMN_ITEM_NAME);
            int columnIndexItemQuantity = cursor.getColumnIndex(COLUM_ITEM_QUANTITY);
            int columnIndexItemImageUrl = cursor.getColumnIndex(COLUMN_ITEM_IMAGE_URL);
            int columnIndexItemPrice = cursor.getColumnIndex(COLUMN_ITEM_PRICE);
            int columnIndexItemDescription = cursor.getColumnIndex(COLUMN_ITEM_DESCRIPTION);
            int columnIndexItemSize = cursor.getColumnIndex(COLUM_PIZZA_SIZE);
            int columnIndexItemCrust = cursor.getColumnIndex(COLUM_PIZZA_CRUST);
            int columnIndexItemToppings = cursor.getColumnIndex(COLUM_PIZZA_TOPPINGS);
            int columnIndexLeftSauce = cursor.getColumnIndex(COLUMN_LEFT_SAUCE);
            int columnIndexRightSauce = cursor.getColumnIndex(COLUMN_RIGHT_SAUCE);
            int columnIndexToppingsLeft = cursor.getColumnIndex(COLUMN_TOPPINGS_LEFT);
            int columnIndexToppingsRight = cursor.getColumnIndex(COLUMN_TOPPINGS_RIGHT);
            int columnIndexDate = cursor.getColumnIndex(COLUMN_DATE);

            while (cursor.moveToNext()) {
                CartModel cartModel = new CartModel();

                if (columnIndexItemType != -1) {
                    cartModel.setViewType(cursor.getInt(columnIndexItemType));
                }
                if (columnIndexId != -1) {
                    cartModel.setId(cursor.getInt(columnIndexId));
                }
                if (columnIndexItemName != -1) {
                    cartModel.setItemName(cursor.getString(columnIndexItemName));
                }
                if (columnIndexItemQuantity != -1) {
                    cartModel.setItemCount(cursor.getInt(columnIndexItemQuantity));
                }
                if (columnIndexItemImageUrl != -1) {
                    cartModel.setItemImage(cursor.getInt(columnIndexItemImageUrl));
                }
                if (columnIndexItemPrice != -1) {
                    cartModel.setItemPrice(cursor.getString(columnIndexItemPrice));
                }
                if (columnIndexItemDescription != -1) {
                    cartModel.setItemDescription(cursor.getString(columnIndexItemDescription));
                }
                if (columnIndexDate != -1) {
                    cartModel.setDate(cursor.getString(columnIndexDate));
                }
                if (columnIndexItemSize != -1) {
                    cartModel.setItemSize(cursor.getFloat(columnIndexItemSize));
                }

                int viewType = cartModel.getViewType();
                switch (viewType) {
                    case 0: // Create Your Own Pizza
                        if (columnIndexLeftSauce != -1) {
                            cartModel.setSelectedSauceLeft(cursor.getString(columnIndexLeftSauce));
                        }
                        if (columnIndexRightSauce != -1) {
                            cartModel.setSelectedSauceRight(cursor.getString(columnIndexRightSauce));
                        }
                        if (columnIndexToppingsLeft != -1) {
                            cartModel.setSelectedToppingsLeft(convertStringToList(cursor.getString(columnIndexToppingsLeft)));
                        }
                        if (columnIndexToppingsRight != -1) {
                            cartModel.setSelectedToppingsRight(convertStringToList(cursor.getString(columnIndexToppingsRight)));
                        }
                        if (columnIndexItemCrust != -1) {
                            cartModel.setItemCrust(cursor.getString(columnIndexItemCrust));
                        }
                        if (columnIndexItemToppings != -1) {
                            cartModel.setItemToppings(cursor.getString(columnIndexItemToppings));
                        }

                        // Add other specific fields for Create Your Own Pizza
                        break;
                    case 1: // Simple Pizza
                        // Add specific fields for Simple Pizza
                        break;
                    case 2: // Other Items
                        // Add specific fields for Other Items
                        break;
                    default:
                        // Handle default case or error
                        break;
                }

                cartItems.add(cartModel);
            }
            cursor.close();
        }

        db.close(); // Close the database connection
        return cartItems;
    }

    public double calculateSubtotal() {
        SQLiteDatabase db = this.getReadableDatabase();
        double subtotal = 0.0;

        Cursor cursor = db.query(
                TABLE_CART,
                new String[]{COLUMN_ITEM_PRICE, COLUM_ITEM_QUANTITY},
                null, null, null, null, null
        );

        if (cursor != null) {
            int columnIndexItemPrice = cursor.getColumnIndex(COLUMN_ITEM_PRICE);
            int columnIndexItemQuantity = cursor.getColumnIndex(COLUM_ITEM_QUANTITY);

            while (cursor.moveToNext()) {
                String unitPriceString = cursor.getString(columnIndexItemPrice);
                double unitPrice = parseUnitPrice(unitPriceString);

                int quantity = cursor.getInt(columnIndexItemQuantity);
                subtotal += unitPrice * quantity;

                Log.d("SubtotalDebug", "Unit Price: " + unitPrice + ", Quantity: " + quantity + ", Subtotal: " + subtotal);
            }
            cursor.close();
        }

        db.close();
        return subtotal;
    }
    private double parseUnitPrice(String unitPriceString) {
        // Remove the currency symbol and any non-numeric characters
        String cleanedPriceString = unitPriceString.replaceAll("[^0-9.]", "");

        // Parse the cleaned string as a double
        try {
            return Double.parseDouble(cleanedPriceString);
        } catch (NumberFormatException e) {
            // Handle any parsing errors here
            e.printStackTrace();
            return 0.0; // Return a default value or handle the error as needed
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrades
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        onCreate(db);
    }
    // Method to delete a specific cart item from the database
    public void deleteCartItem(CartModel cartItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, COLUMN_ID + " = ?",
                new String[]{String.valueOf(cartItem.getId())});
        db.close();
    }
    // Method to update cart item details in the database
    public boolean updateCartItem(CartModel cartItem) {
        Log.d("CartAdapterDebug", "Updating item count in DB. Item: " + cartItem.getItemName());

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Update the fields based on your CartModel structure
        values.put(COLUMN_ITEM_NAME, cartItem.getItemName());
        values.put(COLUMN_ITEM_TYPE, cartItem.getViewType());
        values.put(COLUM_ITEM_QUANTITY, cartItem.getItemCount());
        values.put(COLUMN_ITEM_IMAGE_URL, cartItem.getItemImage());
        // Store the unit price, not the total price
        double unitPrice = Double.parseDouble(cartItem.getItemPrice()) / cartItem.getItemCount();
        values.put(COLUMN_ITEM_PRICE, String.valueOf(unitPrice));
        values.put(COLUMN_ITEM_DESCRIPTION, cartItem.getItemDescription());
        values.put(COLUMN_DATE, cartItem.getDateTime()); // Assuming getDate() returns the date

        // Handle attributes based on item type
        switch (cartItem.getViewType()) {
            case 0: // Create Your Own Pizza
                values.put(COLUM_PIZZA_SIZE, cartItem.getItemSize());
                values.put(COLUM_PIZZA_CRUST, cartItem.getItemCrust());
                values.put(COLUM_PIZZA_TOPPINGS, cartItem.getItemToppings());
                values.put(COLUMN_LEFT_SAUCE, cartItem.getSelectedSauceLeft());
                values.put(COLUMN_RIGHT_SAUCE, cartItem.getSelectedSauceRight());
                values.put(COLUMN_TOPPINGS_LEFT, convertListToString(cartItem.getSelectedToppingsLeft()));
                values.put(COLUMN_TOPPINGS_RIGHT, convertListToString(cartItem.getSelectedToppingsRight()));
                // Add other specific fields for Create Your Own Pizza
                break;
            case 1: // Simple Pizza
                // Add specific fields for Simple Pizza
                break;
            case 2: // Other Items
                // Add specific fields for Other Items
                break;
            default:
                // Handle default case or error
                break;
        }

        // ... add other fields as needed

        // Update the database for a specific item ID
        int rowsAffected = db.update(TABLE_CART, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(cartItem.getId())});
        Log.d("CartAdapterDebug", "Updating item count in DB. Item: " + cartItem.getItemName());

        db.close();
        return rowsAffected > 0;
    }

    public long insertCartItem(CartModel cartItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_ITEM_NAME, cartItem.getItemName());
        values.put(COLUMN_ITEM_TYPE, cartItem.getViewType());
        values.put(COLUM_ITEM_QUANTITY, cartItem.getItemCount());
        values.put(COLUMN_ITEM_IMAGE_URL, cartItem.getItemImage());
        values.put(COLUMN_ITEM_PRICE, cartItem.getItemPrice());
        values.put(COLUMN_ITEM_DESCRIPTION, cartItem.getItemDescription());
        values.put(COLUMN_DATE, cartItem.getDateTime()); // Assuming getDate() returns the date

        // Handle attributes based on item type
        switch (cartItem.getViewType()) {
            case 0: // Create Your Own Pizza
                values.put(COLUM_PIZZA_SIZE, cartItem.getItemSize());
                values.put(COLUM_PIZZA_CRUST, cartItem.getItemCrust());
                values.put(COLUM_PIZZA_TOPPINGS, cartItem.getItemToppings());
                values.put(COLUMN_LEFT_SAUCE, cartItem.getSelectedSauceLeft());
                values.put(COLUMN_RIGHT_SAUCE, cartItem.getSelectedSauceRight());
                values.put(COLUMN_TOPPINGS_LEFT, convertListToString(cartItem.getSelectedToppingsLeft()));
                values.put(COLUMN_TOPPINGS_RIGHT, convertListToString(cartItem.getSelectedToppingsRight()));
                // Add other specific fields for Create Your Own Pizza
                break;
            case 1: // Simple Pizza
                // Add specific fields for Simple Pizza
                break;
            case 2: // Other Items
                // Add specific fields for Other Items
                break;
            default:
                // Handle default case or error
                break;
        }

        // Insert the data into the table
        long newRowId = db.insert(TABLE_CART, null, values);
        db.close(); // Close the database connection
        return newRowId; // Return the inserted row ID or -1 if insertion failed
    }
    public String convertListToString(ArrayList<String> list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String item : list) {
            stringBuilder.append(item).append(", "); // Separating items by a comma and space
        }
        return stringBuilder.toString();
    }

    public ArrayList<String> convertStringToList(String string) {
        ArrayList<String> list = new ArrayList<>();
        String[] items = string.split(", ");
        for (String item : items) {
            list.add(item);
        }
        return list;
    }

    public int getItemCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        int totalCount = 0;

        Cursor cursor = db.query(
                TABLE_CART,
                new String[]{COLUM_ITEM_QUANTITY}, // Select only the quantity column
                null, null, null, null, null
        );

        if (cursor != null) {
            int quantityIndex = cursor.getColumnIndex(COLUM_ITEM_QUANTITY);
            if (quantityIndex != -1) { // Check if the column exists
                while (cursor.moveToNext()) {
                    int quantity = cursor.getInt(quantityIndex);
                    totalCount += quantity;
                }
            }
            cursor.close();
        }

        db.close();
        return totalCount;
    }
    public void clearCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, null, null);
        db.close();

    }


}
