package org.ourkidslearningjourney.swtrace.models;

import androidx.annotation.Nullable;
import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;

public class User extends HashMap<String, Object> {
  public static final String UID = "UID";
  public static final String NRIC_FIN_PPT = "NRIC/FIN/PPT";
  public static final String FULL_NAME = "Full Name";
  public static final String COUNTRY_NAME = "Country Name";
  public static final String CONTACT_NUMBER = "Contact Number";
  public static final String FCM_TOKENS = "FCM Tokens";
  public static final String CREATED_AT = "createdAt";

  @Nullable
  @Override
  public Object put(
    @UserKeyDef String key,
    Object value
  ) {
    return super.put(key, value);
  }

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
