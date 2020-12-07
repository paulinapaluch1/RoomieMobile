package com.pm.roomie.roomie.model;

import java.util.List;

import lombok.Data;

@Data
public class Bill {

    private int id;
    private double amount;
    private String billDate;
    private String comment;
    private BillType billType;
    private Flat flat;
    List<MembersBill> membersBillList;
}
