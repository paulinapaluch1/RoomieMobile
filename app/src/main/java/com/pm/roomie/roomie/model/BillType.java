package com.pm.roomie.roomie.model;

import java.util.List;
import lombok.Data;

@Data
public class BillType {

    private int id;
    private String type;
    List<Bill> billList;

}
