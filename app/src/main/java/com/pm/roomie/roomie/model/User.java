package com.pm.roomie.roomie.model;


import lombok.Data;

@Data
public class User {

    private int id;
    private String name;
    private String surname;
    private boolean active;
    private String roles;
    private String login;
    private String password;
    private String phone;

}
