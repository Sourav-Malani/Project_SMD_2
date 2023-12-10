package com.ass2.Models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UserModel {
    String fullName, email, password,deliveryAddress,phoneNo;
    String imageURL;

    String dateRegistered;

    boolean LoggedIn;

    private long localID; // Local SQLite database ID
    private int onlineID; // Online MySQL database ID

    public UserModel(String fullName, String email, String password, String deliveryAddress, String phoneNo, String imageURL) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.deliveryAddress = deliveryAddress;
        this.phoneNo = phoneNo;
        this.imageURL = imageURL;
        this.LoggedIn = false; // default
        this.setDateRegistered(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
        // Initialize local and online IDs
        this.localID = -1; // Default value
        this.onlineID = -1; // Default value
    }

    public UserModel() {
        this.LoggedIn = false; // default
        this.setDateRegistered(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
    }

    // Getter and setter methods for local and online IDs
    public long getLocalID() {
        return localID;
    }

    public void setLocalID(long localID) {
        this.localID = localID;
    }

    public int getOnlineID() {
        return onlineID;
    }

    public void setOnlineID(int onlineID) {
        this.onlineID = onlineID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setLoggedIn(boolean loggedIn) {
        LoggedIn = loggedIn;
    }
    public boolean getLoggedIn() {
        return LoggedIn;
    }

    public void setDateRegistered(String dateRegistered) {
        this.dateRegistered = dateRegistered;
    }
    public String getDateRegistered() {
        return dateRegistered;
    }
}
