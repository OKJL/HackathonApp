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

package org.ourkidslearningjourney.swtrace;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;

import org.ourkidslearningjourney.swtrace.services.BeaconScanningService;
import org.ourkidslearningjourney.swtrace.services.FirebaseService;
import org.ourkidslearningjourney.swtrace.services.PermissionService;
import org.ourkidslearningjourney.swtrace.services.PreferencesService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity
  extends AppCompatActivity
  implements FirebaseAuth.AuthStateListener {

  /*
   * Logcat: Logging Tag
   */
  private static final String TAG = "MainActivity";

  /*
   * FirebaseUI: Request Code
   */
  private static final int RC_SIGN_IN = 1422;

  /*
   * FirebaseUI: Default Phone Number
   */
  private static final String DEFAULT_NUMBER = "+6531235617";

  /*
   * FirebaseUI: Terms Of Service and Privacy Policy URLs
   */
  private static final String TOS_URL = "https://ite.edu.sg";
  private static final String PRIVACY_POLICY_URL = "https://ite.edu.sg";

  /*
   * BluetoothAdapter: Bluetooth Adapter
   */
  private static BluetoothAdapter sBluetoothAdapter;

  /*
   * SharedPreferences: Preference Manager
   */
  private static SharedPreferences sSharedPreferences;

  @NonNull
  public static Intent createIntent(@NonNull Context context) {
    return new Intent(context, MainActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    /*
     * Set the bluetooth adapter
     */
    sBluetoothAdapter = android.bluetooth.BluetoothAdapter.getDefaultAdapter();

    /*
     * Set the preference manager
     */
    sSharedPreferences = getApplicationContext().getSharedPreferences(
      PreferencesService.GLOBAL_PREFERENCES,
      MODE_PRIVATE
    );

    /*
     * Delay for 500ms and start listening for Firebase auth status
     */
    new Timer().schedule(new TimerTask() {
      @Override
      public void run() {
        FirebaseAuth.getInstance().addAuthStateListener(MainActivity.this);
      }
    }, 500);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();

    /*
     * Remove Firebase auth status listener
     */
    FirebaseAuth.getInstance().removeAuthStateListener(this);
  }

  @Override
  public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

    /*
     * Check if the user is currently logged in
     */
    if (FirebaseService.getCurrentUser() == null) {
      createSignInIntent();
      return;
    }

    /*
     * Check if the user has completed their setup process
     */
    if (!sSharedPreferences.getBoolean(PreferencesService.SETUP_COMPLETED_KEY, false)) {
      startActivity(SetupActivity.createIntent(this));
      finishAffinity();
      return;
    }

    /*
     * Check if Bluetooth is currently turned on
     */
    if (!sBluetoothAdapter.isEnabled()) {
      startActivity(SetupPermissions.createIntent(this));
      finishAffinity();
      return;
    }

    /*
     * Check if all the permissions required are currently enabled
     */
    if (!PermissionService.hasPermissions(this, PermissionService.PERMISSIONS)) {
      startActivity(SetupPermissions.createIntent(this));
      finishAffinity();
      return;
    }

    /*
     * Start beacon scanning service
     */
    ContextCompat.startForegroundService(
      this,
      BeaconScanningService.createIntent(this)
    );

    /*
     * Start HomeActivity if all checks passed
     */
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
    /*
     * Set the list of available countries
     */
    List<String> countriesEnabled = Collections.singletonList("SG");

    /*
     * Set the list of auth service providers
     */
    List<AuthUI.IdpConfig> providers = Arrays.asList(
      new AuthUI.IdpConfig.EmailBuilder().build(),
      new AuthUI.IdpConfig.PhoneBuilder()
        .setDefaultNumber(DEFAULT_NUMBER)
        .setWhitelistedCountries(countriesEnabled)
        .build()
    );

    /*
     * Start FirebaseUI activity
     */
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