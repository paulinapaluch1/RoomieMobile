package com.pm.roomie.roomie.activities.queue;

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

import com.pm.roomie.roomie.MainActivity;
import com.pm.roomie.roomie.R;
import com.pm.roomie.roomie.login.LoginViewModel;
import com.pm.roomie.roomie.login.LoginViewModelFactory;
import com.pm.roomie.roomie.model.Product;
import com.pm.roomie.roomie.remote.ApiUtils;
import com.pm.roomie.roomie.remote.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pm.roomie.roomie.CurrentLoggedUser.getUser;

public class AddProductActivity extends AppCompatActivity {

    private UserService userService;
    private LoginViewModel loginViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        addCancelListener();
        addSaveListener();
        userService = ApiUtils.getUserService();
        createNewProduct();
        addBackListener();
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory()).get(LoginViewModel.class);
        addLogoutListener();

    }

    private Product createNewProduct() {
        final EditText name = findViewById(R.id.name);
        Product product = new Product();
        product.setName(name.getText().toString());
        return product;
    }

    private void addCancelListener() {
        Button cancelButton = (Button) findViewById(R.id.cancel);
        cancelButton.setOnClickListener(v -> {
            Intent intent = new Intent(AddProductActivity.this, QueueActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void addSaveListener() {
        Button saveButton = (Button) findViewById(R.id.save);

        saveButton.setOnClickListener(v -> {

            Call<Boolean> call = userService.saveNewProduct(createNewProduct(),getUser().getId() );

            call.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.isSuccessful()) {
                        Boolean resObj = response.body();
                        if(!(resObj == null)){
                            createToast("Zapisano");
                            finish();
                            Intent intent = new Intent(AddProductActivity.this, QueueActivity.class);
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

    private void addBackListener() {
        Button backButton = (Button) findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddProductActivity.this, QueueActivity.class);
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
                Intent intent = new Intent(AddProductActivity.this, MainActivity.class);
                Toast.makeText(getApplicationContext(), "Wylogowano", Toast.LENGTH_LONG).show();
                startActivity(intent);
                finish();
            }
        });
    }
}