package com.example.dell.quizapp.models;

public class Profile {
    private String name;
    private String phoneNumber;
    private String imageUri;

    public Profile() {
    }

    public Profile(String name, String phoneNumber, String imageUri) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.imageUri = imageUri;
    }

    public String getName() {
        return name;
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

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
