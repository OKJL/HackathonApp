package org.ourkidslearningjourney.swtrace.models;

import androidx.annotation.Nullable;
import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;

public class Entry extends HashMap<String, Object> {
  public static final String UID = "UID";
  public static final String DEVICE_ID = "Device ID";
  public static final String BEACON_ID = "Beacon ID";
  public static final String ENTERED_TIMESTAMP = "Entered Timestamp";
  public static final String EXITED_TIMESTAMP = "Exited Timestamp";

  @Nullable
  @Override
  public Object put(
    @EntryKeyDef String key,
    Object value
  ) {
    return super.put(key, value);
  }

  @Retention(RetentionPolicy.SOURCE)
  @StringDef({
    UID,
    DEVICE_ID,
    BEACON_ID,
    ENTERED_TIMESTAMP,
    EXITED_TIMESTAMP
  })
  public @interface EntryKeyDef {
  }
}
