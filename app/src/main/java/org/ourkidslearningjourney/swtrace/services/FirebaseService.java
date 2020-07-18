package org.ourkidslearningjourney.swtrace.services;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseService {
  private static FirebaseAuth sAuth;
  private static FirebaseUser sUser;

  public static void init() {
    sAuth = FirebaseAuth.getInstance();
    sUser = sAuth.getCurrentUser();
  }

  public static FirebaseUser getCurrentUser() {
    return sUser;
  }
}
