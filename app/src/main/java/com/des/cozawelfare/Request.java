package com.des.cozawelfare;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class Request implements Serializable {

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

    // variables for storing our data.
    private String date, name, surname,gender, age, marital_status, phone_no, email, location, help, request_status,imageUrl;

    public Request() {
        // empty constructor
        // required for Firebase.
    }

    // Constructor for all variables.
    public Request(String date,String name,String surname,String gender,String age,String marital_status,String phone_no,String email,String location,String help,String request_status,String imageUrl) {
        this.date = date;
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.age = age;
        this.marital_status = marital_status;
        this.phone_no = phone_no;
        this.email = email;
        this.location = location;
        this.help = help;
        this.request_status = request_status;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
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
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    public String getHelp() {
        return help;
    }

    public void setHelp(String help) {
        this.help = help;
    }

    public String getRequest_status(){ return request_status;}

    public void setRequest_status(String request_status){ this.request_status = request_status;}

    public String getImageUrl(){ return imageUrl;}

    public void setImageUrl(String imageUrl) {this.imageUrl = imageUrl;}

}
