/*
 * Copyright (c) 2020 SWTrace Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.ourkidslearningjourney.swtrace.activities;

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

import org.ourkidslearningjourney.swtrace.R;
import org.ourkidslearningjourney.swtrace.models.User;
import org.ourkidslearningjourney.swtrace.services.FCMService;
import org.ourkidslearningjourney.swtrace.services.FirebaseService;
import org.ourkidslearningjourney.swtrace.PreferenceConstants;

import java.util.Arrays;

public class SetupActivity extends AppCompatActivity implements View.OnClickListener {

  /*
   * Logcat: Logging Tag
   */
  private static final String TAG = "SetupActivity";

  /*
   * SharedPreferences: Preference Manager
   */
  private static SharedPreferences sPreferences;

  /*
   * Layout: Widgets
   */
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

    /*
     * Set the list of countries
     */
    ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(
      this,
      R.layout.partial_dropdown_item,
      getResources().getStringArray(R.array.countries)
    );

    /*
     * Set the preference manager
     */
    sPreferences = getApplicationContext().getSharedPreferences(
      PreferenceConstants.PREF_GLOBAL,
      MODE_PRIVATE
    );

    /*
     * Set the layout reference
     */
    mTxtNRIC = findViewById(R.id.txt_nric);
    mTxtFullName = findViewById(R.id.txt_full_name);
    mTxtCountryName = findViewById(R.id.txt_country_name);
    mTxtContactNumber = findViewById(R.id.txt_contact_number);
    mBtnNext = findViewById(R.id.btn_next);

    /*
     * Set the list of countries into the view
     */
    mTxtCountryName.setAdapter(countryAdapter);

    /*
     * Check if a phone number is attached to the account
     */
    if (FirebaseService.getCurrentUser().getPhoneNumber() != null) {

      /*
       * Set the phone number into the view and disable the widget
       */
      mTxtContactNumber.getEditText().setText(FirebaseService.getCurrentUser().getPhoneNumber());
      mTxtContactNumber.setEnabled(false);
    }

    /*
     * Set onClick listener override
     */
    mBtnNext.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {

    /*
     * Check if the NRIC field is empty
     */
    if (mTxtNRIC.getEditText().getText().toString().trim().isEmpty()) {
      mTxtNRIC.setError("NRIC or FIN or Passport must not be blank!");
      return;
    }

    /*
     * Reset the error status
     */
    mTxtNRIC.setError(null);

    /*
     * Check if the Full Name field is empty
     */
    if (mTxtFullName.getEditText().getText().toString().trim().isEmpty()) {
      mTxtFullName.setError("Full Name must not be blank!");
      return;
    }

    /*
     * Reset the error status
     */
    mTxtFullName.setError(null);

    /*
     * Check if the Country Name field is empty
     */
    if (mTxtCountryName.getText().toString().trim().isEmpty()) {
      mTxtCountryName.setError("Country Name must not be blank!");
      return;
    }

    /*
     * Check if the Country Name value is inside our string-array
     * resource
     */
    if (
      !Arrays.asList(
        getResources().getStringArray(R.array.countries)
      ).contains(
        mTxtCountryName.getText().toString().trim())
    ) {
      mTxtCountryName.setError("Country Name is invalid!");
      return;
    }

    /*
     * Reset the error status
     */
    mTxtCountryName.setError(null);

    /*
     * Check if the Contact Number value is empty
     */
    if (mTxtContactNumber.getEditText().getText().toString().trim().isEmpty()) {
      mTxtContactNumber.setError("Contact Number must not be blank!");
      return;
    }

    /*
     * Reset the error status
     */
    mTxtContactNumber.setError(null);

    /*
     * Create a new user object for serialization into Firestore
     */
    User user = new User();
    user.put(User.NRIC_FIN_PPT, mTxtNRIC.getEditText().getText().toString().trim().toUpperCase());
    user.put(User.FULL_NAME, mTxtFullName.getEditText().getText().toString().trim().toUpperCase());
    user.put(User.COUNTRY_NAME, mTxtCountryName.getText().toString().trim().toUpperCase());
    user.put(User.CONTACT_NUMBER, mTxtContactNumber.getEditText().getText().toString().trim());
    user.put(User.FCM_TOKENS, FieldValue.arrayUnion(FCMService.getFCMToken(this)));

    /*
     * Subscribe the current device into ALL_USERS topic
     */
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

    /*
     * Subscribe the current device into their respective country topic
     */
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

    /*
     * Set user data into Firestore and listen for success/failure
     */
    FirebaseService
      .setUsersCollection(FirebaseService.getCurrentUser().getUid(), user)
      .addOnSuccessListener(new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void aVoid) {
          sPreferences.edit().putBoolean(PreferenceConstants.PREF_SETUP_COMPLETED, true).apply();

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