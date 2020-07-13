package com.example.traccite;

import android.app.Application;

import com.example.traccite.services.FirebaseService;

public class AppTraCCite extends Application {
  @Override
  public void onCreate() {
    super.onCreate();

    /*
     * Initialize Services
     */
    FirebaseService.initializeService();
  }
}
