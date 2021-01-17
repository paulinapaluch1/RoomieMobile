package com.pm.roomie.roomie;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.pm.roomie.roomie.login.LoginViewModel;
import com.pm.roomie.roomie.model.object.TimetableObject;
import com.pm.roomie.roomie.remote.ApiUtils;
import com.pm.roomie.roomie.remote.UserService;

import java.util.ArrayList;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pm.roomie.roomie.CurrentLoggedUser.getUser;


public class TimetableActivity extends AppCompatActivity {

    private UserService userService;
    private LoginViewModel loginViewModel;

    ListView listView;
    String[] dates;
    String[] names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
        userService = ApiUtils.getUserService();
        Bundle extras = getIntent().getExtras();
        int currentUserId = getUser().getId();


        addShowDutyListener();
        addAddDutyListener();
        addBackListener();
        addLogoutListener();

        Call<ArrayList<TimetableObject>> call = userService.getTimetable(currentUserId);

        call.enqueue(new Callback<ArrayList<TimetableObject>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<ArrayList<TimetableObject>> call, Response<ArrayList<TimetableObject>> response) {
                if (response.isSuccessful()) {
                    ArrayList<TimetableObject> timetableList = response.body();
                    if((timetableList!=null)){
                        int size = timetableList.size();
                        ArrayList<String> datesList=(ArrayList) timetableList.stream().map(f->f.getDate()).collect(Collectors.toList());
                        dates = getDateArray(datesList);
                        ArrayList<String> namesList=(ArrayList) timetableList.stream().map(f->f.getFlatMemberName().concat(" ").concat(f.getFlatMemberSurname())).collect(Collectors.toList());
                        names = getStringArray(namesList);
                        ArrayList<Integer> idsList=(ArrayList) timetableList.stream().map(f->f.getId()).collect(Collectors.toList());
                        Integer[] ids = getIntegerArray(idsList);
                        listView = findViewById(R.id.listViewTimetable);
                        TimetableAdapter timetableAdapter = new TimetableAdapter(getApplicationContext(), names, dates, ids);
                        listView.setAdapter(timetableAdapter);
                    } else {
                        createToast("Grafik jest pusty");
                    }}else{
                    createToast("Wystąpił błąd. Spróbuj ponownie");
                }
            }

            //
            @Override
            public void onFailure(Call<ArrayList<TimetableObject>> call, Throwable t) {
                Toast.makeText(TimetableActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();
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

    public static String[] getDateArray(ArrayList<String> arr)
    {
        String str[] = new String[arr.size()];
        for (int j = 0; j < arr.size(); j++) {
            str[j] = arr.get(j);
            str[j] = str[j].substring(0,10);
        }
        return str;
    }

    public static Double[] getDoubleArray(ArrayList<Double> arr)
    {
        Double str[] = new Double[arr.size()];
        for (int j = 0; j < arr.size(); j++) {
            str[j] = arr.get(j);
        }
        return str;
    }


    public static Integer[] getIntegerArray(ArrayList<Integer> arr)
    {
        Integer str[] = new Integer[arr.size()];
        for (int j = 0; j < arr.size(); j++) {
            str[j] = arr.get(j);
        }
        return str;
    }

    private void addShowDutyListener() {
        Button showDuties = (Button) findViewById(R.id.showDuties);
        showDuties.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TimetableActivity.this, DutiesActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void addAddDutyListener() {
        Button showDuties = (Button) findViewById(R.id.addTimetable);
        showDuties.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TimetableActivity.this, AddTimetableActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void addBackListener() {
        Button backButton = (Button) findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TimetableActivity.this, StartActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void addLogoutListener() {
        Button archiveButton = (Button) findViewById(R.id.logout);
        archiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginViewModel.logout();
                Intent intent = new Intent(TimetableActivity.this, MainActivity.class);
                Toast.makeText(getApplicationContext(), "Wylogowano", Toast.LENGTH_LONG).show();
                startActivity(intent);
                finish();
            }
        });
    }
}