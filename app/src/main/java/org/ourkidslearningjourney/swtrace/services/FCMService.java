package org.ourkidslearningjourney.swtrace.services;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FCMService extends FirebaseMessagingService {
  private static final String TAG = "FCMService";

  public static Task<Void> subscribeToTopic(String topic) {
    return FirebaseMessaging
      .getInstance()
      .subscribeToTopic(topic.toUpperCase());
  }

  public static String getFCMToken(Context context) {
    return context
      .getSharedPreferences(PreferencesService.GLOBAL_PREFERENCES, MODE_PRIVATE)
      .getString(PreferencesService.FCM_TOKEN_KEY, null);
  }

  public static void fetchFCMToken(final Context context) {
    FirebaseInstanceId
      .getInstance()
      .getInstanceId()
      .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
        @Override
        public void onComplete(@NonNull Task<InstanceIdResult> task) {
          Log.i(TAG, "Retrieved FCM Token: " + task.getResult().getToken());

          context
            .getSharedPreferences(PreferencesService.GLOBAL_PREFERENCES, MODE_PRIVATE)
            .edit()
            .putString(PreferencesService.FCM_TOKEN_KEY, task.getResult().getToken())
            .apply();
        }
      })
      .addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
          Log.e(TAG, "Failed to retrieve FCM token: " + e.getMessage());

          Toast.makeText(context, "Failed To Retrieve FCM Token", Toast.LENGTH_LONG).show();
        }
      });
  }

  @Override
  public void onNewToken(@NonNull String token) {
    super.onNewToken(token);

    Log.i(TAG, "Token Refreshed: " + token);

    getApplicationContext()
      .getSharedPreferences(
        PreferencesService.GLOBAL_PREFERENCES,
        MODE_PRIVATE
      )
      .edit()
      .putString(PreferencesService.FCM_TOKEN_KEY, token)
      .apply();
  }

  @Override
  public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
    super.onMessageReceived(remoteMessage);
  }
}
