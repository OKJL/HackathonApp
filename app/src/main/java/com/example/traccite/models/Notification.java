package com.example.traccite.models;

import androidx.annotation.Nullable;
import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;

public class Notification extends HashMap<String, Object> {
  public static final String MESSAGE_TITLE = "Message Title";
  public static final String MESSAGE_BODY = "Message Body";
  public static final String MESSAGE_TYPE = "Message Type";
  public static final String COUNTRY_NAME = "Country Name";
  public static final String FCM_TOKEN_ID = "FCM Token ID";

  public static final String NOTIFY_TYPE_EVERYONE = "ALL_USERS";
  public static final String NOTIFY_TYPE_COUNTRY_NAME = "COUNTRY_NAME";
  public static final String NOTIFY_TYPE_SPECIFIC_TOKEN = "SPECIFIC_TOKEN";

  @Nullable
  @Override
  public Object put(
    @NotificationKeyDef String key,
    @NotificationTypeDef Object value
  ) {
    return super.put(key, value);
  }

  @Retention(RetentionPolicy.SOURCE)
  @StringDef({
    MESSAGE_TITLE,
    MESSAGE_BODY,
    MESSAGE_TYPE,
    COUNTRY_NAME,
    FCM_TOKEN_ID,
  })
  public @interface NotificationKeyDef {
  }

  @Retention(RetentionPolicy.SOURCE)
  @StringDef({
    NOTIFY_TYPE_EVERYONE,
    NOTIFY_TYPE_COUNTRY_NAME,
    NOTIFY_TYPE_SPECIFIC_TOKEN
  })
  public @interface NotificationTypeDef {
  }
}
