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
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import java.util.Objects;

public class Login extends AppCompatActivity {

    private static final String TAG = "Login";
    private static final long AUTH_TIMEOUT_MS = 15000L;

    EditText Email, Password;
    TextView RegisterBtn;
    Button LoginBtn;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private Runnable authTimeoutRunnable;
    private boolean isAuthRequestPending;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Email = findViewById(R.id.email);
        Password = findViewById(R.id.password);
        LoginBtn = findViewById(R.id.loginButton);
        RegisterBtn = findViewById(R.id.register);
        progressBar = findViewById(R.id.progressBar);

        try {
            fAuth = FirebaseAuth.getInstance();
        } catch (IllegalStateException exception) {
            Log.e(TAG, "FirebaseAuth is unavailable in Login", exception);
            Toast.makeText(this, "Firebase is not configured correctly on this device.", Toast.LENGTH_LONG).show();
            LoginBtn.setEnabled(false);
            return;
        }

        // Check if user is already logged in
        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        LoginBtn.setOnClickListener(v -> {
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

            // Login with Firebase
            fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (!finishAuthRequest()) {
                    return;
                }

                if (task.isSuccessful()) {
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                } else {
                    handleLoginFailure(Objects.requireNonNull(task.getException()));
                }
            });
        });

        RegisterBtn.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), Register.class));
        });

    }

    private void handleLoginFailure(Exception exception) {
        LoginBtn.setEnabled(true);

        String message = "Login failed. Please try again.";

        if (exception instanceof FirebaseAuthInvalidUserException) {
            Email.setError("No account found for this email");
            Email.requestFocus();
            message = "No account found for this email.";
        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
            FirebaseAuthInvalidCredentialsException authException = (FirebaseAuthInvalidCredentialsException) exception;
            if ("ERROR_INVALID_EMAIL".equals(authException.getErrorCode())) {
                Email.setError("Enter a valid email address");
                Email.requestFocus();
                message = "Enter a valid email address.";
            } else {
                Password.setError("Incorrect email or password");
                Password.requestFocus();
                message = "Incorrect email or password.";
            }
        } else if (exception instanceof FirebaseNetworkException) {
            message = "Network error. Check your internet connection and try again.";
        } else if (exception instanceof FirebaseTooManyRequestsException) {
            message = "Too many login attempts. Please try again later.";
        } else if (exception instanceof FirebaseAuthException) {
            message = ((FirebaseAuthException) exception).getMessage();
        }

        Log.e(TAG, "Login failed", exception);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void startAuthRequest() {
        isAuthRequestPending = true;
        LoginBtn.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        clearAuthTimeout();

        authTimeoutRunnable = () -> {
            if (!isAuthRequestPending || isFinishing() || isDestroyed()) {
                return;
            }

            isAuthRequestPending = false;
            progressBar.setVisibility(View.GONE);
            LoginBtn.setEnabled(true);
            boolean hasNetworkConnection = hasNetworkConnection();
            Log.e(TAG, "Login request timed out. Network connected: " + hasNetworkConnection);
            Toast.makeText(
                    this,
                    hasNetworkConnection
                            ? "Login timed out while contacting Firebase. Check the device date and internet connection, then try again."
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