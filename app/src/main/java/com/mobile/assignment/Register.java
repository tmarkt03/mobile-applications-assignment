package com.mobile.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Register extends AppCompatActivity {

    EditText FullName, Email, Password, phoneNumber;
    TextView LoginBtn;
    Button RegisterBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        FullName = findViewById(R.id.fullName);
        Email = findViewById(R.id.email);
        Password = findViewById(R.id.password);
        phoneNumber = findViewById(R.id.phoneNumber);
        LoginBtn = findViewById(R.id.login);
        RegisterBtn = findViewById(R.id.materialButton);
        progressBar = findViewById(R.id.progressBar);

        fAuth = FirebaseAuth.getInstance();

        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        RegisterBtn.setOnClickListener(v -> {
            String email = Email.getText().toString().trim();
            String password = Password.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                Email.setError("Email is Required");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Password.setError("Password is Required");
                return;
            }
            if (password.length() < 6) {
                Password.setError("Password Must be >= 6 Characters");
                return;
            }

            progressBar.setVisibility(View.VISIBLE);

            //Register the User in Firebase
            fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(this, "User created", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(this, "Error! " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        LoginBtn.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), Login.class));
        });
    }
}