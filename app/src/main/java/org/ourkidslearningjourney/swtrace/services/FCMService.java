/*
 * Copyright (c) 2020 SWTrace Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.ourkidslearningjourney.swtrace.PreferenceConstants;

public class FCMService extends FirebaseMessagingService {

  /*
   * Logcat: Logging Tag
   */
  private static final String TAG = "FCMService";

  /*
   * subscribeToTopic will automatically subscribe the current
   * FCM Token to the topic with Firebase
   */
  @NotNull
  public static Task<Void> subscribeToTopic(@NotNull String topic) {
    return FirebaseMessaging
      .getInstance()
      .subscribeToTopic(topic.toUpperCase());
  }

  /*
   * getFCMToken will retrieve the FCM token currently stored in
   * SharedPreferences.
   *
   * If no value is set, it will return null
   */
  @Nullable
  public static String getFCMToken(@NotNull Context context) {
    return context
      .getSharedPreferences(PreferenceConstants.PREF_GLOBAL, MODE_PRIVATE)
      .getString(PreferenceConstants.PREF_FCM_TOKEN, null);
  }

  /*
   * fetchFCMToken will retrieve the FCM token from Firebase and
   * store the value into SharedPreferences.
   */
  public static void fetchFCMToken(final Context context) {
    FirebaseInstanceId
      .getInstance()
      .getInstanceId()
      .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
        @Override
        public void onComplete(@NonNull Task<InstanceIdResult> task) {
          Log.i(TAG, "Retrieved FCM Token: " + task.getResult().getToken());

          context
            .getSharedPreferences(PreferenceConstants.PREF_GLOBAL, MODE_PRIVATE)
            .edit()
            .putString(PreferenceConstants.PREF_FCM_TOKEN, task.getResult().getToken())
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
        PreferenceConstants.PREF_GLOBAL,
        MODE_PRIVATE
      )
      .edit()
      .putString(PreferenceConstants.PREF_FCM_TOKEN, token)
      .apply();
  }

  @Override
  public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
    super.onMessageReceived(remoteMessage);
  }
}
