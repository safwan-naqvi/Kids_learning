package com.example.kidsappfyp.HelperClasses;

import com.google.firebase.firestore.auth.User;

public class UserHelperClass {

    String userName,gender,email,profile;
    long score;

    public UserHelperClass(){

    }


    public UserHelperClass(String userName, String gender, String email, String profile, long score) {
        this.userName = userName;
        this.gender = gender;
        this.email = email;
        this.profile = profile;
        this.score = score;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }
}
