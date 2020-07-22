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
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.ourkidslearningjourney.swtrace.services.PermissionService;

public class SetupPermissions extends AppCompatActivity implements View.OnClickListener {

  /*
   * Layout: Widgets
   */
  private Button mBtnEnableBluetooth;

  @NonNull
  public static Intent createIntent(@NonNull Context context) {
    return new Intent(context, SetupPermissions.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_setup_permissions);

    /*
     * Set layout reference
     */
    mBtnEnableBluetooth = findViewById(R.id.btn_enable_bluetooth);

    /*
     * Set onClick listener override
     */
    mBtnEnableBluetooth.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    /*
     * Request Bluetooth enable
     */
    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
    startActivityForResult(intent, PermissionService.RC_PERMISSIONS);

    /*
     * Request Location Services
     */
    ActivityCompat.requestPermissions(
      this,
      PermissionService.PERMISSIONS,
      PermissionService.RC_PERMISSIONS);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    /*
     * Check if the requestCode matches and if the resultCode is OK
     */
    if (requestCode == PermissionService.RC_PERMISSIONS && resultCode == RESULT_OK) {

      /*
       * Check if all the permissions are granted
       */
      if (PermissionService.hasPermissions(this, PermissionService.PERMISSIONS)) {
        startActivity(HomeActivity.createIntent(this));
        finishAffinity();
      }
    }
  }

}