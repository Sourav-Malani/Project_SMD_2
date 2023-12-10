package com.ass2.Models;

public class MainModel {

    int pizzaImage,FoodId;
    String Name, Price, Description, Category;
    private int viewType;
    public MainModel(int pizzaimage, String name, String price, String description, String category, int foodId,int viewType) {
        pizzaImage = pizzaimage;
        Name = name;
        Price = price;
        Description = description;
        Category = category;
        FoodId = foodId;
        this.viewType = viewType;
    }




    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDescription() {
        return Description;
    }
    public int getViewType() {
        return viewType;
    }
    public int getPizzaImage() {
        return pizzaImage;
    }

    public void setPizzaImage(int pizzaImage) {
        this.pizzaImage = pizzaImage;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public int getFoodId() {
        return FoodId;
    }

    public void setFoodId(int foodId) {
        FoodId = foodId;
    }
}
