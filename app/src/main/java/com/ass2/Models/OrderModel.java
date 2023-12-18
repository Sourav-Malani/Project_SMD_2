package com.ass2.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class OrderModel {
    private int orderId;
    private String customerName; // or customerId

    private String customerAddress, customerPhone, customerEmail;
    private ArrayList<CartModel> orderedItems;
    private String totalPrice;
    private String orderStatus;
    private String orderDate;



    // Constructor, getters, and setters

    public OrderModel(int orderId, String customerName, ArrayList<CartModel> orderedItems,
                      String totalPrice, String orderStatus, String orderDate) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.orderedItems = orderedItems;
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
    }

    public int getItemViewType(CartModel model){
        return model.getViewType();
    }

    // Method to serialize CartModel list to JSON
    public String getOrderedItemsJson() {
        JSONArray jsonArray = new JSONArray();
        for (CartModel item : orderedItems) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("itemName", item.getItemName());
                jsonObject.put("itemPrice", item.getItemPrice());
                jsonObject.put("itemDescription", item.getItemDescription());
                jsonObject.put("itemImage", item.getItemImage());
                jsonObject.put("itemCount", item.getItemCount());
                // Add other fields as necessary
                // Example for toppings
                jsonObject.put("selectedToppingsLeft", new JSONArray(item.getSelectedToppingsLeft()));
                jsonObject.put("selectedToppingsRight", new JSONArray(item.getSelectedToppingsRight()));

                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonArray.toString();
    }

    // Method to deserialize JSON to CartModel list
    public void setOrderedItemsFromJson(String json) {
        ArrayList<CartModel> items = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                CartModel item = new CartModel();
                item.setItemName(jsonObject.getString("itemName"));
                item.setItemPrice(jsonObject.getString("itemPrice"));
                item.setItemDescription(jsonObject.optString("itemDescription"));
                item.setItemImage(jsonObject.getInt("itemImage"));
                item.setItemCount(jsonObject.getInt("itemCount"));
                // Deserialize other fields as necessary
                // Example for toppings
                item.setSelectedToppingsLeft(convertJsonArrayToStringList(jsonObject.getJSONArray("selectedToppingsLeft")));
                item.setSelectedToppingsRight(convertJsonArrayToStringList(jsonObject.getJSONArray("selectedToppingsRight")));

                items.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.orderedItems = items;
    }

    private ArrayList<String> convertJsonArrayToStringList(JSONArray jsonArray) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                list.add(jsonArray.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    // Default constructor
    public OrderModel() {
    }

    // Getters and setters for each field
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public String getCustomerEmail(){
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }
    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public ArrayList<CartModel> getOrderedItems() {
        return orderedItems;
    }

    public void setOrderedItems(ArrayList<CartModel> orderedItems) {
        this.orderedItems = orderedItems;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    // Additional methods as needed
}
