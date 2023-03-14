package com.des.cozawelfare;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class FirstTimer implements Serializable {
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
        private String name,age,gender,phone_no,membership,address,remarks,attendant,campus,date;

        public FirstTimer() {
            // empty constructor
            // required for Firebase.
        }

        // Constructor for all variables.
        public FirstTimer(String name,String age,String gender,String phone_no,String membership,String address,String remarks,String attendant,String campus,String date) {
            this.name = name;
            this.age = age;
            this.gender = gender;
            this.phone_no = phone_no;
            this.membership = membership;
            this.address = address;
            this.remarks = remarks;
            this.attendant = attendant;
            this.campus = campus;
            this.date = date;
        }

        // getter methods for all variables.
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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
    public String getMembership() {
        return membership;
    }

    public void setMembership(String membership) {
        this.membership = membership;
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

}
