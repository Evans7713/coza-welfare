package com.des.cozawelfare;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class Cases implements Serializable {
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
    private String name,surname,age,gender,phone_no,attendant,campus,date, marital_status, no_of_children,years_in_coza,bfc,evangelism,born_again,request,request_status,address,remarks,imageUrl;

    public Cases() {
        // empty constructor
        // required for Firebase.
    }
    // Constructor for all variables.
    public Cases(String name,String surname,String age,String gender,String phone_no,String marital_status,String no_of_children,String years_in_coza,String born_again,String bfc,String evangelism,String request,String request_status,String address,String remarks,String attendant,String campus,String date,String imageUrl) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.gender = gender;
        this.phone_no = phone_no;
        this.marital_status = marital_status;
        this.no_of_children = no_of_children;
        this.years_in_coza = years_in_coza;
        this.born_again = born_again;
        this.bfc = bfc;
        this.evangelism = evangelism;
        this.request = request;
        this.request_status = request_status;
        this.address = address;
        this.remarks = remarks;
        this.attendant = attendant;
        this.campus = campus;
        this.date = date;
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

    public String getMarital_Status() {
        return marital_status;
    }

    public void setMarital_Status(String marital_status) {
        this.marital_status = marital_status;
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

    public String getNo_of_Children() {
        return no_of_children;
    }

    public void setNo_of_Children(String no_of_children) {
        this.no_of_children = no_of_children;
    }

    public String getYears_in_COZA() {
        return years_in_coza;
    }

    public void setYears_in_COZA(String years_in_coza) {
        this.years_in_coza = years_in_coza;
    }

    public String getEvangelism() {
        return evangelism;
    }

    public void setEvangelism(String evangelism) {
        this.evangelism = evangelism;
    }

    public String getBorn_Again() {
        return born_again;
    }

    public void setBorn_Again(String born_again) {
        this.born_again = born_again;
    }

    public String getBFC() {
        return bfc;
    }

    public void setBFC(String bfc) {
        this.bfc = bfc;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getRequest_Status() {
        return request_status;
    }

    public void setRequest_Status(String request_status) {
        this.request_status = request_status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getImageUrl(){return imageUrl;}

    public void setImageUrl(String imageUrl){this.imageUrl = imageUrl;}

}


