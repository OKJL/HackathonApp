package com.example.traccite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminActivity extends AppCompatActivity {
  Button btnFCM;
  Button btnBluetooth;

  @NonNull
  public static Intent createIntent(@NonNull Context context) {
    return new Intent(context, AdminActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin);

    btnFCM.setOnClickListener(new View.OnClickListener(){
      @Override
      public void onClick(View v) {
        // TODO: Go the FCM page
      }
    });

    btnBluetooth.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // TODO: GO to "Create Beacon Page
      }
    });
  }
}
