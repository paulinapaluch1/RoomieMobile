package com.pm.roomie.roomie.model;

import lombok.Data;

@Data
public class Timetable {

    private int id;

    private FlatMember flatMember;

    private String date;

}