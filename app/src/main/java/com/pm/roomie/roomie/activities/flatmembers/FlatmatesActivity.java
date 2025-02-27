package com.pm.roomie.roomie.activities.flatmembers;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.pm.roomie.roomie.AddUserFormActivity;
import com.pm.roomie.roomie.MainActivity;
import com.pm.roomie.roomie.R;
import com.pm.roomie.roomie.ShowBillsDetailsUserActivity;
import com.pm.roomie.roomie.StartActivity;
import com.pm.roomie.roomie.login.LoginViewModel;
import com.pm.roomie.roomie.login.LoginViewModelFactory;
import com.pm.roomie.roomie.model.User;
import com.pm.roomie.roomie.remote.ApiUtils;
import com.pm.roomie.roomie.remote.UserService;

import java.util.ArrayList;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pm.roomie.roomie.CurrentLoggedUser.getUser;
import static com.pm.roomie.roomie.login.LoginDataSource.getDebits;


public class FlatmatesActivity extends AppCompatActivity {

    private UserService userService;
    private LoginViewModel loginViewModel;

    ListView listView;
    String[] names;
    String[] debits;
    String[] phones;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flatmates);
        userService = ApiUtils.getUserService();
        int currentUserId = getUser().getId();

        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory()).get(LoginViewModel.class);

        showFlatmates(currentUserId);
        addLogoutListener();
        addAddListener();
        addBackListener();
    }

    private void showFlatmates(int currentUserId) {
        Call<ArrayList<User>> call = userService.getFlatmates(currentUserId);

        call.enqueue(new Callback<ArrayList<User>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                if (response.isSuccessful()) {
                    ArrayList<User> flatmatesList = response.body();
                    if((flatmatesList!=null)){
                        ArrayList<String> namesList=(ArrayList) flatmatesList.stream().map(f->f.getName().concat(" ").concat(f.getSurname())).collect(Collectors.toList());
                        names = getStringArray(namesList);
                        ArrayList<String> phonesList=(ArrayList) flatmatesList.stream().map(f->f.getPhone()).collect(Collectors.toList());
                        phones = getStringArray(phonesList);
                        ArrayList<Integer> idsList=(ArrayList) flatmatesList.stream().map(f->f.getId()).collect(Collectors.toList());
                        Integer[] ids = getIntegerArray(idsList);
                        debits = getDebits();
                        listView = findViewById(R.id.listView);
                        FlatmateAdapter flatmateAdapter = new FlatmateAdapter(getApplicationContext(),names,debits,phones, ids);
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

    private void addLogoutListener() {
        Button archiveButton = (Button) findViewById(R.id.logout);
        archiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginViewModel.logout();
                Intent intent = new Intent(FlatmatesActivity.this, MainActivity.class);
                Toast.makeText(getApplicationContext(), "Wylogowano", Toast.LENGTH_LONG).show();
                startActivity(intent);
                finish();
            }
        });
    }

    private void addAddListener() {
        Button addButton = (Button) findViewById(R.id.add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FlatmatesActivity.this, AddUserFormActivity.class);

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
                Intent intent = new Intent(FlatmatesActivity.this, StartActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public static class MembersBillAdapter extends ArrayAdapter<String> {

        private UserService userService;
        private Context context;
        private Integer[] ids;
        private String[] names;
        private String[] dates;
        Double[] dividedamounts;
        boolean[] paid;

        public MembersBillAdapter(@NonNull Context context, String[] names, String[] dates, Double[] dividedamounts, Integer[] ids ) {
            super(context, R.layout.row_bills_user,names);
            this.context = context;
            this.names = names;
            this.dates = dates;
            this.dividedamounts = dividedamounts;
            this.ids = ids;
            this.userService = ApiUtils.getUserService();
        }
        //
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row_bills_user, parent, false);
            TextView name = row.findViewById(R.id.name);
            TextView date = row.findViewById(R.id.date);
            TextView amount = row.findViewById(R.id.amount);
            CheckBox pay = row.findViewById(R.id.checkbox_bill);

            name.setText(names[position]);
            date.setText("Data: " + dates[position]);
            amount.setText("Kwota: " + dividedamounts[position]+" zł");

            addDetailsListener(ids[position], row);
            addPaidListener(ids[position], row);

            return row;
        }

        private void addDetailsListener(Integer id, View row) {

            Button detailsButton = (Button) row.findViewById(R.id.details);
            detailsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ShowBillsDetailsUserActivity.class);
                    intent.putExtra("id", id);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }

        private void addPaidListener(Integer id, View row) {

            CheckBox paidButton = (CheckBox) row.findViewById(R.id.checkbox_bill);
            paidButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (paidButton.isChecked()) {
                        System.out.println("Zaznaczone");
                    } else {
                        System.out.println("Nie zaznaczone");
                    }
                }
            });
        }

        private void createToast(String toastText, LayoutInflater inflater, View row, Context comtext) {
            View layout = inflater.inflate(R.layout.custom_toast,
                    (ViewGroup) row.findViewById(R.id.custom_toast_container));
            TextView text = (TextView) layout.findViewById(R.id.text);
            text.setText(toastText);
            Toast toast = new Toast(context.getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
        }
    }
}