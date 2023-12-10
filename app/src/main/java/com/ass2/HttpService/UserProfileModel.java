package com.ass2.HttpService;


public class UserProfileModel {
    private String fullName;
    private String email;
    private String password;
    private String deliveryAddress;
    private String phoneNo;
    private String imageURL;

    public UserProfileModel(String fullName, String email, String password, String deliveryAddress, String phoneNo, String imageURL) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.deliveryAddress = deliveryAddress;
        this.phoneNo = phoneNo;
        this.imageURL = imageURL;
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
}
