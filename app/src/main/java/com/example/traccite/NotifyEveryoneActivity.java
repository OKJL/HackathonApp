package com.example.traccite;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.traccite.models.Notification;
import com.example.traccite.services.FirebaseService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;

public class NotifyEveryoneActivity
  extends AppCompatActivity
  implements View.OnClickListener {

  private static final String TAG = "NotifyEveryoneActivity";

  private TextInputLayout mMessageTitle;
  private TextInputLayout mMessageBody;
  private Button mSendNotification;

  @NonNull
  public static Intent createIntent(@NonNull Context context) {
    return new Intent(context, NotifyEveryoneActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_notify_everyone);

    mMessageTitle = findViewById(R.id.message_title);
    mMessageBody = findViewById(R.id.message_body);
    mSendNotification = findViewById(R.id.btn_send);

    mSendNotification.setOnClickListener(this);

    mMessageTitle.requestFocus();
  }

  @Override
  public void onClick(View view) {
    if (mMessageTitle.getEditText().getText().toString().trim().isEmpty()) {
      mMessageTitle.setError("Message Title is empty!");
      return;
    }

    if (mMessageTitle.getEditText().getText().length() > 40) {
      mMessageTitle.setError("Message Title is too long!");
      return;
    }

    mMessageTitle.setError(null);

    if (mMessageBody.getEditText().getText().toString().trim().isEmpty()) {
      mMessageBody.setError("Message Body is empty!");
      return;
    }

    if (mMessageBody.getEditText().getText().length() > 500) {
      mMessageBody.setError("Message Body is too long!");
      return;
    }

    mMessageBody.setError(null);

    Toast.makeText(this, "Sending...", Toast.LENGTH_LONG).show();

    Notification notification = new Notification();
    notification.put(Notification.MESSAGE_TITLE, mMessageTitle.getEditText().getText().toString().toUpperCase().trim());
    notification.put(Notification.MESSAGE_BODY, mMessageBody.getEditText().getText().toString().trim());
    notification.put(Notification.MESSAGE_TYPE, Notification.NOTIFY_TYPE_EVERYONE);

    FirebaseService
      .sendNotification(notification)
      .addOnSuccessListener(new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void aVoid) {
          startActivity(NotifySuccessActivity.createIntent(NotifyEveryoneActivity.this));
          finishAffinity();
        }
      })
      .addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
          Toast.makeText(
            getApplicationContext(),
            "Failed to send notification",
            Toast.LENGTH_LONG
          ).show();
        }
      });
  }
}