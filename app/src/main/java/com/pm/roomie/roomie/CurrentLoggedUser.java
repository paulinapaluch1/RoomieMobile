package com.pm.roomie.roomie;


import com.pm.roomie.roomie.model.User;

public class CurrentLoggedUser {

    private static User user;

    public static void setUser(User _user){
        user = _user;
    }

    public static User getUser() {
        return user;
    }

}