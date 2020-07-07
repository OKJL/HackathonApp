package com.example.traccite.services;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.traccite.AppTraCCite;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FCMService extends FirebaseMessagingService {
  /*
   * Logcat: Logging Tag
   */
  private static final String TAG = "FCMService";

  public static String getToken(Context context) {
    return context
      .getSharedPreferences(AppTraCCite.GLOBAL_PREFS, MODE_PRIVATE)
      .getString(AppTraCCite.FCM_TOKEN_KEY, null);
  }

  @Override
  public void onNewToken(@NonNull String token) {
    super.onNewToken(token);
    Log.d(TAG, "Token Refreshed: " + token);

    getSharedPreferences(AppTraCCite.GLOBAL_PREFS, MODE_PRIVATE)
      .edit()
      .putString(AppTraCCite.FCM_TOKEN_KEY, token)
      .apply();
  }

  @Override
  public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
    super.onMessageReceived(remoteMessage);
  }
}
