package com.example.autopodkat;

import java.io.Serializable;

public class User implements Serializable
{
    public int UserID;
    public String UserName;
    public String Password;
    public String Telephone;
    public String Passport;
    public User(int userID, String userName, String password, String passport, String telephone)
    {
        UserID = userID;
        UserName = userName;
        Password = password;
        Passport = passport;
        Telephone = telephone;
    }
}
