package org.ourkidslearningjourney.swtrace.services;

import android.app.Application;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Map;

public class FirebaseService {
  private static final String TAG = "FirebaseService";

  private static FirebaseAuth sUser;
  private static FirebaseFirestore sDb;

  public static void init() {
    sUser = FirebaseAuth.getInstance();
    sDb = FirebaseFirestore.getInstance();
  }

  public static FirebaseUser getCurrentUser() {
    return sUser.getCurrentUser();
  }

  public static CollectionReference getUsersCollection() {
    return sDb.collection("users");
  }

  public static Task<Void> setUsersCollection(Map<String, Object> data) {
    return getUsersCollection()
      .document()
      .set(data, SetOptions.merge());
  }

  public static Task<Void> setUsersCollection(String doc, Map<String, Object> data) {
    return getUsersCollection()
      .document(doc)
      .set(data, SetOptions.merge());
  }
}
