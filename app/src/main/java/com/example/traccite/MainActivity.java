package com.example.traccite;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

  // Logcat: Logging Tag
  private static final String TAG = "MainActivity";

  // Firebase: Arbitrary Request Code
  private static final int RC_SIGN_IN = 1422;

  // Firebase: Phone Auth Default Number
  private static final String DEFAULT_NUMBER = "+6531235617";

  // Firebase: Privacy Policy & Terms Of Service
  private static final String TOS_URL = "https://google.com";
  private static final String PRIVACY_POLICY_URL = "https://ite.edu.sg";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    SharedPreferences preferences = getApplicationContext().
      getSharedPreferences(AppTraCCite.GLOBAL_PREFS, MODE_PRIVATE);

    // TODO: Remove in production.
    AuthUI.getInstance().signOut(this);

    if (currentUser == null) {
      createSignInIntent();
      return;
    }

    if (!preferences.getBoolean(AppTraCCite.SETUP_COMPLETED_KEY, false)) {
      startActivity(SetupActivity.createIntent(this));
      finish();
      return;
    }

    startActivity(HomeActivity.createIntent(this));
    finish();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == RC_SIGN_IN) {
      IdpResponse response = IdpResponse.fromResultIntent(data);

      if (resultCode == RESULT_OK) {
        Log.i(TAG, "Signed in as: " + response.getPhoneNumber());
        startActivity(SetupActivity.createIntent(this));
        finish();
      } else {
        Log.i(TAG, "Unable to sign in");
      }
    }
  }

  public void createSignInIntent() {
    List<String> countriesEnabled = Arrays.asList("SG");

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
        .setTosAndPrivacyPolicyUrls(TOS_URL, PRIVACY_POLICY_URL)
        .setIsSmartLockEnabled(!BuildConfig.DEBUG)
        .build(),
      RC_SIGN_IN
    );
  }
}