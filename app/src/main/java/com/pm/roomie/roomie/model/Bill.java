package com.pm.roomie.roomie.model;

import java.util.Date;

import lombok.Data;

@Data
public class Bill {

    private int id;
    private double amount;
    private Date date;
    private String comment;
    private BillType billType;
    private Flat flat;

}
