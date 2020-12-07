package com.pm.roomie.roomie;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.pm.roomie.roomie.model.Bill;
import com.pm.roomie.roomie.remote.ApiUtils;
import com.pm.roomie.roomie.remote.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditBillFormActivity extends AppCompatActivity {
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bill_form);
        addCancelListener();
        addSaveListener();
        userService = ApiUtils.getUserService();
        createNewBill();
    }

    private Bill createNewBill() {
        final EditText type = findViewById(R.id.billType);
        final EditText amount = findViewById(R.id.billAmount);
        final EditText dateBill = findViewById(R.id.billDate);
        final EditText comment = findViewById(R.id.billComment);

//        BillType billType = new BillType();
//        billType.setId(1);
//        billType.setType(type.getText().toString());

        Bill bill = new Bill();

//        bill.setBillType(billType);
//        bill.setAmount(Double.parseDouble(amount.getText().toString()));
        bill.setAmount(123.5);
        bill.setBillDate(dateBill.getText().toString());
        bill.setComment(comment.getText().toString());

        return bill;
    }

    private void addCancelListener() {
        Button cancelButton = (Button) findViewById(R.id.cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditBillFormActivity.this, BillsActivity.class);
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

                Call<String> call = userService.saveBill(createNewBill());

                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            String resObj = response.body();
                            if(!(resObj == null)){
                                createToast(resObj);
                                finish();
                                Intent intent = new Intent(EditBillFormActivity.this, BillsActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                createToast("Wystąpił błąd");
                            }}else{
                            createToast("Wystąpił błąd. Spróbuj ponownie");
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        createToast("Wystąpił błąd. Spróbuj ponownie");

                    }
                });

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