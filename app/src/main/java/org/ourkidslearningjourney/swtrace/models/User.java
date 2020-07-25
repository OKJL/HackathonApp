package org.ourkidslearningjourney.swtrace.models;

import androidx.annotation.Nullable;
import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;

/**
 * User represents the current user residing in Firebase Auth/Firestore document.
 *
 * User extends HashMap from Java to serialize and deserialize data incoming or
 * outgoing from the Android application.
 *
 * @author Aaron Teo
 * @since 1.0
 */
public class User extends HashMap<String, Object> {
  /**
   * Represents Firebase Auth's unique identifier.
   */
  public static final String UID = "UID";

  /**
   * Represents the person's identification number.
   *
   * Note: NRIC/FIN/Passport ID will not be checked in any way
   *       or form. The information provided is solely to be
   *       transferred to relevant authorities when an infection
   *       has been detected and details of possibly infected
   *       persons are required.
   */
  public static final String NRIC_FIN_PPT = "NRIC/FIN/PPT";

  /**
   * Represents the person's full name.
   *
   * Note: Full Name will not be checked in any way or form.
   *       The information provided is solely to be transferred
   *       to relevant authorities when an infection has been
   *       detected and details of possibly infected persons are
   *       required.
   */
  public static final String FULL_NAME = "Full Name";

  /**
   * Represents the persons's birth country.
   */
  public static final String COUNTRY_NAME = "Country Name";

  /**
   * Represents the person's contact number.
   *
   * Usually, the contact number is automatically handled by
   * Firebase Auth system. However, there might be certain cases
   * whereby phone numbers are not specified in the account.
   *
   * If that is the case, the user will have to manually enter
   * their phone number into the field which may cause unusual
   * results.
   */
  public static final String CONTACT_NUMBER = "Contact Number";

  /**
   * Represents the device's FCM Messaging token id.
   *
   * FCM Messaging token id (aka FCM Token) is generated from
   * Firebase Cloud Messaging service. We can only capture the
   * identifier and store it within Firestore to alert the correct
   * user when an infection notification is triggered.
   */
  public static final String FCM_TOKENS = "FCM Tokens";

  /**
   * Represents the timestamp when the account was created.
   */
  public static final String CREATED_AT = "createdAt";

  /**
   * Stores the provided key/value pair into HashMap.
   *
   * @param key   the key that will be serialized into Firestore.
   * @param value the value that will be serialized into Firestore.
   * @return      the key/value pair object to be placed into HashMap.
   *
   * @see UserKeyDef
   */
  @Nullable
  @Override
  public Object put(
    @UserKeyDef String key,
    Object value
  ) {
    return super.put(key, value);
  }

  /**
   * An interface that defines the acceptable keys for the object.
   * Similar to enums.
   */
  @Retention(RetentionPolicy.SOURCE)
  @StringDef({
    UID,
    NRIC_FIN_PPT,
    FULL_NAME,
    COUNTRY_NAME,
    CONTACT_NUMBER,
    FCM_TOKENS,
    CREATED_AT
  })
  public @interface UserKeyDef {
  }
}
