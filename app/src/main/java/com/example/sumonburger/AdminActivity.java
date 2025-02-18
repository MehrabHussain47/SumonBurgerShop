package com.example.sumonburger;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sumonburgershop.R;

public class AdminActivity extends AppCompatActivity {

    TextView textViewWelcome;
    Button buttonInsert, buttonUpdate, buttonDelete, buttonView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        textViewWelcome = findViewById(R.id.textViewWelcome);
        buttonInsert = findViewById(R.id.buttonInsert);
        buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonDelete = findViewById(R.id.buttonDelete);
        buttonView = findViewById(R.id.buttonView);

        textViewWelcome.setText("Welcome Admin!! What kind of operation do you want to perform?");

        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startInsertActivity();
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUpdateActivity();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDeleteActivity();
            }
        });

        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startViewActivity();
            }
        });
    }

    private void startInsertActivity() {
        Intent intent = new Intent(AdminActivity.this, InsertActivity.class);
        startActivity(intent);
    }

    private void startUpdateActivity() {
        Intent intent = new Intent(AdminActivity.this, UpdateActivity.class);
        startActivity(intent);
    }

    private void startDeleteActivity() {
        Intent intent = new Intent(AdminActivity.this, DeleteActivity.class);
        startActivity(intent);
    }
    private void startViewActivity() {
        Intent intent = new Intent(AdminActivity.this, ViewActivity.class);
        startActivity(intent);
    }

//    private void performViewOperation() {
//        Toast.makeText(this, "Performing View Operation", Toast.LENGTH_SHORT).show();
//    }
}
