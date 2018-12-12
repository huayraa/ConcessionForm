package com.concessionform.rajmehta.concessionform;

public class FomSubmit {
    public String ClassSelect, DOB, PeriodSelect, PhoneNumber, SourceStation, address, age, course, name, sexMF, uid;

    public FomSubmit(){

    }

    public FomSubmit(String ClassSelect, String DOB, String PeriodSelect, String PhoneNumber, String SourceStation, String address, String age, String course, String name, String sexMF, String uid){
        this.ClassSelect = ClassSelect;
        this.DOB = DOB;
        this.PeriodSelect = PeriodSelect;
        this.PhoneNumber = PhoneNumber;
        this.SourceStation = SourceStation;
        this.address = address;
        this.age = age;
        this.course = course;
        this.name = name;
        this.sexMF = sexMF;
        this.uid = uid;
    }
}
