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

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.kontakt.sdk.android.common.profile.IEddystoneDevice;
import com.kontakt.sdk.android.common.profile.IEddystoneNamespace;

import org.jetbrains.annotations.NotNull;
import org.ourkidslearningjourney.swtrace.R;
import org.ourkidslearningjourney.swtrace.SWTrace;

public class NotificationService {

  public static final String ACTION_DEVICE_DISCOVERED = "ACTION_DEVICE_DISCOVERED";
  public static final String EXTRA_DEVICE_STATUS = "EXTRA_DEVICE_STATUS";
  public static final String EXTRA_EDDYSTONE_DEVICE = "EXTRA_EDDYSTONE_DEVICE";
  public static final String EXTRA_NAMESPACE_EDDYSTONE = "EXTRA_NAMESPACE_EDDYSTONE";

  public static final BroadcastReceiver BROADCAST_RECEIVER = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, @NotNull Intent intent) {
      int status = intent.getIntExtra(EXTRA_DEVICE_STATUS, -1);
      IEddystoneDevice device = intent.getParcelableExtra(EXTRA_EDDYSTONE_DEVICE);
      IEddystoneNamespace namespace = intent.getParcelableExtra(EXTRA_NAMESPACE_EDDYSTONE);

      switch (status) {
        case 0:
          displayNotification(
            context,
            SWTrace.CHANNEL_ID,
            "Beacon Lost",
            "UID: " + device.getUniqueId() + " Namespace: " + namespace.getNamespace()
          );
          break;
        case 1:
          displayNotification(
            context,
            SWTrace.CHANNEL_ID,
            "Beacon Discovered",
            "UID: " + device.getUniqueId() + " Namespace: " + namespace.getNamespace()
          );
          break;
        default:
          displayNotification(
            context,
            SWTrace.CHANNEL_ID,
            "Whoops!",
            "We're unable to identify the gantry beacon. Please try again later"
          );
      }
    }
  };

  public static void displayNotification(
    Context context,
    String channel,
    String title,
    String body
  ) {
    PendingIntent pIntent = PendingIntent.getActivity(
      context,
      0,
      new Intent().addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
      PendingIntent.FLAG_CANCEL_CURRENT
    );

    NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(context, channel)
      .setContentIntent(pIntent)
      .setSmallIcon(R.mipmap.ic_launcher)
      .setContentTitle(title)
      .setContentText(body)
      .setPriority(NotificationCompat.PRIORITY_MAX)
      .setAutoCancel(true);

    NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

    nManager.notify(2, nBuilder.build());
  }
}
