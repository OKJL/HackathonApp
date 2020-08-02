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

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.kontakt.sdk.android.ble.configuration.ScanMode;
import com.kontakt.sdk.android.ble.configuration.ScanPeriod;
import com.kontakt.sdk.android.ble.connection.OnServiceReadyListener;
import com.kontakt.sdk.android.ble.exception.ScanError;
import com.kontakt.sdk.android.ble.manager.ProximityManager;
import com.kontakt.sdk.android.ble.manager.ProximityManagerFactory;
import com.kontakt.sdk.android.ble.manager.listeners.EddystoneListener;
import com.kontakt.sdk.android.ble.manager.listeners.ScanStatusListener;
import com.kontakt.sdk.android.ble.spec.EddystoneFrameType;
import com.kontakt.sdk.android.common.profile.IEddystoneDevice;
import com.kontakt.sdk.android.common.profile.IEddystoneNamespace;

import org.jetbrains.annotations.NotNull;
import org.ourkidslearningjourney.swtrace.PreferenceConstants;
import org.ourkidslearningjourney.swtrace.R;
import org.ourkidslearningjourney.swtrace.SWTrace;
import org.ourkidslearningjourney.swtrace.models.Entry;

import java.util.Collections;
import java.util.List;

public class BeaconService extends Service
  implements EddystoneListener, OnServiceReadyListener, ScanStatusListener {

  /**
   * Tag used for logging in logcat.
   */
  private static final String TAG = "BeaconService";

  /**
   * Represents the entrance code for beacons.
   */
  private static final String BEACON_ENTR = "01";

  /**
   * Represents the exit code for beacons.
   */
  private static final String BEACON_EXIT = "02";

  /**
   * Represents the current status of the foreground service.
   */
  private static boolean isRunning;

  /**
   * Represents Kontakt.io's Eddystone scanning manager.
   */
  private static ProximityManager sProximityManager;

  /**
   * The tracking system for maintaining current locations.
   */
  private static SharedPreferences sGantryPreferences;

  /**
   * Checks if the service is currently running.
   *
   * @return status of the service.
   */
  public static boolean isRunning() {
    return isRunning;
  }

  /**
   * Creates a new intent for starting the foreground service.
   *
   * @param context the caller's context
   * @return the intent to start the foreground service.
   */
  @NonNull
  public static Intent createIntent(@NonNull Context context) {
    return new Intent(context, BeaconService.class);
  }

  @Override
  public void onCreate() {
    super.onCreate();

    sGantryPreferences = getApplicationContext().getSharedPreferences(
      PreferenceConstants.PREF_GANTRIES,
      MODE_PRIVATE
    );

    sProximityManager = ProximityManagerFactory.create(this);
    sProximityManager.setEddystoneListener(this);
    sProximityManager.setScanStatusListener(this);
    sProximityManager.configuration()
      .scanMode(ScanMode.BALANCED)
      .scanPeriod(ScanPeriod.RANGING)
      .eddystoneFrameTypes(Collections.singletonList(EddystoneFrameType.UID));
  }

  @Override
  public void onDestroy() {
    super.onDestroy();

    sProximityManager.disconnect();
    sProximityManager = null;
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    super.onStartCommand(intent, flags, startId);

    PendingIntent pIntent = PendingIntent.getActivity(
      this,
      0,
      new Intent(),
      0
    );
    Notification notification = new NotificationCompat.Builder(this, SWTrace.CHANNEL_ID)
      .setContentTitle("Gantry Monitoring Active")
      .setContentText("Your app is working as intended.")
      .setSmallIcon(R.mipmap.ic_launcher)
      .setContentIntent(pIntent)
      .build();

    startForeground(1, notification);

    Log.i(TAG, "onStartCommand: Foreground Service Started");

    sProximityManager.connect(this);

    return START_STICKY;
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  public void onServiceReady() {
    sProximityManager.startScanning();
  }

  @Override
  public void onScanStart() {
    isRunning = true;

    Log.i(TAG, "onScanStart: Scanning Started");
  }

  @Override
  public void onScanStop() {
    isRunning = false;

    Log.i(TAG, "onScanStop: Scanning Stopped");
  }

  @Override
  public void onScanError(ScanError scanError) {
    /* TODO: Implement code */
  }

  @Override
  public void onMonitoringCycleStart() {
    /* TODO: Implement code */
  }

  @Override
  public void onMonitoringCycleStop() {
    /* TODO: Implement code */
  }

  @Override
  public void onEddystoneDiscovered(
    @NotNull final IEddystoneDevice eddystone,
    final IEddystoneNamespace namespace
  ) {
    final String instanceId = eddystone.getInstanceId();
    final String locationCode = instanceId.substring(0, 11);
    final String directionCode = instanceId.substring(10, 12);

    Log.i(TAG, "onEddystoneDiscovered: " + eddystone.toString());

    if (directionCode.equals(BEACON_ENTR)) {
      Entry entry = new Entry();
      entry.put(Entry.FCM_TOKEN, FCMService.getFCMToken(this));
      entry.put(Entry.BEACON_ID, FirebaseService.getBeaconReference(instanceId));
      entry.put(Entry.ENTERED_TIMESTAMP, FirebaseService.getServerTimestamp());
      entry.put(Entry.EXITED_TIMESTAMP, FirebaseService.getServerTimestamp(FirebaseService.ONE_HOUR));

      FirebaseService.setEntriesCollection(entry)
        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
          @Override
          public void onComplete(@NonNull Task<DocumentReference> task) {
            if (!task.isSuccessful()) {
              onDeviceDiscovered(eddystone, namespace, -1);

              Log.e(TAG, "onComplete: Failed to create entry document");
            }

            onDeviceDiscovered(eddystone, namespace, 1);

            Log.i(TAG, "onComplete: Successfully created entry document");

            sGantryPreferences.edit().putString(instanceId, task.getResult().getId()).apply();
          }
        });

      return;
    }

    String docId = sGantryPreferences.getString(instanceId.substring(0, 11) + "1", null);

    if (docId != null) {
      Entry entry = new Entry();
      entry.put(Entry.EXITED_TIMESTAMP, FirebaseService.getServerTimestamp());

      FirebaseService.setEntriesCollection(docId, entry)
        .addOnSuccessListener(new OnSuccessListener<Void>() {
          @Override
          public void onSuccess(Void aVoid) {
            onDeviceDiscovered(eddystone, namespace, 0);

            Log.i(TAG, "Document Update Success.");

            sGantryPreferences.edit().remove(instanceId).apply();
          }
        })
        .addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
            onDeviceDiscovered(eddystone, namespace, -1);

            Log.i(TAG, "Document Update Failed.");
          }
        });
    }

    onDeviceDiscovered(eddystone, namespace, -1);
  }

  @Override
  public void onEddystonesUpdated(List<IEddystoneDevice> eddystones, IEddystoneNamespace namespace) {

  }

  @Override
  public void onEddystoneLost(@NotNull IEddystoneDevice eddystone, IEddystoneNamespace namespace) {
    Log.i(TAG, "Eddystone Lost: " + eddystone.toString());

    onDeviceDiscovered(eddystone, namespace, 0);

    sProximityManager.restartScanning();
  }

  private void onDeviceDiscovered(IEddystoneDevice device, IEddystoneNamespace namespace, int status) {
    Intent intent = new Intent()
      .setAction(NotificationService.ACTION_DEVICE_DISCOVERED)
      .putExtra(NotificationService.EXTRA_EDDYSTONE_DEVICE, device)
      .putExtra(NotificationService.EXTRA_NAMESPACE_EDDYSTONE, namespace)
      .putExtra(NotificationService.EXTRA_DEVICE_STATUS, status);

    sendBroadcast(intent);
  }
}
