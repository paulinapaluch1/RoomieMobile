package com.pm.roomie.roomie.model.object;

import lombok.Data;

@Data
public class FlatMemberObject {

    private int id;
    private String flatMemberName;
    private String flatMemberSurname;
    private int idFlat;
    private int idUser;

}
