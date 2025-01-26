package com.example.sumonburgershop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPassword;
    Button buttonLogin, buttonRegister;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonRegister = findViewById(R.id.buttonRegister);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                String email = editTextEmail.getText().toString();
//                String password = editTextPassword.getText().toString();
//
//                editTextEmail.setError(null);
//                editTextPassword.setError(null);
//
//                String emailPattern = "^[a-zA-Z0-9._%+-]+@gmail\\.com$";
//                String passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$";
//
//                if (email.isEmpty() || password.isEmpty()) {
//                    Toast.makeText(getApplicationContext(), "Fields are empty", Toast.LENGTH_SHORT).show();
//                } else if (!email.matches(emailPattern)) {
//                    editTextEmail.setError("Enter a valid Gmail address");
//                } else if (!password.matches(passwordPattern)) {
//                    editTextPassword.setError("Password must be at least 6 characters, include letters and numbers");
//                } else {
//                    if (email.equals("admin@gmail.com") && password.equals("admin")) {
//                        Intent intent = new Intent(MainActivity.this, AdminActivity.class);
//                        startActivity(intent);
//                    } else {
//                        boolean checkEmailPassword = db.checkEmailPassword(email, password);
//                        if (checkEmailPassword) {
//                            Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(MainActivity.this, ViewActivity.class);
//                            startActivity(intent);
//                        } else {
//                            Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }

                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Fields are empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    editTextEmail.setError("Enter a valid email address");
                    return;
                }

                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (email.equals("admin@gmail.com") && password.equals("admin")) {
                                Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                                startActivity(intent);
                            }
                            else{
                            if (task.isSuccessful()) {
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                if (user != null && user.isEmailVerified()) {
                                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MainActivity.this, ViewActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Please verify your email", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            }
                        });
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}