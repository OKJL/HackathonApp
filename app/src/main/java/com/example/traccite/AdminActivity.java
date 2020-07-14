package com.example.traccite;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class AdminActivity extends AppCompatActivity {
  private Button mRegisterBeacons;
  private Button mSendNotification;

  @NonNull
  public static Intent createIntent(@NonNull Context context) {
    return new Intent(context, AdminActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin);

    mRegisterBeacons = findViewById(R.id.register_beacons);
    mSendNotification = findViewById(R.id.send_notification);

    mRegisterBeacons.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

      }
    });

    mSendNotification.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        startActivity(NotificationSelectorActivity.createIntent(AdminActivity.this));
      }
    });

//    btnFCM.setOnClickListener(new View.OnClickListener(){
//      @Override
//      public void onClick(View v) {
//        // TODO: Go the FCM page
//      }
//    });
//
//    btnBluetooth.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        // TODO: GO to "Create Beacon Page
//      }
//    });
  }
}
