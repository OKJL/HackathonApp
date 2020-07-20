package com.example.traccite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kontakt.sdk.android.ble.configuration.ScanMode;
import com.kontakt.sdk.android.ble.configuration.ScanPeriod;
import com.kontakt.sdk.android.ble.connection.OnServiceReadyListener;
import com.kontakt.sdk.android.ble.manager.ProximityManager;
import com.kontakt.sdk.android.ble.manager.ProximityManagerFactory;
import com.kontakt.sdk.android.ble.manager.listeners.EddystoneListener;
import com.kontakt.sdk.android.common.KontaktSDK;
import com.kontakt.sdk.android.common.model.Time;
import com.kontakt.sdk.android.common.profile.IEddystoneDevice;
import com.kontakt.sdk.android.common.profile.IEddystoneNamespace;


import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BeaconActivity extends AppCompatActivity implements View.OnClickListener {
  private ProximityManager proximityManager;
  private TextView statusText;
  private TextView ID;
  public static final String TAG = "ProximityManager";
  private ProgressBar progressBar;
  private TextView Time;
  private Date currenttime;

  @NonNull
  public static Intent createIntent(@NonNull Context context){
    return new Intent(context, BeaconActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_beacon);

    KontaktSDK.initialize("zGwvulRMVFNSGUGzqiZwellCmsrpKKIl");

    Time = (TextView) findViewById(R.id.Time);
    progressBar= (ProgressBar) findViewById(R.id.scanning_progress);
    ID = findViewById(R.id.UniqueID);
    statusText = findViewById(R.id.status);
    currenttime = Calendar.getInstance().getTime();


    setupButtons();

    setupProximityManager();

  }
  private void setupButtons() {
    Button startScanButton = (Button) findViewById(R.id.scan);
    Button stopScanButton = (Button) findViewById(R.id.stopscan);
    startScanButton.setOnClickListener(this);
    stopScanButton.setOnClickListener(this);
  }
  private void setupProximityManager() {
    proximityManager = ProximityManagerFactory.create(this);

    //Configure proximity manager basic options
    proximityManager.configuration()
      //Using ranging for continuous scanning or MONITORING for scanning with intervals
      .scanPeriod(ScanPeriod.RANGING)
      //Using BALANCED for best performance/battery ratio
      .scanMode(ScanMode.BALANCED)
      //OnDeviceUpdate callback will be received with 5 seconds interval
      .deviceUpdateCallbackInterval(TimeUnit.SECONDS.toMillis(5));

    //Setting up Eddystone listeners
    proximityManager.setEddystoneListener(createEddystoneListener());
  }
  private void startScanning() {
    //Connect to scanning service and start scanning when ready
    proximityManager.connect(new OnServiceReadyListener() {
      @Override
      public void onServiceReady() {
        //Check if proximity manager is already scanning
        if (proximityManager.isScanning()) {
          statusText.setText("Scanning");
          return;
        }
        proximityManager.startScanning();
        progressBar.setVisibility(View.VISIBLE);
        Toast.makeText(BeaconActivity.this, "Scanning started", Toast.LENGTH_SHORT).show();
      }
    });
  }
  private void stopScanning() {
    //Stop scanning if scanning is in progress
    if (proximityManager.isScanning()) {
      proximityManager.stopScanning();
      progressBar.setVisibility(View.GONE);
      statusText.setText("Stopped Scanning");
    }
  }
  private EddystoneListener createEddystoneListener() {
    return new EddystoneListener() {
      @Override
      public void onEddystoneDiscovered(IEddystoneDevice eddystone, IEddystoneNamespace namespace) {
        ID.setText("Beacon Discovered: " + eddystone.getUniqueId());
        Time.setText("Time Discovered: " + currenttime);

      }

      @Override
      public void onEddystonesUpdated(List<IEddystoneDevice> eddystones, IEddystoneNamespace namespace) {
        Log.i(TAG, "onEddystonesUpdated: " + eddystones.size());
      }


      @Override
      public void onEddystoneLost(IEddystoneDevice eddystone, IEddystoneNamespace namespace) {
        ID.setText("Beacon Lost: " + eddystone.getName());
        Time.setText("Time Lost: " + currenttime);
      }
    };
  }
  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.scan:
        startScanning();
        break;
      case R.id.stopscan:
        stopScanning();
        break;
    }
  }
  @Override
  protected void onStop() {
    //Stop scanning when leaving screen.
    stopScanning();
    super.onStop();
  }
  protected void onDestroy() {
    //Remember to disconnect when finished.
    proximityManager.disconnect();
    super.onDestroy();
  }
}