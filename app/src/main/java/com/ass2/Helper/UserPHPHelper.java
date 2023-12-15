package com.ass2.Helper;

import com.ass2.config.Config;

import java.net.URL;

public class UserPHPHelper {

        String fullName, email, password,deliveryAddress,phoneNo;
        String imageURL;
        String DB_URL_REGISTER = Config.API_BASE_URL + "register.php";
        String DB_URL_GET_USER_DATA = Config.API_BASE_URL + "get_user_data.php";
        String DB_URL_SET_USER_DATA = Config.API_BASE_URL + "update_user_data.php";

        //get and set user data on the mysql database


        public UserPHPHelper(String fullName, String email, String password, String deliveryAddress, String phoneNo, String imageURL) {
            this.fullName = fullName;
            this.email = email;
            this.password = password;
            this.deliveryAddress = deliveryAddress;
            this.phoneNo = phoneNo;
            this.imageURL = imageURL;
        }

        public UserPHPHelper(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public UserPHPHelper(String email) {
            this.email = email;
        }

        public UserPHPHelper() {
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

        public String getDB_URL_REGISTER() {
            return DB_URL_REGISTER;
        }

        public void setDB_URL_REGISTER(String DB_URL_REGISTER) {
            this.DB_URL_REGISTER = DB_URL_REGISTER;
        }

        public String getDB_URL_GET_USER_DATA() {
            return DB_URL_GET_USER_DATA;
        }





}
