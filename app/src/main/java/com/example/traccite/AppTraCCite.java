package com.example.traccite;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class AppTraCCite extends Application {
  /*
   * SharedPreferences: Preference Names
   */
  public static final String PREF_SETUP = "SETUP_PREFERENCES";

  /*
   * SharedPreferences: Key-Value Pairs
   */
  public static final String SETUP_KEY = "SETUP_COMPLETED";

  /*
   * Logcat: Logging Tag
   */
  private static final String TAG = "AppTraCCite";

  @Override
  public void onCreate() {
    super.onCreate();
  }
}
