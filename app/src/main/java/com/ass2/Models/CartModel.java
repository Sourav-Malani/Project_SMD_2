package com.ass2.Models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CartModel {

    String itemName, itemPrice, itemDescription;

    int itemImage,itemID,itemCount;

    float UNITPRICE;

    int viewType;
    String date;
    String itemCrust,itemToppings;
    float itemSize;
    private String selectedSauceLeft;
    private String selectedSauceRight;
    private ArrayList<String> selectedToppingsLeft;
    private ArrayList<String> selectedToppingsRight;

    public CartModel(int itemCount, String itemName, String itemPrice,int itemImage,
                     int viewType,float itemSize,String itemCrust,String itemToppings, String selectedSauceLeft, String selectedSauceRight,
                     ArrayList<String> selectedToppingsLeft, ArrayList<String> selectedToppingsRight) {
        this.itemCount = 1; // one item is added by default
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemImage = itemImage;
        this.viewType = viewType;
        this.selectedSauceLeft = selectedSauceLeft;
        this.selectedSauceRight = selectedSauceRight;
        this.selectedToppingsLeft = selectedToppingsLeft;
        this.selectedToppingsRight = selectedToppingsRight;
        this.itemSize = itemSize;
        this.itemCrust = itemCrust;
        this.itemToppings = itemToppings;

    }

    public CartModel() {
        // Default constructor

    }

    public CartModel(int itemCount, String itemName, String itemPrice, String itemDescription, int itemImage,
                     int viewType) {
        this.itemCount = itemCount;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemDescription = itemDescription;
        this.itemImage = itemImage;
        this.viewType = viewType;
    }

//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }

    public int getViewType() {
        return viewType;
    }
    public int getId() {
        return itemID;
    }

    public void setId(int id) {
        this.itemID = id;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemImage() {
        return itemImage;
    }

    public void setItemImage(int itemImage) {
        this.itemImage = itemImage;
    }

    public String getItemPrice() {
        return itemPrice;
    }


    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getSelectedSauceLeft() {
        return selectedSauceLeft;
    }

    public void setSelectedSauceLeft(String selectedSauceLeft) {
        this.selectedSauceLeft = selectedSauceLeft;
    }

    public String getSelectedSauceRight() {
        return selectedSauceRight;
    }

    public void setSelectedSauceRight(String selectedSauceRight) {
        this.selectedSauceRight = selectedSauceRight;
    }

    public ArrayList<String> getSelectedToppingsLeft() {
        return selectedToppingsLeft;
    }

    public void setSelectedToppingsLeft(ArrayList<String> selectedToppingsLeft) {
        this.selectedToppingsLeft = selectedToppingsLeft;
    }

    public ArrayList<String> getSelectedToppingsRight() {
        return selectedToppingsRight;
    }

    public void setSelectedToppingsRight(ArrayList<String> selectedToppingsRight) {
        this.selectedToppingsRight = selectedToppingsRight;
    }

    public String getDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }


    public void setDate(String date) {
        this.date = date;
    }

    public int getItemCount() {
        return itemCount;
    }

    public float getItemSize() {
        return itemSize;
    }

    public void setItemSize(float itemSize) {
        this.itemSize = itemSize;
    }

    public String getItemToppings() {
        return itemToppings;
    }

    public void setItemToppings(String itemToppings) {
        this.itemToppings = itemToppings;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public void setItemCrust(String itemCrust) {
        this.itemCrust = itemCrust;
    }

    public String getItemCrust() {
        return itemCrust;
    }


    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }
    public float getUnitPrice(float s, String c) {
        return calculateUnitPrice(s,c);
    }

    public void setUnitPrice(float unitPrice) {
        this.UNITPRICE = unitPrice;
    }
    public float calculateUnitPrice(float size,String crust) {
        float totalPizzaPrice = 0.0f;
        if(size == 7.0f) {
            totalPizzaPrice += 5.99;
        } else if(size == 9.0f) {
            totalPizzaPrice += 7.99;
        } else if(size == 12.0f) {
            totalPizzaPrice += 9.99;
        } else if(size == 13.5f) {
            totalPizzaPrice += 15.99;
        }

        if (crust.equals("Classic Crust")) {
            totalPizzaPrice += 0.00; // no change
        } else if (crust.equals("Italian Crust")) {
            totalPizzaPrice += 1.00; // add 1$
        }
        return totalPizzaPrice;

    }


}
