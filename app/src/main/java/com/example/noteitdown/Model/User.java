package com.example.noteitdown.Model;

public class User {

    private String UID;
    private String Name;
    private String Email;
    private String Password;

    public static boolean CloudState = false;
    public static boolean initializedCloudState = false;


    public User() {
    }


    public User(String UID, String name, String email, String password) {
        this.UID = UID;
        Name = name;
        Email = email;
        Password = password;
    }

    public User(String name, String email, String password) {
        Name = name;
        Email = email;
        Password = password;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
