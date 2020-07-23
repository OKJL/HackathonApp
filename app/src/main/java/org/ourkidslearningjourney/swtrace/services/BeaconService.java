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
import com.kontakt.sdk.android.ble.manager.ProximityManager;
import com.kontakt.sdk.android.ble.manager.ProximityManagerFactory;
import com.kontakt.sdk.android.ble.manager.listeners.EddystoneListener;
import com.kontakt.sdk.android.ble.manager.listeners.simple.SimpleEddystoneListener;
import com.kontakt.sdk.android.ble.spec.EddystoneFrameType;
import com.kontakt.sdk.android.common.profile.IEddystoneDevice;
import com.kontakt.sdk.android.common.profile.IEddystoneNamespace;

import org.ourkidslearningjourney.swtrace.MainActivity;
import org.ourkidslearningjourney.swtrace.R;

import java.util.EnumSet;

import static org.ourkidslearningjourney.swtrace.SWTrace.CHANNEL_ID;

public class BeaconService extends Service {

  private static final String TAG = "BeaconService";

  private boolean isRunning;
  private ProximityManager mProximityManager;

  @NonNull
  public static Intent createIntent(@NonNull Context context) {
    return new Intent(context, BeaconService.class);
  }

  @Override
  public void onCreate() {
    super.onCreate();

    mProximityManager = ProximityManagerFactory.create(this);
    mProximityManager.configuration()
      .scanPeriod(ScanPeriod.RANGING)
      .scanMode(ScanMode.BALANCED)
      .eddystoneFrameTypes(EnumSet.of(EddystoneFrameType.UID));
    mProximityManager.setEddystoneListener(createEddystoneListener());
  }

  private EddystoneListener createEddystoneListener() {
    return new SimpleEddystoneListener() {
      @Override
      public void onEddystoneDiscovered(IEddystoneDevice eddystone, IEddystoneNamespace namespace) {
        super.onEddystoneDiscovered(eddystone, namespace);

        Log.i(TAG, "Eddystone Discovered: " + eddystone.toString());
      }
    };
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    super.onStartCommand(intent, flags, startId);

    if (isRunning) {
      Toast.makeText(this, "Service is already running!", Toast.LENGTH_LONG).show();

      return START_STICKY;
    }

    /*
     * Creates a pending intent for redirecting notification
     * onClick events to an activity
     */
    PendingIntent pIntent = PendingIntent.getActivity(
      this,
      0,
      new Intent(this, MainActivity.class),
      0
    );

    /*
     * Build the sticky notification
     */
    Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
      .setContentTitle("Scanning Active")
      .setContentText("Actively scanning for gantries.")
      .setSmallIcon(R.mipmap.ic_launcher)
      .setContentIntent(pIntent)
      .build();

    /*
     * Set the foreground service with sticky notification
     */
    startForeground(1, notification);

    return START_STICKY;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();

    if (mProximityManager != null) {
      mProximityManager.disconnect();
      mProximityManager = null;
    }

    Toast.makeText(this, "Scanning Service Stopped!", Toast.LENGTH_LONG).show();
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }
}
