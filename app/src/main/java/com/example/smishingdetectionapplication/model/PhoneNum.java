package com.example.smishingdetectionapplication.model;

public class PhoneNum {
    public String name;
    public String phoneNum;

    public PhoneNum() { }

    public PhoneNum(String userName, String email) {
        this.name = userName;
        this.phoneNum = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    @Override
    public String toString() {
        return "PhoneNum{" +
                "name='" + name + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                '}';
    }


}
