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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class SetupActivity extends AppCompatActivity {

  // Logcat: Logging Tag
  private static final String TAG = "SetupActivity";

  // Android: Fields From Layout
  private EditText nric_number;
  private EditText full_name;
  private EditText contact_number;
  private Switch resident_of_singapore;
  private Button continue_button;

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
    nric_number = findViewById(R.id.nric_number);
    full_name = findViewById(R.id.full_name);
    contact_number = findViewById(R.id.contact_number);
    resident_of_singapore = findViewById(R.id.resident_of_singapore);
    continue_button = findViewById(R.id.continue_button);

    /*
     * Firebase: Checks if the current user has a
     * phone number linked to their account.
     *
     * If there is a phone number present, automatically
     * set the phone number to the Contact Number field
     * in the layout and disable any inputs from it.
     */
    if (!user.getPhoneNumber().isEmpty()) {
      contact_number.setText(user.getPhoneNumber());
      contact_number.setEnabled(false);
    }

    // Android: Attach an onClick event listener
    continue_button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Map<String, Object> user_data = new HashMap<>();

        if (nric_number.getText().toString().isEmpty()) {
          showToast("NRIC Number is invalid!");
          return;
        }

        if (nric_number.getText().length() != 9) {
          showToast("NRIC Number is invalid!");
          return;
        }

        if (!NRICManager.checkNRICForValidity(nric_number.getText().toString())) {
          showToast("NRIC Number is invalid!");
          return;
        }

        if (full_name.getText().toString().isEmpty()) {
          showToast("Full Name is empty!");
          return;
        }

        if (contact_number.getText().toString().isEmpty()) {
          showToast("Contact Number is empty!");
          return;
        }

        user_data.put("UID", user.getUid());
        user_data.put("NRIC/FIN No.", nric_number.getText().toString());
        user_data.put("Full Name", full_name.getText().toString());
        user_data.put(
          "Contact Number",
          Integer.parseInt(contact_number.getText().toString())
        );
        user_data.put(
          "Resident Of Singapore",
          resident_of_singapore.isChecked()
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