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

package org.ourkidslearningjourney.swtrace.services;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.firestore.SetOptions;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;

import java.util.Date;
import java.util.Map;

public class FirebaseService {

  public static final long ONE_HOUR = 3600000;

  /*
   * Logcat: Logging Tag
   */
  private static final String TAG = "FirebaseService";

  /*
   * Firebase: Authentication Instance
   */
  private static FirebaseAuth sUser;

  /*
   * Firestore: Firestore Instance
   */
  private static FirebaseFirestore sDb;

  public static void init() {

    /*
     * Set the authentication instance
     */
    sUser = FirebaseAuth.getInstance();

    /*
     * Set the Firestore instance
     */
    sDb = FirebaseFirestore.getInstance();
  }

  /*
   * Signs the current authenticated user out
   */
  public static void signOut() {
    sUser.signOut();
  }

  @NotNull
  @ServerTimestamp
  public static Date getServerTimestamp() {
    return new Date();
  }

  @NotNull
  @ServerTimestamp
  public static Date getServerTimestamp(long after) {
    return new Date(new Date().getTime() + after);
  }

  /*
   * Returns the current authenticated user
   */
  public static FirebaseUser getCurrentUser() {
    return sUser.getCurrentUser();
  }

  /*
   * Returns a Firestore reference for users collection
   */
  @NotNull
  public static CollectionReference getUsersCollection() {
    return sDb.collection("users");
  }

  @NotNull
  public static DocumentReference getBeaconReference(String instance) {
    return sDb.collection("entries")
      .document(instance);
  }

  /*
   * Returns a Firestore reference for entries collection
   */
  @NotNull
  public static CollectionReference getEntriesCollection() {
    return sDb.collection("entries");
  }

  /*
   * Returns a listener to check if the operation is successful or failed
   */
  @NotNull
  public static Task<Void> setUsersCollection(Map<String, Object> data) {
    return getUsersCollection()
      .document()
      .set(data, SetOptions.merge());
  }

  /*
   * Returns a listener to check if the operation is successful or failed
   */
  @NotNull
  public static Task<Void> setUsersCollection(String doc, Map<String, Object> data) {
    return getUsersCollection()
      .document(doc)
      .set(data, SetOptions.merge());
  }

  /*
   * Returns a listener to check if the operation is successful or failed
   */
  @NotNull
  public static Task<DocumentReference> setEntriesCollection(Map<String, Object> data) {
    return getEntriesCollection()
      .add(data);
  }

  /*
   * Returns a listener to check if the operation is successful or failed
   */
  @NotNull
  public static Task<Void> setEntriesCollection(String doc, Map<String, Object> data) {
    return getEntriesCollection()
      .document(doc)
      .set(data, SetOptions.merge());
  }
}
