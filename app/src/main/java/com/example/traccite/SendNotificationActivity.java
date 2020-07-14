package com.example.traccite;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SendNotificationActivity extends AppCompatActivity {

  private AutoCompleteTextView mDropdownMenu;

  @NonNull
  public static Intent createIntent(@NonNull Context context) {
    return new Intent(context, SendNotificationActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_send_notification);

    mDropdownMenu = findViewById(R.id.notification_type_dropdown);

    ArrayAdapter<String> adapter = new ArrayAdapter<>(
      this,
      R.layout.dropdown_item,
      getResources().getStringArray(R.array.notification_type)
    );

    mDropdownMenu.setInputType(InputType.TYPE_NULL);
    mDropdownMenu.setAdapter(adapter);
  }
}