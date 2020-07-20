package com.example.traccite;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest.permission;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.traccite.services.BtleBackgroundScanService;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.kontakt.sdk.android.ble.configuration.ScanMode;
import com.kontakt.sdk.android.ble.configuration.ScanPeriod;
import com.kontakt.sdk.android.ble.connection.OnServiceReadyListener;
import com.kontakt.sdk.android.ble.manager.ProximityManager;
import com.kontakt.sdk.android.ble.manager.ProximityManagerFactory;
import com.kontakt.sdk.android.ble.manager.listeners.EddystoneListener;
import com.kontakt.sdk.android.ble.manager.listeners.simple.SimpleEddystoneListener;
import com.kontakt.sdk.android.common.KontaktSDK;
import com.kontakt.sdk.android.common.profile.IEddystoneDevice;
import com.kontakt.sdk.android.common.profile.IEddystoneNamespace;
import com.kontakt.sdk.android.common.profile.RemoteBluetoothDevice;

import org.w3c.dom.Text;

import java.security.Permission;

import static android.app.Service.START_STICKY;

public class HomeActivity extends AppCompatActivity {
  int REQUEST_ENABLE_BT = 0;
  int REQUEST_ALL_PERMISSION = 1;


  private static final String TAG = "KontaktIO";
  private static Intent btleServiceIntent;
  private final Handler handler = new Handler();
  private static Context mApplicationContext;
  private static final String KONTAKT_API_KEY = "zGwvulRMVFNSGUGzqiZwellCmsrpKKIl";


  /*
   * Android: From Layout
   */
  private Button mSignOut;
  private Button mAdminActivity;

  @NonNull
  public static Intent createIntent(@NonNull Context context) {
    return new Intent(context, HomeActivity.class);
  }

  private void listenForSignOut() {
    mSignOut.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        AuthUI
          .getInstance()
          .signOut(getApplicationContext())
          .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
              Toast.makeText(
                getApplicationContext(),
                "Signed Out Successfully",
                Toast.LENGTH_SHORT
              ).show();

              startActivity(MainActivity.createIntent(HomeActivity.this));
              finish();
            }
          })
          .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
              Toast.makeText(
                getApplicationContext(),
                "Failed to sign out",
                Toast.LENGTH_SHORT
              ).show();
            }
          });
      }
    });
  }

  private void listenForAdminActivity() {
    mAdminActivity.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        startActivity(AdminActivity.createIntent(HomeActivity.this));
      }
    });
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    btleServiceIntent = new Intent(getApplicationContext(), BtleBackgroundScanService.class);

    String[] Permissions = {
      permission.ACCESS_COARSE_LOCATION,
      permission.ACCESS_FINE_LOCATION
    };
    if (!hasPermissionAccess(Permissions)) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        requestPermissions(Permissions, REQUEST_ALL_PERMISSION);
      }
    }

    /*
     * Link variables to layout ids
     */
    mSignOut = findViewById(R.id.sign_out_button);
    mAdminActivity = findViewById(R.id.admin_button);

    /*
     * Listen for onClick events
     */
    listenForSignOut();
    listenForAdminActivity();

    // Get permissions
    final BluetoothAdapter BluetoothAdapter = android.bluetooth.BluetoothAdapter.getDefaultAdapter();
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
    if (BluetoothAdapter == null) {
      // Phone does not support bluetooth
      alertDialogBuilder.setTitle("Device not supported!");
      alertDialogBuilder.setMessage("Your Phone does not support bluetooth!");
      alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          // exit the app
          finish();
          System.exit(0);
        }
      });
      AlertDialog alertDialog = alertDialogBuilder.create();
      alertDialog.show();
    } else {
      if (!BluetoothAdapter.isEnabled()) {
        Intent itEnableBluetooth = new Intent(android.bluetooth.BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(itEnableBluetooth, REQUEST_ENABLE_BT);
      }
    }
    mApplicationContext = getApplicationContext();

    // Init Kontaktio API
    initializeDependencies();
    startBackgroundBtleService();

    // Register BroadcastReceiver that will accept results from background scanning
  }

  private boolean hasPermissionAccess(String... permissions) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      for (String permission : permissions) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
          return false;
        }
      }
    }
    return true;
  }
  protected void onResume() {
    super.onResume();
    //Register Broadcast receiver that will accept results from background scanning
    IntentFilter intentFilter = new IntentFilter(BtleBackgroundScanService.ACTION_DEVICE_DISCOVERED);
    registerReceiver(scanningBroadcastReceiver, intentFilter);
  }
  @Override
  protected void onPause() {
    unregisterReceiver(scanningBroadcastReceiver);
    super.onPause();
  }
  private void initializeDependencies() {
    KontaktSDK.initialize(KONTAKT_API_KEY);
  }
  private void startBackgroundBtleService() {
    startService(btleServiceIntent);
  }
  private final BroadcastReceiver scanningBroadcastReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      //Device discovered!
      int devicesCount = intent.getIntExtra(BtleBackgroundScanService.EXTRA_DEVICES_COUNT, 0);
      RemoteBluetoothDevice device = intent.getParcelableExtra(BtleBackgroundScanService.EXTRA_DEVICE);
      Log.i(TAG, String.format("Total discovered devices: %d\n\nLast scanned device:\n%s", devicesCount, device.toString()));
    }
  };
}