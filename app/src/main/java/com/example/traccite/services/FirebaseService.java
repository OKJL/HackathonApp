package com.example.traccite.services;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Map;

public class FirebaseService {

  /*
   * Logcat: Logging Tag
   */
  private static final String TAG = "FirebaseService";

  /*
   * Firebase: Current Auth Instance
   */
  private static FirebaseAuth mUser;

  /*
   * Firebase: Current Firestore Instance
   */
  private static FirebaseFirestore mDb;

  /*
   * A workaround to the onCreate method not being called
   */
  public static void initializeService() {
    /*
     * Set the current Firebase Auth instance
     */
    mUser = FirebaseAuth.getInstance();

    /*
     * Set the current Firestore instance
     */
    mDb = FirebaseFirestore.getInstance();
  }

  /*
   * Get the current Firebase user
   */
  public static FirebaseUser getCurrentUser() {
    return mUser.getCurrentUser();
  }

  /*
   * Get the users collection from Firestore
   */
  public static CollectionReference getUsersCollection() {
    return mDb.collection("users");
  }

  public static CollectionReference getNotifiersCollection() {
    return mDb.collection("notifiers");
  }

  /*
   * Set data into the users collection
   */
  public static Task<Void> setUsersCollection(String doc, Map<String, Object> data) {
    return getUsersCollection()
      .document(doc)
      .set(data, SetOptions.merge());
  }

  public static Task<Void> sendNotification(Map<String, Object> data) {
    return getNotifiersCollection()
      .document()
      .set(data, SetOptions.merge());
  }
}
