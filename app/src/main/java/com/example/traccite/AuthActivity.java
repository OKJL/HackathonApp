package com.example.traccite;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;

import java.util.Arrays;
import java.util.List;

public class AuthActivity extends AppCompatActivity {
  // Firebase: Random arbitrary request code
  private static final int RC_SIGN_IN = 1422;

  // Logcat: Used for logging
  private static final String TAG = "AuthActivity";

  // Firebase: Default phone number
  // TODO: Remove in production.
  private static final String DEFAULT_NUMBER = "+6531235617";

  // Firebase: Privacy Policy & Terms Of Service
  private static final String TOS_URL = "https://google.com";
  private static final String PRIVACY_POLICY_URL = "https://ite.edu.sg";

  @NonNull
  public static Intent createIntent(@NonNull Context context) {
    return new Intent(context, AuthActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_auth);

    // Show the login options
    createSignInIntent();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == RC_SIGN_IN) {
      IdpResponse response = IdpResponse.fromResultIntent(data);

      if (resultCode == RESULT_OK) {
        Log.d(TAG, "Successfully signed in as: " + response.getPhoneNumber());
      } else {
        Log.d(TAG, "Unable to sign in");
      }
    }
  }

  public void createSignInIntent() {
    List<String> countriesEnabled = Arrays.asList(
      "SG"
    );

    List<AuthUI.IdpConfig> providers = Arrays.asList(
      new AuthUI.IdpConfig.EmailBuilder().build(),
      new AuthUI.IdpConfig.PhoneBuilder()
        .setDefaultNumber(DEFAULT_NUMBER)
        .setWhitelistedCountries(countriesEnabled)
        .build()
    );

    startActivityForResult(
      AuthUI.getInstance()
        .createSignInIntentBuilder()
        .setAvailableProviders(providers)
        .setLogo(R.drawable.common_google_signin_btn_icon_dark_focused)
        .setTheme(R.style.AuthTheme)
        .setTosAndPrivacyPolicyUrls(
          TOS_URL,
          PRIVACY_POLICY_URL
        )
        .setIsSmartLockEnabled(!BuildConfig.DEBUG)
        .build(),
      RC_SIGN_IN
    );
  }
}