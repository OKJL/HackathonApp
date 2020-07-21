package org.ourkidslearningjourney.swtrace.services;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

public class PermissionService {

  public static final int RC_PERMISSIONS = 1028;

  public static final String[] PERMISSIONS = {
    Manifest.permission.BLUETOOTH,
    Manifest.permission.BLUETOOTH_ADMIN,
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION,
  };

  public static boolean hasPermissions(Context context, String... permissions) {
    if (context == null && permissions == null) {
      return true;
    }

    for (String permission : permissions) {
      if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
        return false;
      }
    }

    return true;
  }
}
