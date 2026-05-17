package com.mobile.assignment;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import java.util.Objects;

public class Register extends AppCompatActivity {

    private static final String TAG = "Register";
    private static final long AUTH_TIMEOUT_MS = 15000L;

    EditText FullName, Email, Password, phoneNumber;
    TextView LoginBtn;
    Button RegisterBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private Runnable authTimeoutRunnable;
    private boolean isAuthRequestPending;


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

        try {
            fAuth = FirebaseAuth.getInstance();
        } catch (IllegalStateException exception) {
            Log.e(TAG, "FirebaseAuth is unavailable in Register", exception);
            Toast.makeText(this, "Firebase is not configured correctly on this device.", Toast.LENGTH_LONG).show();
            RegisterBtn.setEnabled(false);
            return;
        }

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
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Email.setError("Enter a valid email address");
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
            if (!hasNetworkConnection()) {
                Toast.makeText(this, "No internet connection available on this device.", Toast.LENGTH_LONG).show();
                return;
            }

            startAuthRequest();

            //Register the User in Firebase
            fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (!finishAuthRequest()) {
                    return;
                }

                if (task.isSuccessful()) {
                    Toast.makeText(this, "User created", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                } else {
                    handleRegistrationFailure(Objects.requireNonNull(task.getException()));
                }
            });
        });

        LoginBtn.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), Login.class));
        });
    }

    private void handleRegistrationFailure(Exception exception) {
        RegisterBtn.setEnabled(true);

        String message = "Registration failed. Please try again.";

        if (exception instanceof FirebaseAuthUserCollisionException) {
            Email.setError("An account already exists for this email");
            Email.requestFocus();
            message = "An account already exists for this email.";
        } else if (exception instanceof FirebaseAuthWeakPasswordException) {
            Password.setError("Password must be at least 6 characters");
            Password.requestFocus();
            message = "Password must be at least 6 characters.";
        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
            Email.setError("Enter a valid email address");
            Email.requestFocus();
            message = "Enter a valid email address.";
        } else if (exception instanceof FirebaseNetworkException) {
            message = "Network error. Check your internet connection and try again.";
        } else if (exception instanceof FirebaseTooManyRequestsException) {
            message = "Too many attempts. Please try again later.";
        } else if (exception instanceof FirebaseAuthException) {
            message = ((FirebaseAuthException) exception).getMessage();
        }

        Log.e(TAG, "Registration failed", exception);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void startAuthRequest() {
        isAuthRequestPending = true;
        RegisterBtn.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        clearAuthTimeout();

        authTimeoutRunnable = () -> {
            if (!isAuthRequestPending || isFinishing() || isDestroyed()) {
                return;
            }

            isAuthRequestPending = false;
            progressBar.setVisibility(View.GONE);
            RegisterBtn.setEnabled(true);
            boolean hasNetworkConnection = hasNetworkConnection();
            Log.e(TAG, "Registration request timed out. Network connected: " + hasNetworkConnection);
            Toast.makeText(
                    this,
                    hasNetworkConnection
                            ? "Registration timed out while contacting Firebase. Check the device date and internet connection, then try again."
                            : "No internet connection available on this device.",
                    Toast.LENGTH_LONG
            ).show();
        };

        mainHandler.postDelayed(authTimeoutRunnable, AUTH_TIMEOUT_MS);
    }

    private boolean finishAuthRequest() {
        if (!isAuthRequestPending) {
            return false;
        }

        isAuthRequestPending = false;
        clearAuthTimeout();
        progressBar.setVisibility(View.GONE);
        return true;
    }

    private void clearAuthTimeout() {
        if (authTimeoutRunnable != null) {
            mainHandler.removeCallbacks(authTimeoutRunnable);
            authTimeoutRunnable = null;
        }
    }

    @Override
    protected void onDestroy() {
        clearAuthTimeout();
        super.onDestroy();
    }

    private boolean hasNetworkConnection() {
        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        if (connectivityManager == null) {
            return false;
        }

        Network activeNetwork = connectivityManager.getActiveNetwork();
        if (activeNetwork == null) {
            return false;
        }

        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork);
        if (networkCapabilities == null || !networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
            return false;
        }

        return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN);
    }
}