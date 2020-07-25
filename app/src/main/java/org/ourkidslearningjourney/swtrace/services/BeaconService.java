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
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

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

import org.ourkidslearningjourney.swtrace.R;
import org.ourkidslearningjourney.swtrace.SWTrace;

import java.util.Collections;
import java.util.List;

public class BeaconService extends Service implements EddystoneListener, OnServiceReadyListener, ScanStatusListener {

  private static final String TAG = "BeaconService";

  private ProximityManager mProximityManager;

  @NonNull
  public static Intent createIntent(@NonNull Context context) {
    return new Intent(context, BeaconService.class);
  }

  @Override
  public void onCreate() {
    super.onCreate();

    mProximityManager = ProximityManagerFactory.create(this);
    mProximityManager.setEddystoneListener(this);
    mProximityManager.setScanStatusListener(this);
    mProximityManager.configuration()
      .scanMode(ScanMode.BALANCED)
      .scanPeriod(ScanPeriod.RANGING)
      .eddystoneFrameTypes(Collections.singletonList(EddystoneFrameType.UID));
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    super.onStartCommand(intent, flags, startId);

    mProximityManager.connect(this);

    PendingIntent pIntent = PendingIntent.getActivity(
      this,
      0,
      new Intent(),
      0
    );
    Notification notification = new NotificationCompat.Builder(this, SWTrace.CHANNEL_ID)
      .setContentTitle("Gantry Monitoring Active")
      .setContentText("This is to inform you that your app is working as intended")
      .setSmallIcon(R.mipmap.ic_launcher)
      .setContentIntent(pIntent)
      .build();

    startForeground(1, notification);

    return START_STICKY;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();

    mProximityManager.disconnect();
    mProximityManager = null;
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  public void onServiceReady() {
    mProximityManager.startScanning();
  }

  @Override
  public void onScanStart() {
    Toast.makeText(this, "Scanning Started", Toast.LENGTH_LONG).show();

    Log.i(TAG, "Scanning Started");
  }

  @Override
  public void onScanStop() {
    Toast.makeText(this, "Scanning Stopped", Toast.LENGTH_LONG).show();

    Log.i(TAG, "Scanning Stopped");
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
  public void onEddystoneDiscovered(IEddystoneDevice eddystone, IEddystoneNamespace namespace) {
    Toast.makeText(
      this,
      "Eddystone Discovered: " + eddystone.toString(),
      Toast.LENGTH_SHORT
    ).show();

    Log.i(TAG, "Eddystone Discovered: " + eddystone.toString());
  }

  @Override
  public void onEddystonesUpdated(List<IEddystoneDevice> eddystones, IEddystoneNamespace namespace) {

  }

  @Override
  public void onEddystoneLost(IEddystoneDevice eddystone, IEddystoneNamespace namespace) {
    Toast.makeText(
      this,
      "Eddystone Lost: " + eddystone.toString(),
      Toast.LENGTH_SHORT
    ).show();

    Log.i(TAG, "Eddystone Lost: " + eddystone.toString());
  }
}
