package com.example.traccite.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;

public class FCMService extends FirebaseMessagingService {

  // Logcat: Logging tag
  private static final String TAG = "FCMService";

  @Override
  public void onNewToken(@NonNull String token) {
    super.onNewToken(token);
    Log.d(TAG, "Token Refreshed: " + token);
  }
}
