package com.example.traccite;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

public class AppTraCCite extends Application {
  // SharedPreferences: Preference Names
  public static final String PREF_SETUP = "SETUP_PREFERENCES";

  // SharedPreferences: Key-Value Pairs
  public static final String SETUP_KEY = "SETUP_COMPLETED";

  @Override
  public void onCreate() {
    super.onCreate();
  }
}
