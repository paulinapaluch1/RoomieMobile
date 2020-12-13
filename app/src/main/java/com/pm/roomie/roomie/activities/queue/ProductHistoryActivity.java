package com.pm.roomie.roomie.activities.queue;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.pm.roomie.roomie.MainActivity;
import com.pm.roomie.roomie.R;
import com.pm.roomie.roomie.login.LoginViewModel;
import com.pm.roomie.roomie.login.LoginViewModelFactory;
import com.pm.roomie.roomie.model.dtos.ProductHistoryDto;
import com.pm.roomie.roomie.remote.ApiUtils;
import com.pm.roomie.roomie.remote.UserService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pm.roomie.roomie.CurrentLoggedUser.getUser;


public class ProductHistoryActivity extends AppCompatActivity {
    private LoginViewModel loginViewModel;
    private UserService userService;
    private TextView history;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_history);
        userService = ApiUtils.getUserService();
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory()).get(LoginViewModel.class);
        addLogoutListener();
        addBackListener();
        showHistory();
        TextView header = (TextView) findViewById(R.id.product);
        Bundle extras = getIntent().getExtras();
        header.setText((String)extras.get("name"));
    }

    private void showHistory() {
        Bundle extras = getIntent().getExtras();
        Call<ArrayList<ProductHistoryDto>> call = userService.getProductHistory((String)extras.get("name"), getUser().getId());

        call.enqueue(new Callback<ArrayList<ProductHistoryDto>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<ArrayList<ProductHistoryDto>> call, Response<ArrayList<ProductHistoryDto>> response) {
                if (response.isSuccessful()) {
                    ArrayList<ProductHistoryDto> productHistoryList = response.body();
                    if((productHistoryList!=null)){
                        history = findViewById(R.id.history);
                        history.setText(createHistory(productHistoryList));
                    } else {
                        createToast("Historia jest pusta");
                    }}else{
                    createToast("Wystąpił błąd. Spróbuj ponownie");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ProductHistoryDto>> call, Throwable t) {
                createToast("Wystąpił błąd. Spróbuj ponownie");

            }
        });
    }

    private String createHistory(ArrayList<ProductHistoryDto> productHistoryList) {
        String s = "";
        for(ProductHistoryDto dto : productHistoryList){
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date = dateFormat.format(dto.getDate());
            s = new StringBuilder(s).append(date+" ").toString();
            s = new StringBuilder(s).append(dto.getUserName()+"\n").toString();
        }
        return s;

    }


    private void addLogoutListener() {
        Button archiveButton = (Button) findViewById(R.id.logout);
        archiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginViewModel.logout();
                Intent intent = new Intent(ProductHistoryActivity.this, MainActivity.class);
                Toast.makeText(getApplicationContext(), "Wylogowano", Toast.LENGTH_LONG).show();
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
                Intent intent = new Intent(ProductHistoryActivity.this, QueueActivity.class);
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
}