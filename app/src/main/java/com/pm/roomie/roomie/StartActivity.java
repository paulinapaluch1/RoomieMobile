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
        final Button flatmatesButton = findViewById(R.id.flatMembers);
        Bundle extras = getIntent().getExtras();

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
}