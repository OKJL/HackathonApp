package com.example.traccite;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

public class HomeActivity extends AppCompatActivity {
  int REQUEST_ENABLE_BT = 0;

  /*
   * Android: From Layout
   */
  private Button mSignOut;

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

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

    /*
     * Link variables to layout ids
     */
    mSignOut = findViewById(R.id.sign_out_button);

    /*
     * Listen for onClick in signOutButton
     */
    listenForSignOut();

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