package com.example.traccite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class NotifyCountryActivity extends AppCompatActivity {

  @NonNull
  public static Intent createIntent(@NonNull Context context) {
    return new Intent(context, NotifyCountryActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_notify_country);
  }
}