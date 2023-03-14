package com.des.cozawelfare;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class Members implements Serializable {
    // getter method for our id
    public String getId() {
        return id;
    }

    // setter method for our id
    public void setId(String id) {
        this.id = id;
    }

    // we are using exclude because
    // we are not saving our id
    @Exclude
    private String id;

    private String name, surname, gender, birthday, phone_no, email,marital_status, date, imageUrl;

    public Members() {
        // empty constructor
        // required for Firebase.
    }
    // Constructor for all variables.
    public Members(String date,String name,String surname,String gender,String birthday,String marital_status,String phone_no,String email,String imageUrl) {
        this.date = date;
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.birthday= birthday;
        this.marital_status = marital_status;
        this.phone_no = phone_no;
        this.email = email;
        this.imageUrl = imageUrl;

    }

    // getter methods for all variables.
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }


    public String getMarital_status() {
        return marital_status;
    }

    public void setMarital_status(String marital_status) {
        this.marital_status = marital_status;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl(){ return imageUrl;}

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
