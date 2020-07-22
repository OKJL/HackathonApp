package org.ourkidslearningjourney.swtrace.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import org.ourkidslearningjourney.swtrace.MainActivity;
import org.ourkidslearningjourney.swtrace.R;

public class BeaconScanningService extends Service {

  private static final int RC_NOTIFICATION = 1028;
  private static final String CHANNEL_ID = "BeaconScanningService";

  @NonNull
  public static Intent createIntent(@NonNull Context context) {
    return new Intent(context, BeaconScanningService.class);
  }

  @Override
  public void onCreate() {
    super.onCreate();
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    super.onStartCommand(intent, flags, startId);

    createNotificationChannel();

    Intent nIntent = new Intent(this, MainActivity.class);
    PendingIntent pIntent = PendingIntent.getActivity(this, RC_NOTIFICATION, nIntent, 0);

    Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
      .setContentTitle("Scanning Active")
      .setContentText("Checking-in and out process is now automatic while this service is active.")
      .setSmallIcon(R.mipmap.ic_launcher)
      .setContentIntent(pIntent)
      .build();

    startForeground(1, notification);

    return START_NOT_STICKY;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  private void createNotificationChannel() {
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
      NotificationChannel channel = new NotificationChannel(
        CHANNEL_ID,
        "Auto Tracing System Active",
        NotificationManager.IMPORTANCE_DEFAULT
      );

      NotificationManager manager = getSystemService(NotificationManager.class);
      manager.createNotificationChannel(channel);
    }
  }
}
