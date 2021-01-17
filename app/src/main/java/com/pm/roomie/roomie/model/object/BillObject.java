package com.pm.roomie.roomie.model.object;

import lombok.Data;
@Data

public class BillObject {

    private int id;
    private String billType;
    private String amount;
    private int flat;
    private String billDate;
    private String comment;

}
