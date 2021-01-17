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

import com.pm.roomie.roomie.model.Bill;
import com.pm.roomie.roomie.model.User;
import com.pm.roomie.roomie.remote.ApiUtils;
import com.pm.roomie.roomie.remote.UserService;

import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import static com.pm.roomie.roomie.CurrentLoggedUser.getBill;
import static com.pm.roomie.roomie.CurrentLoggedUser.getUser;


public class BillsActivity extends AppCompatActivity {

    private UserService userService;

    ListView listView;
    String[] names;
    String[] dates;
    Double[] amounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bills);
        userService = ApiUtils.getUserService();
        Bundle extras = getIntent().getExtras();
        int currentUserId = getUser().getId();

        addAddBillListener();
        addBackListener();

        Call<ArrayList<Bill>> call = userService.getBills(currentUserId);

        call.enqueue(new Callback<ArrayList<Bill>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<ArrayList<Bill>> call, Response<ArrayList<Bill>> response) {
                if (response.isSuccessful()) {
                    ArrayList<Bill> billsList = response.body();
                    if((billsList!=null)){
                        createToast("ok");
                        int size = billsList.size();
//                        ArrayList<String> namesList=(ArrayList) billsList.stream().map(f->f.getBillType().getType()).collect(Collectors.toList());
                        ArrayList<String> namesList=(ArrayList) billsList.stream().map(f->f.getComment().concat(" ")).collect(Collectors.toList());
                        names = getStringArray(namesList);
                        ArrayList<String> datesList=(ArrayList) billsList.stream().map(f->f.getBillDate()).collect(Collectors.toList());
                        dates = getDateArray(datesList);
                        ArrayList<Double> amountsList=(ArrayList) billsList.stream().map(f->f.getAmount()).collect(Collectors.toList());
                        amounts = getDoubleArray(amountsList);
                        ArrayList<Integer> idsList=(ArrayList) billsList.stream().map(f->f.getId()).collect(Collectors.toList());
                        Integer[] ids = getIntegerArray(idsList);
//
                        listView = findViewById(R.id.listViewBills);
                        BillAdapter billAdapter = new BillAdapter(getApplicationContext(),names,dates,amounts, ids);
                        listView.setAdapter(billAdapter);
                    } else {
                        createToast("Lista rachunków jest pusta");
                    }}else{
                    createToast("Wystąpił błąd. Spróbuj ponownie");
                }
            }

            //
            @Override
            public void onFailure(Call<ArrayList<Bill>> call, Throwable t) {
                Toast.makeText(BillsActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }

    private void addAddBillListener() {
        Button addButton = (Button) findViewById(R.id.addBill);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BillsActivity.this, AddBillFormActivity.class);
                startActivity(intent);
                finish();
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

    private void addBackListener() {
        Button backButton = (Button) findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BillsActivity.this, StartActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}