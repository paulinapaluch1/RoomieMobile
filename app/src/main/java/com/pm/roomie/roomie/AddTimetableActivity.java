package com.pm.roomie.roomie;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.pm.roomie.roomie.login.LoginViewModel;
import com.pm.roomie.roomie.model.FlatMember;
import com.pm.roomie.roomie.model.Timetable;
import com.pm.roomie.roomie.model.User;
import com.pm.roomie.roomie.model.object.FlatMemberObject;
import com.pm.roomie.roomie.model.object.TimetableObject;
import com.pm.roomie.roomie.remote.ApiUtils;
import com.pm.roomie.roomie.remote.UserService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pm.roomie.roomie.CurrentLoggedUser.getUser;

public class AddTimetableActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private UserService userService;
    private LoginViewModel loginViewModel;
    String[] items = {"Kaja N", "Krzysztof B"};
    String name;
    int idFlatMember;
    DatePicker picker;
    TextView tvw;
    Button btnGet;
    String[] names;
    Integer[] ids;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_timetable);

        userService = ApiUtils.getUserService();
        Bundle extras = getIntent().getExtras();
        int currentUserId = getUser().getId();

        addSaveListener();
        addCancelListener();

        Spinner dropdown = (Spinner) findViewById(R.id.nameAddTimetable);
        dropdown.setOnItemSelectedListener(this);

        Call<ArrayList<FlatMemberObject>> call = userService.getFlatmembers(currentUserId);
        call.enqueue(new Callback<ArrayList<FlatMemberObject>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<ArrayList<FlatMemberObject>> call, Response<ArrayList<FlatMemberObject>> response) {
                if (response.isSuccessful()) {
                    ArrayList<FlatMemberObject> flatmatesList = response.body();
                    if((flatmatesList!=null)){

                        ArrayList<String> namesList=(ArrayList) flatmatesList.stream().map(f->f.getFlatMemberName().concat(" ").concat(f.getFlatMemberSurname())).collect(Collectors.toList());
                        items = getStringArray(namesList);
                        ArrayList<Integer> idsList=(ArrayList) flatmatesList.stream().map(f->f.getId()).collect(Collectors.toList());
                        ids = getIntegerArray(idsList);

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, items);
                        dropdown.setAdapter(adapter);

                    } else {
                        createToast("Brak współlokatorów");
                    }}else{
                    createToast("Wystąpił błąd. Spróbuj ponownie");
                }
            }

            //
            @Override
            public void onFailure(Call<ArrayList<FlatMemberObject>> call, Throwable t) {
                Toast.makeText(AddTimetableActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
//        Toast.makeText(getApplicationContext(), items[position], Toast.LENGTH_LONG).show();
        name=String.valueOf(arg0.getItemAtPosition(position));
        System.out.println(name);
        idFlatMember = ids[position];
        System.out.println(idFlatMember);
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

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

    private Timetable createNewTimetable() {

        final DatePicker picker = findViewById(R.id.datePicker);
        Timetable timetable = new Timetable();
        FlatMember flatMember = new FlatMember();
        final String date = picker.getYear()+"-"+ (picker.getMonth() + 1)+"-"+picker.getDayOfMonth();

        timetable.setDate(date);

        return timetable;
    }

    private void addCancelListener() {
        Button cancelButton = (Button) findViewById(R.id.cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddTimetableActivity.this, TimetableActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void addSaveListener() {
        Button saveButton = (Button) findViewById(R.id.save);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Call<Boolean> call = userService.saveTimetable(createNewTimetable(), idFlatMember);

                call.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (response.isSuccessful()) {
                            Boolean resObj = response.body();
                            if(!(resObj == null)){
                                createToast("Zapisano");
                                finish();
                                Intent intent = new Intent(AddTimetableActivity.this, TimetableActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                createToast("Wystąpił błąd");
                            }}else{
                            createToast("Wystąpił błąd. Spróbuj ponownie");
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        createToast("Wystąpił błąd. Spróbuj ponownie");

                    }
                });

            }
        });

    }

    public static String[] getStringArray(ArrayList<String> arr)
    {
        String str[] = new String[arr.size()];
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
}
