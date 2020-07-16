package com.example.traccite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PermActivity extends AppCompatActivity {
  int REQUEST_ENABLE_BT = 0;
  private Button ContinueBtn;

  @NonNull
  public static Intent createIntent(@NonNull Context context) {
    return new Intent(context, PermActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_perm);
    ContinueBtn = findViewById(R.id.ContinueBtn);

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
    ContinueBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        startActivity(HomeActivity.createIntent(PermActivity.this));
      }
    });
  }
}