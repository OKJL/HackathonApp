package com.example.traccite;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NotifySpecificUserActivity extends AppCompatActivity {

  @NonNull
  public static Intent createIntent(@NonNull Context context) {
    return new Intent(context, NotifySpecificUserActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_notify_specific_user);
  }
}