package com.des.cozawelfare;


import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class Children implements Serializable {

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
    private String name, surname, age,gender,phone_no,card_no,attendant, campus, date, clockInState, clockOutState, imageUrl;

    public Children() {
        // empty constructor
        // required for Firebase.
    }

    // Constructor for all variables.
    public Children(String name, String surname, String age,String gender,String phone_no,String card_no,String attendant, String campus, String date, String clockInState, String clockOutState, String imageUrl) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.gender = gender;
        this.phone_no = phone_no;
        this.card_no = card_no;
        this.attendant = attendant;
        this.campus = campus;
        this.date = date;
        this.clockInState = clockInState;
        this.clockOutState = clockOutState;
        this.imageUrl = imageUrl;
    }

    // getter and setter methods for all variables.

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

    public String getAge() {
        return age;
    }
    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone_no() {
        return phone_no;
    }
    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getCard_no() {
        return card_no;
    }
    public void setCard_no(String card_no) {
        this.card_no = card_no;
    }

    public String getAttendant() {
        return attendant;
    }
    public void setAttendant(String attendant) {
        this.attendant = attendant;
    }

    public String getCampus() {
        return campus;
    }
    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getClockInState() {
        return clockInState;
    }
    public void setClockInState(String clockInState) {
        this.clockInState = clockInState;
    }

    public String getClockOutState() {
        return clockOutState;
    }
    public void setClockOutState(String clockOutState) {
        this.clockOutState = clockOutState;
    }

    public String getImageUrl(){return imageUrl;}
    public void setImageUrl(String imageUrl){this.imageUrl = imageUrl;}

}
