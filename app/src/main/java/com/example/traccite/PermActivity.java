package com.example.traccite;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class PermActivity
  extends AppCompatActivity
  implements View.OnClickListener {
  private static final BluetoothAdapter BLUETOOTH_ADAPTER =
    android.bluetooth.BluetoothAdapter.getDefaultAdapter();

  private static AlertDialog.Builder mDialog;

  int REQUEST_ENABLE_BT = 0;
  private Button requestPermissionBtn;

  @NonNull
  public static Intent createIntent(@NonNull Context context) {
    return new Intent(context, PermActivity.class);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == REQUEST_ENABLE_BT) {
      if (resultCode == RESULT_OK) {
        startActivity(HomeActivity.createIntent(this));
        finishAffinity();
      }
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_perm);

    mDialog = new AlertDialog.Builder(this);

    requestPermissionBtn = findViewById(R.id.btn_request_permission);
    requestPermissionBtn.setOnClickListener(this);

    mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
      @Override
      public void onCancel(DialogInterface dialogInterface) {
        finishAffinity();
        System.exit(0);
      }
    });

    if (BLUETOOTH_ADAPTER == null) {
      mDialog.setCancelable(false);
      mDialog.setTitle("Device Not Supported!");
      mDialog.setMessage("Your device does not support Bluetooth!");
      mDialog.setPositiveButton("EXIT", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
          finishAffinity();
          System.exit(0);
        }
      });

      mDialog.show();

      return;
    }

    if (!BLUETOOTH_ADAPTER.isEnabled()) requestBluetoothEnable();
    else {
      startActivity(HomeActivity.createIntent(this));
      finishAffinity();
    }
  }

  private void requestBluetoothEnable() {
    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
    startActivityForResult(intent, REQUEST_ENABLE_BT);
  }

  @Override
  public void onClick(View view) {
    requestBluetoothEnable();
  }
}