package com.example.traccite.services;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesService extends Application {

  /*
   * Logcat: Logging Tag
   */
  private static final String TAG = "PreferencesService";

  /*
   * SharedPreferences: Global Preferences Name
   */
  private static final String GLOBAL_PREFERENCES = "_";

  /*
   * SharedPreferences: All Preference Keys
   */
  public static final String FCM_TOKEN_KEY = "FCM_TOKEN_UID";
  public static final String SETUP_COMPLETED_KEY = "SETUP_COMPLETED";

  /*
   * SharedPreferences: Current Preferences Instance
   */
  private static SharedPreferences mPreferences;

  /*
   * A workaround to the onCreate method not being called
   */
  public static void initializeService(Context context) {
    /*
     * Set the current SharedPreferences instance
     */
    mPreferences = context.getSharedPreferences(GLOBAL_PREFERENCES, MODE_PRIVATE);
  }

  /*
   * Get the current SharedPreferences Editor instance
   */
  public static SharedPreferences.Editor getEditor() {
    return mPreferences.edit();
  }

  /*
   * Resets all keys documented in this service with their
   * respective default values
   */
  public static void resetAllKeys() {
    getEditor().putString(FCM_TOKEN_KEY, null);
    getEditor().putBoolean(SETUP_COMPLETED_KEY, false);
    getEditor().commit();
  }

  /*
   * Set the specific key with the respective value provided
   *
   * Takes only String
   */
  public static void setStringKey(String key, String value) {
    getEditor().putString(key, value);
    getEditor().commit();
  }

  /*
   * Set the specific key with the respective value provided
   *
   * Takes only boolean
   */
  public static void setBooleanKey(String key, boolean value) {
    getEditor().putBoolean(key, value);
    getEditor().commit();
  }
}
