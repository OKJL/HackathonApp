package org.ourkidslearningjourney.swtrace;

import android.app.Application;

import org.ourkidslearningjourney.swtrace.services.FirebaseService;

public class SWTrace extends Application {
  @Override
  public void onCreate() {
    super.onCreate();

    FirebaseService.init();
  }
}
