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

public class SetupBluetoothActivity extends AppCompatActivity implements View.OnClickListener {

  private static final int REQUEST_ENABLE_BT = 0;

  private Button mBtnEnableBluetooth;

  @NonNull
  public static Intent createIntent(@NonNull Context context) {
    return new Intent(context, SetupBluetoothActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_setup_bluetooth);

    mBtnEnableBluetooth = findViewById(R.id.btn_enable_bluetooth);
    mBtnEnableBluetooth.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
    startActivityForResult(intent, REQUEST_ENABLE_BT);
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

}