package org.ourkidslearningjourney.swtrace;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import org.ourkidslearningjourney.swtrace.services.FirebaseService;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

  private Button mBtnLogout;

  @NonNull
  public static Intent createIntent(@NonNull Context context) {
    return new Intent(context, HomeActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

    mBtnLogout = findViewById(R.id.btn_logout);
    mBtnLogout.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    FirebaseService.signOut();
  }
}