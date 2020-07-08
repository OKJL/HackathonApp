package com.example.traccite.models;

import com.google.firebase.firestore.FieldValue;

public class User {
  private String uid;
  private String nric_fin_ppn;
  private String full_name;
  private String country_code;
  private String contact_number;
  private FieldValue fcm_tokens;
  private FieldValue updatedAt;
  private FieldValue createdAt;

  public User(
    String uid,
    String nric_fin_ppn,
    String full_name,
    String contact_number,
    String country_code,
    FieldValue fcm_tokens
  ) {
    this.uid = uid;
    this.nric_fin_ppn = nric_fin_ppn;
    this.full_name = full_name;
    this.fcm_tokens = fcm_tokens;
    this.contact_number = contact_number;
    this.country_code = country_code;
  }

  public User(
    String uid,
    String nric_fin_ppn,
    String full_name,
    String contact_number,
    String country_code,
    FieldValue fcm_tokens,
    FieldValue updatedAt,
    FieldValue createdAt
  ) {
    this.uid = uid;
    this.nric_fin_ppn = nric_fin_ppn;
    this.full_name = full_name;
    this.fcm_tokens = fcm_tokens;
    this.contact_number = contact_number;
    this.country_code = country_code;
    this.updatedAt = updatedAt;
    this.createdAt = createdAt;
  }

  public String getUid() {
    return this.uid;
  }

  public String getNric_fin_ppn() {
    return this.nric_fin_ppn;
  }

  public String getFull_name() {
    return this.full_name;
  }

  public String getContact_number() {
    return this.contact_number;
  }

  public String getCountry_code() {
    return this.country_code;
  }

  public FieldValue getFcm_tokens() {
    return this.fcm_tokens;
  }

  public FieldValue getUpdatedAt() {
    return this.updatedAt;
  }

  public FieldValue getCreatedAt() {
    return this.createdAt;
  }
}
