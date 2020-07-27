package org.ourkidslearningjourney.swtrace.models;

import androidx.annotation.Nullable;
import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;

/**
 * Entry represents an entry object when a user has passed through a gantry.
 * <p>
 * Entry extends HashMap from Java to serialize and deserialize data incoming
 * or outgoing from the Android application.
 *
 * @author Aaron Teo
 * @since 2.0
 */
public class Entry extends HashMap<String, Object> {
  /**
   * Represents the document's unique identifier.
   */
  public static final String UID = "UID";

  /**
   * Represents the device's FCM Messaging token id.
   * <p>
   * FCM Messaging token id (aka FCM Token) is generated from Firebase Cloud
   * Messaging service. We can only capture the identifier and store it within
   * Firestore to alert the correct user when an infection notification is triggered.
   */
  public static final String FCM_TOKEN = "FCM Token";

  /**
   * Represents the Eddystone beacon instance identifier.
   * <p>
   * Eddystone beacon instance identifer (aka Instance ID) is provided by the physical
   * Kontakt.io beacon broadcasting packet. We will use the instance id to triage
   * persons that have visited the specified timestamp and duration in order to create
   * an infection notification.
   */
  public static final String BEACON_ID = "Beacon ID";

  /**
   * Represents the timestamp when the user has entered the place of interest.
   */
  public static final String ENTERED_TIMESTAMP = "Entered Timestamp";

  /**
   * Represents the timestamp when the user has left the place of interest.
   * <p>
   * By default, we will automatically set the timestamp to an hour after their entry.
   * This is to ensure that when an infection notification alert is generated, all
   * persons in the specified timestamp, duration and venue will get notified.
   */
  public static final String EXITED_TIMESTAMP = "Exited Timestamp";

  /**
   * Stores the provided key/value pair into HashMap.
   *
   * @param key   the key that will be serialized into Firestore.
   * @param value the value that will be serialized into Firestore.
   * @return the key/value pair object to be placed into HashMap.
   * @see EntryKeyDef
   */
  @Nullable
  @Override
  public Object put(
    @EntryKeyDef String key,
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
    FCM_TOKEN,
    BEACON_ID,
    ENTERED_TIMESTAMP,
    EXITED_TIMESTAMP
  })
  public @interface EntryKeyDef {
  }
}
