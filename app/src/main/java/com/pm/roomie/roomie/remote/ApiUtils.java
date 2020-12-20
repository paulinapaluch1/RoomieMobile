package com.pm.roomie.roomie.remote;

public class ApiUtils {

    public static final String BASE_URL="http://192.168.0.150:8080/mobile/";
    //   public static final String BASE_URL="http://192.168.1.11:7070/mobile/";
    //   public static final String BASE_URL="http://192.168.1.11:7070/mobile/";

    public static UserService getUserService(){
        return RetrofitClient.getClient(BASE_URL).create(UserService.class);
    }
}