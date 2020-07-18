package org.ourkidslearningjourney.swtrace;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;

import org.ourkidslearningjourney.swtrace.services.FirebaseService;
import org.ourkidslearningjourney.swtrace.services.PreferencesService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity
  extends AppCompatActivity
  implements FirebaseAuth.AuthStateListener {

  private static final String TAG = "MainActivity";

  private static final int RC_SIGN_IN = 1422;

  private static final String DEFAULT_NUMBER = "+6531235617";

  private static final String TOS_URL = "https://ite.edu.sg";
  private static final String PRIVACY_POLICY_URL = "https://ite.edu.sg";

  private static SharedPreferences sSharedPreferences;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    sSharedPreferences = getApplicationContext().getSharedPreferences(
      PreferencesService.GLOBAL_PREFERENCES,
      MODE_PRIVATE
    );
  }

  @Override
  protected void onStart() {
    super.onStart();
    FirebaseAuth.getInstance().addAuthStateListener(this);
  }

  @Override
  public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
    if (FirebaseService.getCurrentUser() == null) {
      createSignInIntent();
      return;
    }

    startActivity(HomeActivity.createIntent(this));
    finishAffinity();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == RC_SIGN_IN) {
      IdpResponse response = IdpResponse.fromResultIntent(data);

      if (resultCode == RESULT_OK) {
        Log.i(TAG, "Signed In As: " + response.getPhoneNumber());
        return;
      }

      Toast.makeText(this, "Failed To Sign In", Toast.LENGTH_LONG).show();
      Log.e(TAG, "Failed To Sign In");
    }
  }

  private void createSignInIntent() {
    List<String> countriesEnabled = Collections.singletonList("SG");

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
        .setLogo(R.mipmap.ic_launcher)
        .setTheme(R.style.AuthTheme)
        .setTosAndPrivacyPolicyUrls(TOS_URL, PRIVACY_POLICY_URL)
        .setIsSmartLockEnabled(!BuildConfig.DEBUG)
        .build(),
      RC_SIGN_IN
    );
  }
}