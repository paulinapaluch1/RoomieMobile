package com.pm.roomie.roomie.model;

import java.util.List;

import lombok.Data;

@Data
public class Flat {

    private int id;
    List<FlatMember> flatMemberList;
    List<Bill> billList;
}
