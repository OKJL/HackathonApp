package com.example.traccite;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class NotifySuccessActivity
  extends AppCompatActivity
  implements View.OnClickListener {

  private Button mReturnHome;

  @NonNull
  public static Intent createIntent(@NonNull Context context) {
    return new Intent(context, NotifySuccessActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_notify_success);

    mReturnHome = findViewById(R.id.btn_return);
    mReturnHome.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    startActivity(MainActivity.createIntent(this));
    finish();
  }
}