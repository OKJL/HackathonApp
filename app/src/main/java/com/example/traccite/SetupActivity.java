package com.example.traccite;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.traccite.models.User;
import com.example.traccite.services.FCMService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class SetupActivity extends AppCompatActivity {

  /*
   * Logcat: Logging Tag
   */
  private static final String TAG = "SetupActivity";

  /*
   * Firebase: Get Firebase Instance
   */
  private final FirebaseFirestore mDb = FirebaseFirestore.getInstance();

  /*
   * Firebase: Get Current Authenticated User
   */
  private final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

  /*
   * Android: Fields From Layout
   */
  private EditText mNricFin;
  private EditText mFullName;
  private EditText mContactNumber;
  private Switch mResidentOfSingapore;
  private Button mContinue;

  @NonNull
  public static Intent createIntent(@NonNull Context context) {
    return new Intent(context, SetupActivity.class);
  }

  private void showToast(String message) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
  }

  private void resetPrefKey(SharedPreferences.Editor editor) {
    /*
     * SharedPreferences: Reset Key-Value pair
     */
    editor.putBoolean(AppTraCCite.SETUP_COMPLETED_KEY, false);
    editor.apply();
  }

  private void listenForOnClick(final SharedPreferences.Editor editor) {
    mContinue.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mNricFin.getText().toString().isEmpty()) {
          showToast("NRIC Number is invalid!");
          return;
        }

        if (mNricFin.getText().length() != 9) {
          showToast("NRIC Number is invalid!");
          return;
        }

        if (!NRICManager.checkNRICForValidity(mNricFin.getText().toString())) {
          showToast("NRIC Number is invalid!");
          return;
        }

        if (mFullName.getText().toString().isEmpty()) {
          showToast("Full Name is empty!");
          return;
        }

        if (mContactNumber.getText().toString().isEmpty()) {
          showToast("Contact Number is empty!");
          return;
        }

        User user = new User(
          mUser.getUid(),
          mNricFin.getText().toString(),
          mFullName.getText().toString(),
          FCMService.getToken(SetupActivity.this),
          mContactNumber.getText().toString(),
          mResidentOfSingapore.isChecked()
        );

        mDb.document("users/" + user.getUid())
          .set(user, SetOptions.merge())
          .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
              showToast("Successfully updated your profile.");

              editor.putBoolean(AppTraCCite.SETUP_COMPLETED_KEY, true);
              editor.commit();

              startActivity(MainActivity.createIntent(SetupActivity.this));
              finish();
            }
          })
          .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
              showToast("Failed to update your profile.");

              Log.e(TAG, "Error Occurred: " + e.getMessage());
            }
          });
      }
    });
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_setup);

    /*
     * SharedPreferences: Editor For Setting Values
     */
    final SharedPreferences.Editor mEditor = getApplicationContext()
      .getSharedPreferences(AppTraCCite.GLOBAL_PREFS, MODE_PRIVATE).edit();

    /*
     * SharedPreferences: Reset Key
     */
    resetPrefKey(mEditor);

    /*
     * Android: Linking variables to layout ids
     */
    mNricFin = findViewById(R.id.nric_number);
    mFullName = findViewById(R.id.full_name);
    mContactNumber = findViewById(R.id.contact_number);
    mResidentOfSingapore = findViewById(R.id.resident_of_singapore);
    mContinue = findViewById(R.id.continue_button);

    /*
     * Firebase: Checks if the current user has a
     * phone number linked to their account.
     *
     * If there is a phone number present, automatically
     * set the phone number to the Contact Number field
     * in the layout and disable any inputs from it.
     */
    if (mUser.getPhoneNumber() != null && !mUser.getPhoneNumber().isEmpty()) {
      mContactNumber.setText(mUser.getPhoneNumber());
      mContactNumber.setEnabled(false);
    }

    /*
     * Android: Listen for onClick events
     */
    listenForOnClick(mEditor);

    FirebaseInstanceId
      .getInstance()
      .getInstanceId()
      .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
        @Override
        public void onComplete(@NonNull Task<InstanceIdResult> task) {
          if (!task.isSuccessful()) {
            Log.e(TAG, "Failed to get FCM instance: " + task.getException());
            return;
          }

          Log.d(TAG, task.getResult().getToken());

          getSharedPreferences(AppTraCCite.GLOBAL_PREFS, MODE_PRIVATE)
            .edit()
            .putString(task.getResult().getToken(), null)
            .apply();
        }
      });
  }
}