package com.example.traccite;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.Manifest.permission;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import java.security.Permission;

public class HomeActivity extends AppCompatActivity {
  int REQUEST_ENABLE_BT = 0;
  int REQUEST_ALL_PERMISSION = 1;

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

    String[] Permissions = {
      permission.ACCESS_COARSE_LOCATION,
      permission.ACCESS_FINE_LOCATION
    };
    if(!hasPermissionAccess(Permissions)){
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
}