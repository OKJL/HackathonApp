package com.example.traccite;

import android.app.Application;

public class NRICManager extends Application {
  /*
   * checkNRICForValidity checks the given NRIC number with
   * Singapore Government's NRIC hashing algorithm (private).
   *
   * Source: https://github.com/taronaeo/NRICValidator/
   */
  public static boolean checkNRICForValidity(String nric) {
    // NRIC Checksum Total
    int checksum_total = 0;

    // NRIC Checksum
    final int[] checksum_weights = {2, 7, 6, 5, 4, 3, 2};

    // NRIC Breakdown
    final String nric_number = nric.trim().toUpperCase();
    final String nric_digits = nric.substring(1, 8);
    final String prefix_letter = String.valueOf(nric.charAt(0));
    final String checksum_letter = String.valueOf(nric.charAt(8));

    /*
     * Conditions to check before returning `false`:
     * 1. If the NRIC provided is not exactly 9 characters long or;
     * 2. If the NRIC prefix is not recognised or;
     * 3. If the NRIC digits are not numbers
     */
    if (
      nric_number.length() != 9
        || "STGF".indexOf(prefix_letter) == -1
        || Double.isNaN(Integer.parseInt(nric_digits))
    ) {
      return false;
    }

    /*
     * Iterates through every single digit found in the
     * NRIC provided and multiplies it by the respective
     * weight.
     */
    for (int i = 0; i < nric_digits.length(); i++) {
      checksum_total += Integer.parseInt(
        String.valueOf(nric_digits.charAt(i))
      ) * checksum_weights[i];
    }

    if (prefix_letter.equals("T") || prefix_letter.equals("G"))
      checksum_total += 4;

    if (prefix_letter.equals("S") || prefix_letter.equals("T")) {
      final String[] checksum_lookup = {
        "J", "Z", "I", "H", "G", "F", "E", "D", "C", "B", "A"
      };

      if (checksum_lookup[checksum_total % 11].equals(checksum_letter))
        return true;
    }

    if (prefix_letter.equals("F") || prefix_letter.equals("G")) {
      final String[] checksum_lookup = {
        "X", "W", "U", "T", "R", "Q", "P", "N", "M", "L", "K"
      };

      if (checksum_lookup[checksum_total % 11].equals(checksum_letter))
        return true;
    }

    return false;
  }
}
