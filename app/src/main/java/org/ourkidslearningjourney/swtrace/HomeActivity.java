package org.ourkidslearningjourney.swtrace;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.ourkidslearningjourney.swtrace.services.FirebaseService;
import org.ourkidslearningjourney.swtrace.services.PreferencesService;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

  private static SharedPreferences sPreferences;
  private Button mBtnLogout;

  @NonNull
  public static Intent createIntent(@NonNull Context context) {
    return new Intent(context, HomeActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

    sPreferences = getApplication().getSharedPreferences(
      PreferencesService.GLOBAL_PREFERENCES,
      MODE_PRIVATE
    );

    mBtnLogout = findViewById(R.id.btn_logout);
    mBtnLogout.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    sPreferences.edit().putBoolean(PreferencesService.SETUP_COMPLETED_KEY, false).apply();
    FirebaseService.signOut();
    finishAffinity();
  }
}