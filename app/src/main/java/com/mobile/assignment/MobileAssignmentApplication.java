package com.mobile.assignment;

import android.app.Application;
import android.util.Log;

import com.google.firebase.FirebaseApp;

public class MobileAssignmentApplication extends Application {

    private static final String TAG = "MobileAssignmentApp";

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp firebaseApp = FirebaseApp.initializeApp(this);
        if (firebaseApp == null) {
            Log.e(TAG, "Firebase failed to initialize at app startup");
        }
    }
}