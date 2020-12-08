package com.pm.roomie.roomie.remote;

import com.pm.roomie.roomie.model.Bill;
import com.pm.roomie.roomie.model.Product;
import com.pm.roomie.roomie.model.MembersBill;
import com.pm.roomie.roomie.model.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Path;

public interface UserService {

    @GET("login/{username}/{password}")
    Call<User> login(@Path("username") String username,
                     @Path("password") String password);

    @GET("getFlatmates/{userId}")
    Call<ArrayList<User>> getFlatmates(@Path("userId") int userId);

    @GET("getBills/{userId}")
    Call<ArrayList<Bill>> getBills(@Path("userId") int userId);

    @GET("getBillsDetails/{id}")
    Call<ArrayList<Bill>> getBillsDetails(@Path("id") int id);

//    @GET("getUserBillsDetails/{id}")
//    Call<ArrayList<Bill>> getUserBillsDetails(@Path("id") int id);

    @GET("getUserBills/{userId}")
    Call<ArrayList<MembersBill>> getUserBills(@Path("userId") int userId);

    @HTTP(method = "POST", path = "archive/{id}")
    Call<Boolean> archiveUser(@Path("id") Integer id);

    @HTTP(method = "POST", path = "saveUser/{flatId}", hasBody = true)
    Call<Boolean> save(@Body User newUser, @Path("flatId") Integer flatId);


    @HTTP(method = "POST", path = "updateUser", hasBody = true)
    Call<Boolean> update(@Body User newUser);

    @HTTP(method = "POST", path = "saveBill", hasBody = true)
    Call<String> saveBill(@Body Bill newBill);

    @GET("getUserById/{id}")
    Call<User> getUserById(@Path("id")Integer id);

    @GET("getBillById/{id}")
    Call<Bill> getBillById(@Path("id")Integer id);
    @GET("getProducts/{userId}")
    Call<ArrayList<Product>> getProducts(@Path("userId") int id);


//
//    @HTTP(method = "POST", path = "send/{instructor}", hasBody = true)
//    Call<Message> sendCoordinates(@Body ArrayList<LocationToSend> coordinates,
//                                  @Path("instructor") Integer currentLoggedInstructorId);
//    @GET("timetable/{id}")
//    Call<ArrayList<TimetableJson>> getTodayTimetable(@Path("id") Integer instructorId);
}
