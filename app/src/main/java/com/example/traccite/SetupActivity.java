package com.example.traccite;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.traccite.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

public class SetupActivity extends AppCompatActivity {

  // Logcat: Logging Tag
  private static final String TAG = "SetupActivity";

  // Android: Fields From Layout
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

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_setup);

    // Firebase: Get The Firestore Instance
    final FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Firebase: Get Current Authenticated User
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    // SharedPreferences: Editor for setting values
    final SharedPreferences.Editor editor = getApplicationContext()
      .getSharedPreferences(AppTraCCite.PREF_SETUP, MODE_PRIVATE).edit();

    // SharedPreferences: Reset Key-Value pair
    editor.putBoolean(AppTraCCite.SETUP_KEY, false);
    editor.apply();

    // Android: Linking variables to layout ids
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
    if (!user.getPhoneNumber().isEmpty()) {
      mContactNumber.setText(user.getPhoneNumber());
      mContactNumber.setEnabled(false);
    }

    // Android: Attach an onClick event listener
    mContinue.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
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

        User user_data = new User(
          user.getUid(),
          mNricFin.getText().toString(),
          mFullName.getText().toString(),
          null,
          mContactNumber.getText().toString(),
          mResidentOfSingapore.isChecked()
          );

        db.collection("users")
          .document(user.getUid())
          .set(user_data, SetOptions.merge())
          .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
              showToast("Successfully updated your profile.");

              editor.putBoolean(AppTraCCite.SETUP_KEY, true);
              editor.commit();

              startActivity(HomeActivity.createIntent(SetupActivity.this));
              finish();
            }
          })
          .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
              showToast("Failed to update your profile.");
            }
          });
      }
    });
  }
}