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
import androidx.lifecycle.ViewModelProviders;

import com.pm.roomie.roomie.login.LoginViewModel;
import com.pm.roomie.roomie.login.LoginViewModelFactory;
import com.pm.roomie.roomie.model.Product;
import com.pm.roomie.roomie.model.User;
import com.pm.roomie.roomie.remote.ApiUtils;
import com.pm.roomie.roomie.remote.UserService;

import java.util.ArrayList;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pm.roomie.roomie.CurrentLoggedUser.getUser;

public class QueueActivity extends AppCompatActivity {

    private UserService userService;
    private LoginViewModel loginViewModel;

    ListView listView;
    String[] names={"Janusz","Kaja"};
    String[] products = {"Płyn do naczyń","Domestos","Papier toaletowy"};
    Integer [] ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);
        userService = ApiUtils.getUserService();
        int currentUserId = getUser().getId();

        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory()).get(LoginViewModel.class);

        showProducts();
        addLogoutListener();
        addAddListener();
        addBackListener();
    }

    private void showProducts() {
        Call<ArrayList<Product>> call = userService.getProducts(getUser().getId());

        call.enqueue(new Callback<ArrayList<Product>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
                if (response.isSuccessful()) {
                    ArrayList<Product> productList = response.body();
                    if((productList!=null)){
                        ArrayList<String> productsList = (ArrayList) productList.stream().map(f->f.getName()).collect(Collectors.toList());
                        products = getStringArray(productsList);
                        ArrayList<Integer> idsList = (ArrayList) productList.stream().map(f->f.getId()).collect(Collectors.toList());
                        ids = getIntegerArray(idsList);
                        getFlatmates(getUser().getId());
                        listView = findViewById(R.id.listView);
                        QueueAdapter flatmateAdapter = new QueueAdapter(getApplicationContext(), names, products, ids);
                        listView.setAdapter(flatmateAdapter);
                    } else {
                        createToast("Lista produktów jest pusta");
                    }}else{
                    createToast("Wystąpił błąd. Spróbuj ponownie");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Product>> call, Throwable t) {
                createToast("Wystąpił błąd. Spróbuj ponownie");

            }
        });
    }

    private void addLogoutListener() {
        Button archiveButton = (Button) findViewById(R.id.logout);
        archiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginViewModel.logout();
                Intent intent = new Intent(QueueActivity.this, MainActivity.class);
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
                Intent intent = new Intent(QueueActivity.this, AddUserFormActivity.class);
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

    private void getFlatmates(int currentUserId) {
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
                    } else {
                        createToast("Lista lokatorow jest pusta");
                    }}else{
                    createToast("Wystąpił błąd. Spróbuj ponownie");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {
            }
        });
    }

    private void addBackListener() {
        Button backButton = (Button) findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QueueActivity.this, StartActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}