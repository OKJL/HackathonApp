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

import com.example.traccite.models.Notification;
import com.example.traccite.services.FirebaseService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;

public class NotifyCountryActivity
  extends AppCompatActivity
  implements View.OnClickListener, AdapterView.OnItemClickListener {

  private static final String TAG = "NotifyCountryActivity";

  private static String country_name = "";

  private TextInputLayout mMessageTitle;
  private TextInputLayout mMessageBody;
  private AutoCompleteTextView mCountryName;
  private Button mSendNotification;

  @NonNull
  public static Intent createIntent(@NonNull Context context) {
    return new Intent(context, NotifyCountryActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_notify_country);

    ArrayAdapter<String> adapter = new ArrayAdapter<>(
      this,
      R.layout.dropdown_item,
      getResources().getStringArray(R.array.countries)
    );

    mMessageTitle = findViewById(R.id.message_title);
    mMessageBody = findViewById(R.id.message_body);
    mCountryName = findViewById(R.id.country_name);
    mSendNotification = findViewById(R.id.btn_send);

    mCountryName.setAdapter(adapter);
    mCountryName.setOnItemClickListener(this);
    mCountryName.setInputType(InputType.TYPE_NULL);

    mSendNotification.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    if (mMessageTitle.getEditText().getText().toString().isEmpty()) {
      mMessageTitle.setError("Message Title is empty!");
      return;
    }

    if (mMessageTitle.getEditText().getText().length() > 40) {
      mMessageTitle.setError("Message Title is too long!");
      return;
    }

    mMessageTitle.setError(null);

    if (mMessageBody.getEditText().getText().toString().isEmpty()) {
      mMessageBody.setError("Message Body is empty!");
      return;
    }

    if (mMessageBody.getEditText().getText().length() > 500) {
      mMessageBody.setError("Message Body is too long!");
      return;
    }

    mMessageBody.setError(null);

    if (country_name.isEmpty()) {
      mCountryName.setError("Country Name is empty!");
      return;
    }

    mCountryName.setError(null);

    Notification notification = new Notification();
    notification.put(Notification.MESSAGE_TITLE, mMessageTitle.getEditText().getText().toString().toUpperCase().trim());
    notification.put(Notification.MESSAGE_BODY, mMessageBody.getEditText().getText().toString().trim());
    notification.put(Notification.MESSAGE_TYPE, Notification.NOTIFY_TYPE_COUNTRY_NAME);
    notification.put(Notification.COUNTRY_NAME, country_name.toUpperCase());

    FirebaseService
      .sendNotification(notification)
      .addOnSuccessListener(new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void aVoid) {
          startActivity(NotifySuccessActivity.createIntent(NotifyCountryActivity.this));
          finishAffinity();
        }
      })
      .addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
          Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
        }
      });
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
    country_name = parent.getItemAtPosition(i).toString();
  }
}