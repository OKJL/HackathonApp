package com.example.traccite;

import android.app.AlertDialog;
import android.app.Notification;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import static android.bluetooth.BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED;

public class HomeActivity extends AppCompatActivity {
  int REQUEST_ENABLE_BT = 0;


  @NonNull
  public static Intent createIntent(@NonNull Context context) {
    return new Intent(context, HomeActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

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
}