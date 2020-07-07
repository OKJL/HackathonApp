package com.example.traccite;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class AppTraCCite extends Application {
  /*
   * SharedPreferences: Preference Names
   */
  public static final String GLOBAL_PREFS = "_";

  /*
   * SharedPreferences: Key-Value Pairs
   */
  public static final String FCM_TOKEN_KEY = "FCM_TOKEN_ID";
  public static final String SETUP_COMPLETED_KEY = "SETUP_COMPLETED";

  /*
   * Logcat: Logging Tag
   */
  private static final String TAG = "AppTraCCite";

  @Override
  public void onCreate() {
    super.onCreate();
  }
}
