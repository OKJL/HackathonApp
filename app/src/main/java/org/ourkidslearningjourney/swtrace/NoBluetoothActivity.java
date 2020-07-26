package org.ourkidslearningjourney.swtrace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NoBluetoothActivity extends AppCompatActivity implements View.OnClickListener {

  private Button btnClose;

  @NonNull
  public static Intent createIntent(@NonNull Context context) {
    return new Intent(context, NoBluetoothActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_no_bluetooth);

    btnClose = findViewById(R.id.btn_exit);
    btnClose.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    finishAffinity();
    System.exit(0);
  }
}