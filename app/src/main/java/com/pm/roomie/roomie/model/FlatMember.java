package com.pm.roomie.roomie.model;

import java.util.List;

import lombok.Data;

@Data
public class FlatMember {

    private int id;
    private User user;
    private Flat flat;
    List<MembersBill> membersBillList;
}
