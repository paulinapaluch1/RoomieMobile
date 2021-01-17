package com.pm.roomie.roomie;


import com.pm.roomie.roomie.model.Bill;
import com.pm.roomie.roomie.model.User;

public class CurrentLoggedUser {

    private static User user;
    private static Bill bill;

    public static void setUser(User _user){
        user = _user;
    }

    public static User getUser() {
        return user;
    }

    public static void setBill(Bill _bill){
        bill = _bill;
    }

    public static Bill getBill() {
        return bill;
    }

}