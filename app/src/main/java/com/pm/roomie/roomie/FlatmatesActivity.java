package com.pm.roomie.roomie;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.pm.roomie.roomie.model.User;
import com.pm.roomie.roomie.remote.ApiUtils;
import com.pm.roomie.roomie.remote.UserService;

import java.util.ArrayList;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FlatmatesActivity extends AppCompatActivity {

    private UserService userService;

    ListView listView;
    String[] names;
    String[] debits={"70","50","10","0","0","40","20"};
    String[] phones={"12345678","5358706345"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flatmates);
        userService = ApiUtils.getUserService();
        Bundle extras = getIntent().getExtras();
        int currentUserId=0;
        if (extras != null) {
            currentUserId= (int)extras.get("userId");
        }

        Call<ArrayList<User>> call = userService.getFlatmates(currentUserId);

        call.enqueue(new Callback<ArrayList<User>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                if (response.isSuccessful()) {
                    ArrayList<User> flatmatesList = response.body();
                    if((flatmatesList!=null)){
                        createToast("ok");
                        int size = flatmatesList.size();
                        ArrayList<String> namesList=(ArrayList) flatmatesList.stream().map(f->f.getName().concat(" ").concat(f.getSurname())).collect(Collectors.toList());
                        names = getStringArray(namesList);
                        ArrayList<String> phonesList=(ArrayList) flatmatesList.stream().map(f->f.getPhone()).collect(Collectors.toList());
                        phones = getStringArray(phonesList);

                        listView = findViewById(R.id.listView);
                        FlatmateAdapter flatmateAdapter = new FlatmateAdapter(getApplicationContext(),names,debits,phones);
                        listView.setAdapter(flatmateAdapter);
                    } else {
                        createToast("Lista lokatorow jest pusta");
                    }}else{
                    createToast("Wystąpił błąd. Spróbuj ponownie");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                Toast.makeText(FlatmatesActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();

            }

        });


    }

    private void createToast(String toastText) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.custom_toast_container));
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(toastText);
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public static String[] getStringArray(ArrayList<String> arr)
    {
        String str[] = new String[arr.size()];
        for (int j = 0; j < arr.size(); j++) {
            str[j] = arr.get(j);
        }
        return str;
    }
}