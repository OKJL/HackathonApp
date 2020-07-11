package com.example.traccite;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.traccite.models.User;
import com.example.traccite.services.FCMService;
import com.example.traccite.services.NRICService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rilixtech.CountryCodePicker;

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
  private TextInputLayout mNricFin;
  private TextInputLayout mFullName;
  private TextInputLayout mContactNumber;
  private Spinner mCountry;
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
        if (mNricFin.getEditText().getText().toString().isEmpty()) {
          mNricFin.setError("NRIC/FIN is empty!");
          return;
        }

        if (mNricFin.getEditText().getText().length() != 9) {
          mNricFin.setError("NRIC/FIN is invalid!");
          return;
        }

        if (
          !NRICService.checkNRIC(
            mNricFin.getEditText()
              .getText()
              .toString()
              .toUpperCase()
          )
        ) {
          mNricFin.setError("NRIC/FIN is invalid!");
          return;
        }

        /*
         * Reset the error message
         */
        mNricFin.setError(null);

        if (mFullName.getEditText().getText().toString().isEmpty()) {
          mFullName.setError("Full Name is empty!");
          return;
        }

        /*
         * Reset the error message
         */
        mFullName.setError(null);

        if (mContactNumber.getEditText().getText().toString().isEmpty()) {
          mContactNumber.setError("Contact Number is empty!");
          return;
        }

        /*
         * Reset the error message
         */
        mContactNumber.setError(null);

        /*
         * Create the user data structure
         */
        User user = new User();
        user.put(User.UID, mUser.getUid());
        user.put(User.NRIC_FIN_PPT, mNricFin.getEditText().getText().toString().toUpperCase());
        user.put(User.FULL_NAME, mFullName.getEditText().getText().toString().toUpperCase());
        user.put(User.CONTACT_NUMBER, mContactNumber.getEditText().getText().toString());
        user.put(User.COUNTRY_NAME, mCountry.getSelectedItem());
        user.put(User.FCM_TOKENS, FieldValue.arrayUnion(FCMService.getToken(SetupActivity.this)));

        /*
         * Subscribe user to topic
         *
         * TODO: Replace "SINGAPORE" with actual countries
         */
        FirebaseMessaging
          .getInstance()
          .subscribeToTopic("SINGAPORE")
          .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
              if (!task.isSuccessful()) {
                showToast("Failed to subscribe to topic!");
              }

              showToast("Successfully subscribed to topic!");
            }
          });

        /*
         * Upload the data into Firestore
         */
        mDb.document("users/" + mUser.getUid())
          .set(user.retrieve(), SetOptions.merge())
          .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
              showToast("Successfully updated your profile.");

              editor.putBoolean(AppTraCCite.SETUP_COMPLETED_KEY, true);
              editor.commit();

              startActivity(HomeActivity.createIntent(SetupActivity.this));
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
    String[] aCountries = new String[] {
      "Afghanistan",
      "Albania",
      "Algeria",
      "Andorra",
      "Angola",
      "Antigua and Barbuda",
      "Argentina" ,
      "Armenia",
      "Australia",
      "Austria",
      "Azerbaijan",
      "Bahamas",
      "Bahrain",
      "Bangladesh",
      "Barbados",
      "Belarus",
      "Belgium",
      "Belize",
      "Benin",
      "Bhutan",
      "Bolivia",
      "Bosnia and Herzegovina",
      "Botswana",
      "Brazil",
      "Brunei",
      "Bulgaria",
      "Burkina Faso",
      "Burundi",
      "Cabo Verde",
      "Cambodia",
      "Cameroon",
      "Canada",
      "Central African Republic (CAR)",
      "Chad",
      "Chile",
      "China",
      "Colombia",
      "Comoros",
      "Congo, Democratic Republic of the Congo, Republic of the Costa Rica",
      "Cote d'Ivoire",
      "Croatia",
      "Cuba",
      "Cyprus",
      "Czechia",
      "Denmark",
      "Djibouti",
      "Dominica",
      "Dominican Republic",
      "Ecuador",
      "Egypt",
      "El Salvador",
      "Equatorial Guinea",
      "Eritrea",
      "Estonia",
      "Eswatini",
      "Ethiopia",
      "Fiji",
      "Finland",
      "France",
      "Gabon",
      "Gambia",
      "Georgia",
      "Germany",
      "Ghana",
      "Greece",
      "Grenada" ,
      "Guatemala",
      "Guinea",
      "Guinea-Bissau",
      "Haiti",
      "Honduras",
      "Hungary",
      "Iceland",
      "India",
      "Indonesia",
      "Iran",
      "Iraq",
      "Ireland",
      "Israel",
      "Italy",
      "Jamaica",
      "Japan",
      "Jordan",
      "Kazakhstan",
      "Kenya",
      "Kiribati",
      "Kosovo",
      "Kuwait",
      "Kyrgyzstan",
      "Dhekelia",
      "Laos",
      "Latvia",
      "Lebanon",
      "Lesotho",
      "Liberia",
      "Libya",
      "Liechtenstein",
      "Lithuania",
      "Luxembour",
      "Madagascar",
      "Malawi",
      "Malaysia",
      "Maldives",
      "Mali",
      "Malta",
      "Marshall Islands",
      "Mauritania",
      "Mauritius",
      "Mexico",
      "Micronesia",
      "Moldova",
      "Monaco",
      "Mongolia",
      "Montenegro",
      "Morocco",
      "Mozambique",
      "Myanmar",
      "Namibia",
      "Nauru",
      "Nepal",
      "Netherlands",
      "New Zealand",
      "Nicaragua",
      "Niger",
      "Nigeria",
      "North Korea",
      "North Macedonia",
      "Norway",
      "Ecuador",
      "Oman",
      "Pakistan",
      "Palau",
      "Palestine",
      "Panama",
      "Papua New Guinea",
      "Paraguay",
      "Peru",
      "Philippines",
      "Poland",
      "Portugal",
      "Qatar",
      "Romania",
      "Russia",
      "Rwanda",
      "Saint Kitts and Nevis",
      "Saint Lucia",
      "Saint Vincent and the Grenadines",
      "Samoa",
      "San Marino",
      "Sao Tome and Principe",
      "Saudi Arabia",
      "Senegal",
      "Serbia",
      "Seychelles",
      "Sierra Leone",
      "Singapore",
      "Slovakia",
      "Slovenia",
      "Solomon Islands",
      "Somalia",
      "South Africa",
      "South Korea",
      "South Sudan",
      "Spain",
      "Sri Lanka",
      "Sudan",
      "Suriname",
      "Sweden",
      "Switzerland",
      "Syria",
      "Taiwan",
      "Tajikistan",
      "Tanzania",
      "Thailand",
      "Timor-Leste",
      "Togo",
      "Tonga",
      "Trinidad and Tobago",
      "Tunisia",
      "Turkey",
      "Turkmenistan",
      "Tuvalu",
      "Europa Island",
      "Uganda",
      "Ukraine",
      "United Arab Emirates (UAE)",
      "United Kingdom (UK)",
      "United States of America (USA)",
      "Uruguay",
      "Uzbekistan",
      "Vanuatu",
      "Vatican City",
      "Venezuela",
      "Vietnam",
      "Fiji",
      "Yemen",
      "Zambia",
      "Zimbabwe"
    };
    mCountry = (Spinner) findViewById(R.id.residency);
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
      android.R.layout.simple_spinner_item, aCountries);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    mCountry.setAdapter(adapter);


    // TODO: add into firebase
    CountryCodePicker ccp = (CountryCodePicker) findViewById(R.id.ccp);

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
    mContinue = findViewById(R.id.continue_button);

    /*
     * Firebase: Checks if the current user has a
     * phone number linked to their account.
     *
     * If there is a phone number present, automatically
     * set the phone number to the Contact Number field
     * in the layout and disable any inputs from it.
     */
    if (!mUser.getPhoneNumber().isEmpty()) {
      mContactNumber.getEditText().setText(mUser.getPhoneNumber());
      mContactNumber.setEnabled(false);
    }

    /*
     * Android: Listen for onClick events
     */
    listenForOnClick(mEditor);
  }
}