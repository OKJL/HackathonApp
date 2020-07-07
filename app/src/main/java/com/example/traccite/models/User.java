package com.example.traccite.models;

import com.google.firebase.firestore.FieldValue;

public class User {
  private String uid;
  private String nric_fin;
  private String full_name;
  private String device_id;
  private String contact_number;
  private boolean resident_of_singapore;
  private FieldValue updatedAt;
  private FieldValue createdAt;

  public User(
    String uid,
    String nric_fin,
    String full_name,
    String device_id,
    String contact_number,
    boolean resident_of_singapore
  ) {
    this.uid = uid;
    this.nric_fin = nric_fin;
    this.full_name = full_name;
    this.device_id = device_id;
    this.contact_number = contact_number;
    this.resident_of_singapore = resident_of_singapore;
  }

  public User(
    String uid,
    String nric_fin,
    String full_name,
    String device_id,
    String contact_number,
    boolean resident_of_singapore,
    FieldValue updatedAt,
    FieldValue createdAt
  ) {
    this.uid = uid;
    this.nric_fin = nric_fin;
    this.full_name = full_name;
    this.device_id = device_id;
    this.contact_number = contact_number;
    this.resident_of_singapore = resident_of_singapore;
    this.updatedAt = updatedAt;
    this.createdAt = createdAt;
  }

  public String getUid() {
    return this.uid;
  }

  public String getNric_fin() {
    return this.nric_fin;
  }

  public String getFull_name() {
    return this.full_name;
  }

  public String getDevice_id() {
    return this.device_id;
  }

  public String getContact_number() {
    return this.contact_number;
  }

  public boolean isResident_of_singapore() {
    return this.resident_of_singapore;
  }

  public FieldValue getUpdatedAt() {
    return this.updatedAt;
  }

  public FieldValue getCreatedAt() {
    return this.createdAt;
  }
}
