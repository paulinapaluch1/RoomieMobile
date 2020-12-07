package com.pm.roomie.roomie.model;
import lombok.Data;

@Data
public class MembersBill {

    private int id;

    private Bill bill;

    private double dividedAmount;

    private boolean paid;

    private FlatMember flatMember;
}
