package com.example.traccite.services;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.traccite.AppTraCCite;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FCMService extends FirebaseMessagingService {
  /*
   * Logcat: Logging Tag
   */
  private static final String TAG = "FCMService";

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

  /*
   * subscribeToTopic automatically subscribes the current FCM token
   * into the topic.
   */
  public static Task<Void> subscribeToTopic(String topic) {
    return FirebaseMessaging.getInstance().subscribeToTopic(topic);
  }

  /*
   * getFCMToken retrieves the FCM token stored inside SharedPreferences
   */
  public static String getFCMToken(Context context) {
    return context
      .getSharedPreferences(AppTraCCite.GLOBAL_PREFS, MODE_PRIVATE)
      .getString(AppTraCCite.FCM_TOKEN_KEY, null);
  }

  /*
   * fetchFCMToken retrieves the FCM token from the Firebase server
   * and stores the token into SharedPreferences
   */
  public static void fetchFCMToken(final Context context) {
    FirebaseInstanceId
      .getInstance()
      .getInstanceId()
      .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
        @Override
        public void onComplete(@NonNull Task<InstanceIdResult> task) {
          Log.i(TAG, "Successfully retrieved FCM token: " + task.getResult().getToken());

          context
            .getSharedPreferences(AppTraCCite.GLOBAL_PREFS, MODE_PRIVATE)
            .edit()
            .putString(AppTraCCite.FCM_TOKEN_KEY, task.getResult().getToken())
            .apply();
        }
      })
      .addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
          Log.e(TAG, "Failed to retrieve FCM token: " + e.getMessage());
        }
      });
  }
}
