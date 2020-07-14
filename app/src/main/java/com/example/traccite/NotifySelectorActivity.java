package com.example.traccite;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class NotifySelectorActivity
  extends AppCompatActivity
  implements AdapterView.OnItemClickListener, View.OnClickListener {

  private static final String TAG = "NotifySelectorActivity";

  private static long sSelectionIndex = -1;

  private Button mContinueBtn;
  private AutoCompleteTextView mDropdownMenu;

  @NonNull
  public static Intent createIntent(@NonNull Context context) {
    return new Intent(context, NotifySelectorActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_notify_selector);

    mContinueBtn = findViewById(R.id.button_continue);
    mDropdownMenu = findViewById(R.id.notification_type_dropdown);

    mContinueBtn.setEnabled(false);
    mContinueBtn.setOnClickListener(this);

    ArrayAdapter<String> adapter = new ArrayAdapter<>(
      this,
      R.layout.dropdown_item,
      getResources().getStringArray(R.array.notification_type)
    );

    mDropdownMenu.setAdapter(adapter);
    mDropdownMenu.setOnItemClickListener(this);
    mDropdownMenu.setInputType(InputType.TYPE_NULL);
  }

  @Override
  public void onItemClick(
    AdapterView<?> parent,
    View view,
    int position,
    long id
  ) {
    sSelectionIndex = parent.getItemIdAtPosition(position);
    mContinueBtn.setEnabled(true);
  }

  @Override
  public void onClick(View view) {
    switch ((int) sSelectionIndex) {
      case 0:
        startActivity(NotifyEveryoneActivity.createIntent(this));
        break;
      case 1:
        startActivity(NotifyCountryActivity.createIntent(this));
        break;
      case 2:
        startActivity(NotifyTimeframeActivity.createIntent(this));
        break;
      case 3:
        startActivity(NotifySpecificUserActivity.createIntent(this));
        break;
      default:
        break;
    }
  }
}