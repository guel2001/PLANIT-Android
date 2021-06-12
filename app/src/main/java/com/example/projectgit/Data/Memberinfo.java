package com.example.projectgit.Data;

import android.widget.EditText;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Memberinfo {

    private String name="";
    private String phoneNumber="";
    private int currentConstellation=0; //
    private String photoUrl="";
    private String address;
    private int currentCount = 0;
    private boolean checkStar = false;
    private String email="";
    private String uid="";
    public boolean space_show;
    public boolean lockscreen;

    public Memberinfo(){}

    public Memberinfo(String name, String email, String uid,String photoUrl) {
        this.name = name;
        this.photoUrl = photoUrl;
        this.uid=uid;
        this.email=email;
    }

    public Memberinfo(String name, String phoneNumber, int currentConstellation,String photoUrl,  ArrayList<String> finished_cons, String email, String uid, boolean checkStar, int currentCount,String address,boolean space_show,boolean lockscreen) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.currentConstellation = currentConstellation;
        this.photoUrl = photoUrl;
        this.uid=uid;
        this.email=email;
        this.finished_cons = finished_cons;
        this.email = email;
        this.uid=uid;
        this.checkStar=checkStar;
        this.currentCount = currentCount;
        this.address = address;
        this.space_show = space_show;
        this.lockscreen=lockscreen;
    }

    public Memberinfo(String name, String phoneNumber, int currentConstellation,  ArrayList<String> finished_cons, String email, String uid, boolean checkStar, int currentCount,String address,boolean space_show,boolean lockscreen) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.currentConstellation = currentConstellation;
        this.uid=uid;
        this.email=email;
        this.finished_cons = finished_cons;
        this.email = email;
        this.uid=uid;
        this.checkStar=checkStar;
        this.currentCount = currentCount;
        this.address = address;
        this.space_show = space_show;
        this.lockscreen=lockscreen;
    }



    public ArrayList<String> getFinished_cons() {
        return finished_cons;
    }

    public void setFinished_cons(ArrayList<String> finished_cons) {
        this.finished_cons = finished_cons;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getCurrentConstellation() {
        return currentConstellation;
    }

    public void setCurrentConstellation(int currentConstellation) {
        this.currentConstellation = currentConstellation;
    }
    public boolean isSpace_show() {
        return space_show;
    }

    public void setSpace_show(boolean space_show) {
        this.space_show = space_show;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private ArrayList<String> finished_cons = new ArrayList<String>();

    public int getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }

    public String getUid() {
        return uid;
    }

    public Memberinfo(ArrayList<String> finished_cons) {
        this.finished_cons = finished_cons;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isCheckStar() {
        return checkStar;
    }

    public void setCheckStar(boolean checkStar) {
        this.checkStar = checkStar;
    }
}
