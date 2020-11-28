package com.pm.roomie.roomie;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        final Button billsButton = findViewById(R.id.bills);
        Bundle extras = getIntent().getExtras();
        addNewFlatmate(extras);

        billsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, BillsActivity.class);
                intent.putExtra("userId",  (int)extras.get("id"));
                startActivity(intent);
                finish();
            }
        });
    }



    private void addNewFlatmate(Bundle extras) {
        final Button flatmatesButton = findViewById(R.id.flatMembers);

        flatmatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, FlatmatesActivity.class);
                intent.putExtra("userId",  (int)extras.get("id"));
                startActivity(intent);
                finish();

            }
        });

    }

    private void goToMainActivity() {
        Intent intent = new Intent(StartActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}