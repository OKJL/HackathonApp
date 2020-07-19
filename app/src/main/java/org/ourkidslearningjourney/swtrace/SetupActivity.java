package org.ourkidslearningjourney.swtrace;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FieldValue;

import org.ourkidslearningjourney.swtrace.models.User;
import org.ourkidslearningjourney.swtrace.services.FCMService;
import org.ourkidslearningjourney.swtrace.services.FirebaseService;
import org.ourkidslearningjourney.swtrace.services.PreferencesService;

import java.util.Arrays;

public class SetupActivity extends AppCompatActivity implements View.OnClickListener {

  private static final String TAG = "SetupActivity";

  private static SharedPreferences sPreferences;

  private TextInputLayout mTxtNRIC;
  private TextInputLayout mTxtFullName;
  private TextInputLayout mTxtContactNumber;
  private AutoCompleteTextView mTxtCountryName;
  private MaterialButton mBtnNext;

  @NonNull
  public static Intent createIntent(@NonNull Context context) {
    return new Intent(context, SetupActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_setup);

    ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(
      this,
      R.layout.dropdown_item,
      getResources().getStringArray(R.array.countries)
    );

    sPreferences = getApplicationContext().getSharedPreferences(
      PreferencesService.GLOBAL_PREFERENCES,
      MODE_PRIVATE
    );

    mTxtNRIC = findViewById(R.id.txt_nric);
    mTxtFullName = findViewById(R.id.txt_full_name);
    mTxtCountryName = findViewById(R.id.txt_country_name);
    mTxtContactNumber = findViewById(R.id.txt_contact_number);
    mBtnNext = findViewById(R.id.btn_next);

    mTxtCountryName.setAdapter(countryAdapter);

    if (FirebaseService.getCurrentUser().getPhoneNumber() != null) {
      mTxtContactNumber.getEditText().setText(FirebaseService.getCurrentUser().getPhoneNumber());
      mTxtContactNumber.setEnabled(false);
    }

    mBtnNext.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    if (mTxtNRIC.getEditText().getText().toString().trim().isEmpty()) {
      mTxtNRIC.setError("NRIC or FIN or Passport must not be blank!");
      return;
    }

    mTxtNRIC.setError(null);

    if (mTxtFullName.getEditText().getText().toString().trim().isEmpty()) {
      mTxtFullName.setError("Full Name must not be blank!");
      return;
    }

    mTxtFullName.setError(null);

    if (mTxtCountryName.getText().toString().trim().isEmpty()) {
      mTxtCountryName.setError("Country Name must not be blank!");
      return;
    }

    if (
      !Arrays.asList(
        getResources().getStringArray(R.array.countries)
      ).contains(
        mTxtCountryName.getText().toString().trim())
    ) {
      mTxtCountryName.setError("Country Name is invalid!");
      return;
    }

    mTxtCountryName.setError(null);

    if (mTxtContactNumber.getEditText().getText().toString().trim().isEmpty()) {
      mTxtContactNumber.setError("Contact Number must not be blank!");
      return;
    }

    mTxtContactNumber.setError(null);

    User user = new User();
    user.put(User.NRIC_FIN_PPT, mTxtNRIC.getEditText().getText().toString().trim().toUpperCase());
    user.put(User.FULL_NAME, mTxtFullName.getEditText().getText().toString().trim().toUpperCase());
    user.put(User.COUNTRY_NAME, mTxtCountryName.getText().toString().trim().toUpperCase());
    user.put(User.CONTACT_NUMBER, mTxtContactNumber.getEditText().getText().toString().trim());
    user.put(User.FCM_TOKENS, FieldValue.arrayUnion(FCMService.getFCMToken(this)));
    user.put(User.UPDATED_AT, FieldValue.serverTimestamp());

    FCMService.subscribeToTopic("ALL_USERS")
      .addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
          if (!task.isSuccessful()) {
            Toast.makeText(getApplicationContext(), "Failed to subscribe to ALL_USERS topic!", Toast.LENGTH_LONG).show();
          }

          Toast.makeText(getApplicationContext(), "Successfully subscribed to ALL_USERS topic!", Toast.LENGTH_LONG).show();
        }
      });

    FCMService.subscribeToTopic(mTxtCountryName.getText().toString().trim().toUpperCase())
      .addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
          if (!task.isSuccessful()) {
            Toast.makeText(getApplicationContext(), "Failed to subscribed to country topic!", Toast.LENGTH_LONG).show();
          }

          Toast.makeText(getApplicationContext(), "Successfully subscribed to country topic!", Toast.LENGTH_LONG).show();
        }
      });

    FirebaseService
      .setUsersCollection(FirebaseService.getCurrentUser().getUid(), user)
      .addOnSuccessListener(new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void aVoid) {
          sPreferences.edit().putBoolean(PreferencesService.SETUP_COMPLETED_KEY, true).apply();

          startActivity(MainActivity.createIntent(getApplicationContext()));
          finishAffinity();
        }
      })
      .addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
          Toast.makeText(getApplicationContext(), "Failed to update profile", Toast.LENGTH_LONG).show();

          Log.e(TAG, "Updated Profile Error: " + e.getMessage());
        }
      });
  }
}