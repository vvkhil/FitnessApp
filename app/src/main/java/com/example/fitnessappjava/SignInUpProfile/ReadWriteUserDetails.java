package com.example.fitnessappjava.SignInUpProfile;

public class ReadWriteUserDetails {
    public String doB, gender, mobile;

    //Constructor
    public ReadWriteUserDetails(){}

    public ReadWriteUserDetails(String textDoB, String textGender, String textMobile) {
        this.doB = textDoB;
        this.gender = textGender;
        this.mobile = textMobile;
    }
}
