package com.example.sumonburger;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sumonburgershop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    EditText editTextUsername, editTextEmail, editTextPassword, editTextConfirmPassword;
    Button buttonRegister, buttonGoToLogin;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonGoToLogin = findViewById(R.id.buttonGoToLogin);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String confirmPassword = editTextConfirmPassword.getText().toString().trim();

                if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Fields are empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!email.matches("^[a-zA-Z0-9._%+-]+@gmail\\.com$")) {
                    editTextEmail.setError("Enter a valid Email address");
                    return;
                }

                if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$")) {
                    editTextPassword.setError("Password must include capital and small letters, numbers, and be at least 8 characters");
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    editTextConfirmPassword.setError("Passwords do not match");
                    return;
                }

                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                if (user != null) {
                                    user.sendEmailVerification()
                                            .addOnCompleteListener(verificationTask -> {
                                                if (verificationTask.isSuccessful()) {
                                                    Toast.makeText(getApplicationContext(), "Registration successful. Verify your email.", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                                    startActivity(intent);
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Failed to send verification email", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Registration Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        buttonGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
