package com.pm.roomie.roomie.login;

import java.io.IOException;

public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {

        try {
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "");
            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Wystąpił problem", e));
        }
    }

    public void logout() {

    }

    public static String[] getDebits() {
        return new String[]{"70","50","10","0","0","40","20"};
    }

}