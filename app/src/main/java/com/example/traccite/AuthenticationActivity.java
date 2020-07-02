package com.example.traccite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AuthenticationActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_authentication);

    // Firebase: Arbitrary request code value
    final int RC_SIGN_IN = 123456789;
    final List<String> whitelistedCountries = new ArrayList<String>();
    whitelistedCountries.add("SG");

    startActivityForResult(
      AuthUI.getInstance()
        .createSignInIntentBuilder()
        .setAvailableProviders(
          Arrays.asList(
          new AuthUI.IdpConfig.PhoneBuilder()
            .setWhitelistedCountries(whitelistedCountries)
            .setDefaultCountryIso("sg")
            .build()
          )
        )
        .setIsSmartLockEnabled(!BuildConfig.DEBUG)
        .build(),
      RC_SIGN_IN);
  }
}