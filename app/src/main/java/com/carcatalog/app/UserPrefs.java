package com.carcatalog.app;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

/**
 * Local user storage via SharedPreferences.
 * Stores registered users as "username|||password" entries in a string set.
 * No backend or internet required.
 */
public final class UserPrefs {

    private static final String PREF_NAME = "car_catalog_users";
    private static final String KEY_USERS = "registered_users";
    private static final String KEY_LOGGED = "logged_in_user";
    private static final String SEP = "|||";

    private UserPrefs() {
        // Utility class
    }

    private static SharedPreferences prefs(Context ctx) {
        return ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    /** Register a new user. Returns false if username is already taken. */
    public static boolean register(Context ctx, String username, String password) {
        SharedPreferences p = prefs(ctx);
        Set<String> existing = p.getStringSet(KEY_USERS, new HashSet<>());
        Set<String> users = existing == null ? new HashSet<>() : new HashSet<>(existing);

        for (String entry : users) {
            if (entry.startsWith(username + SEP)) {
                return false; // already taken
            }
        }

        users.add(username + SEP + password);
        p.edit().putStringSet(KEY_USERS, users).apply();
        return true;
    }

    /** Validate credentials. Returns true on success and saves session. */
    public static boolean login(Context ctx, String username, String password) {
        Set<String> users = prefs(ctx).getStringSet(KEY_USERS, new HashSet<>());
        boolean ok = users != null && users.contains(username + SEP + password);
        if (ok) {
            prefs(ctx).edit().putString(KEY_LOGGED, username).apply();
        }
        return ok;
    }

    /** Clear the session. */
    public static void logout(Context ctx) {
        prefs(ctx).edit().remove(KEY_LOGGED).apply();
    }

    /** Returns the logged-in username, or null if not logged in. */
    public static String getLoggedInUser(Context ctx) {
        return prefs(ctx).getString(KEY_LOGGED, null);
    }
}
