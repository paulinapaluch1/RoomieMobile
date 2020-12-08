package com.pm.roomie.roomie;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.pm.roomie.roomie.login.LoginViewModel;
import com.pm.roomie.roomie.login.LoginViewModelFactory;
import com.pm.roomie.roomie.model.User;
import com.pm.roomie.roomie.remote.ApiUtils;
import com.pm.roomie.roomie.remote.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditUserActivity extends AppCompatActivity {

    private UserService userService;
    private LoginViewModel loginViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_form);
        userService = ApiUtils.getUserService();
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory()).get(LoginViewModel.class);
        final TextView header = findViewById(R.id.header);
        header.setText("Edytuj lokatora");
        Bundle extras = getIntent().getExtras();
        final EditText login = findViewById(R.id.login);
        final EditText name = findViewById(R.id.name);
        final EditText surname = findViewById(R.id.surname);
        final EditText phone = findViewById(R.id.phone);
        fillUserData(extras, login, name, surname, phone);
        addCancelListener();
        addSaveListener();
        addBackListener();
        addLogoutListener();
    }

    private void fillUserData(Bundle extras, EditText login, EditText name, EditText surname, EditText phone) {
        Call<User> call = userService.getUserById((int)extras.get("id"));

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User resObj = response.body();
                    if(!(resObj.getId()==0)){
                        login.setText(resObj.getLogin().toString());
                        name.setText(resObj.getName().toString());
                        surname.setText(resObj.getSurname().toString());
                        phone.setText(resObj.getPhone().toString());
                    } else {
                        createToast("Bład");
                    }}else{
                    createToast("Wystąpił błąd. Spróbuj ponownie");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                createToast("Wystąpił błąd. Spróbuj ponownie");

            }
        });
    }

    private void addCancelListener() {
        Button cancelButton = (Button) findViewById(R.id.cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditUserActivity.this, FlatmatesActivity.class);
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

    private void addSaveListener() {
        Button saveButton = (Button) findViewById(R.id.save);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Call<Boolean> call = userService.update(createNewUser());

                call.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (response.isSuccessful()) {
                            Boolean resObj = response.body();
                            if(!(resObj == null)){
                                createToast("Zapisano");
                                finish();
                                Intent intent = new Intent(EditUserActivity.this, FlatmatesActivity.class);
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
    private User createNewUser() {
        final EditText login = findViewById(R.id.login);
        final EditText password = findViewById(R.id.password);
        final EditText name = findViewById(R.id.name);
        final EditText surname = findViewById(R.id.surname);
        final EditText phone = findViewById(R.id.phone);
        User user = new User();
        user.setLogin(login.getText().toString());
        user.setName(name.getText().toString());
        user.setSurname(surname.getText().toString());
        user.setPassword(password.getText().toString());
        user.setPhone(phone.getText().toString());
        return user;
    }

    private void addBackListener() {
        Button backButton = (Button) findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditUserActivity.this, FlatmatesActivity.class);
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
                Intent intent = new Intent(EditUserActivity.this, MainActivity.class);
                Toast.makeText(getApplicationContext(), "Wylogowano", Toast.LENGTH_LONG).show();
                startActivity(intent);
                finish();
            }
        });
    }
}