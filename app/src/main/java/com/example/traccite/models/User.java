package com.example.traccite.models;

import java.util.HashMap;
import java.util.Map;

public class User {
  public static final String UID = "UID";
  public static final String NRIC_FIN_PPT = "NRIC/FIN/PPT";
  public static final String FULL_NAME = "Full Name";
  public static final String CONTACT_NUMBER = "Contact Number";
  public static final String COUNTRY_CODE = "Country Code";
  public static final String FCM_TOKENS = "FCM Tokens";
  public static final String UPDATED_AT = "updatedAt";
  public static final String CREATED_AT = "createdAt";
  private final Map<String, Object> map = new HashMap<>();

//  public Map<String, Object> User(
//    String uid,
//    String nric_fin_ppt,
//    String full_name,
//    String contact_number,
//    String country_code,
//    String[] fcm_tokens,
//    FieldValue updatedAt,
//    FieldValue createdAt
//  ) {
//    map.put(UID, uid);
//    map.put(NRIC_FIN_PPT, nric_fin_ppt);
//    map.put(FULL_NAME, full_name);
//    map.put(CONTACT_NUMBER, contact_number);
//    map.put(COUNTRY_CODE, country_code);
//    map.put(FCM_TOKENS, fcm_tokens);
//    map.put(UPDATED_AT, updatedAt);
//    map.put(CREATED_AT, createdAt);
//
//    return map;
//  }

  public Map<String, Object> retrieve() {
    return map;
  }

  public void put(String key, Object value) {
    map.put(key, value);
  }

  public Object get(String key) {
    return map.get(key);
  }
}
