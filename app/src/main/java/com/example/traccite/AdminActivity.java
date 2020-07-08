package com.example.traccite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class AdminActivity extends AppCompatActivity {

  @NonNull
  public static Intent createIntent(@NonNull Context context) {
    return new Intent(context, AdminActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin);
  }
}