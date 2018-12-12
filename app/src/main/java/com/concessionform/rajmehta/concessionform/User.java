package com.concessionform.rajmehta.concessionform;

public class User {
    public String name, DOB, address, course, sexSelect, phoneNumber, srcStation, uid;

    public User(){

    }

    public User(String DOB, String address, String course, String name, String phoneNumber, String sexSelect, String srcStation, String uid){
        this.DOB = DOB;
        this.address = address;
        this.course = course;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.sexSelect = sexSelect;
        this.srcStation = srcStation;
        this.uid = uid;
    }

}
