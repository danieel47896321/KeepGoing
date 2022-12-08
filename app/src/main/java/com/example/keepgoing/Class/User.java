package com.example.keepgoing.Class;

import java.io.Serializable;

public class User implements Serializable {
    private String Uid = "Uid";
    private String Email = "test@email.com";
    private String FirstName = "FirstName";
    private String LastName = "LastName";
    public User() {}
    public User(String email, String firstName, String lastName) {
        Email = email;
        FirstName = firstName;
        LastName = lastName;
    }
    public String getUid() {
        return Uid;
    }
    public void setUid(String uid) {
        Uid = uid;
    }
    public String getEmail() {
        return Email;
    }
    public void setEmail(String email) {
        Email = email;
    }
    public String getFirstName() {
        return FirstName;
    }
    public void setFirstName(String firstName) {
        FirstName = firstName;
    }
    public String getLastName() {
        return LastName;
    }
    public void setLastName(String lastName) {
        LastName = lastName;
    }
}
