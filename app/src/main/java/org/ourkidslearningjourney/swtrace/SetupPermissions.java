package org.ourkidslearningjourney.swtrace;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.ourkidslearningjourney.swtrace.services.PermissionService;

public class SetupPermissions extends AppCompatActivity implements View.OnClickListener {

  private Button mBtnEnableBluetooth;

  @NonNull
  public static Intent createIntent(@NonNull Context context) {
    return new Intent(context, SetupPermissions.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_setup_permissions);

    mBtnEnableBluetooth = findViewById(R.id.btn_enable_bluetooth);
    mBtnEnableBluetooth.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
    startActivityForResult(intent, PermissionService.RC_PERMISSIONS);

    ActivityCompat.requestPermissions(
      this,
      PermissionService.PERMISSIONS,
      PermissionService.RC_PERMISSIONS);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == PermissionService.RC_PERMISSIONS && resultCode == RESULT_OK) {
      if (PermissionService.hasPermissions(this, PermissionService.PERMISSIONS)) {
        startActivity(HomeActivity.createIntent(this));
        finishAffinity();
      }
    }
  }

}