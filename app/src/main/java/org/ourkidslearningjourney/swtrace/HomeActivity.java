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

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.ourkidslearningjourney.swtrace.services.BeaconService;
import org.ourkidslearningjourney.swtrace.services.FirebaseService;
import org.ourkidslearningjourney.swtrace.services.PreferencesService;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

  /*
   * SharedPreferences: Preference Manager
   */
  private static SharedPreferences sPreferences;

  /*
   * Layout: Widgets
   */
  private Button mBtnLogout;

  @NonNull
  public static Intent createIntent(@NonNull Context context) {
    return new Intent(context, HomeActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

    /*
     * Set the preference manager
     */
    sPreferences = getApplication().getSharedPreferences(
      PreferencesService.PREF_GLOBAL,
      MODE_PRIVATE
    );

    /*
     * Set the layout reference
     */
    mBtnLogout = findViewById(R.id.btn_logout);

    /*
     * Set onClick listener override
     */
    mBtnLogout.setOnClickListener(this);

    /*
     * Check if service is already running
     *
     * @bug service will not start upon first installation
     */
    if (!BeaconService.isRunning()) {
      ContextCompat.startForegroundService(this, BeaconService.createIntent(this));
    }
  }

  @Override
  public void onClick(View view) {

    /*
     * Reset the setup completed status
     */
    sPreferences.edit().putBoolean(PreferencesService.PREF_SETUP_COMPLETED, false).apply();

    /*
     * Sign the current user out of Firebase
     */
    FirebaseService.signOut();

    /*
     * Stops beacon monitoring service
     */
    stopService(BeaconService.createIntent(this));

    /*
     * Redirects the activity back to MainActivity
     */
    startActivity(MainActivity.createIntent(this));

    /*
     * Clears all activities
     */
    finishAffinity();
  }
}