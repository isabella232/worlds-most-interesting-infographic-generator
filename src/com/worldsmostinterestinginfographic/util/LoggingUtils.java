package com.worldsmostinterestinginfographic.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public enum LoggingUtils {
  INSTANCE;

  /**
   * Anonymize input data.
   *
   * @param statement The statement to anonymize
   * @return An anonymized, but reproducible, version of the input data
   */
  public static String anonymize(String statement) {

    byte[] bytesOfMessage = statement.getBytes(StandardCharsets.UTF_8);

    MessageDigest messageDigest = null;
    try {
      messageDigest = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }

    byte[] thedigest = messageDigest.digest(bytesOfMessage);

    StringBuffer stringBuffer = new StringBuffer();
    for (int i = 0; i < thedigest.length; i++) {
      stringBuffer.append(Integer.toString((thedigest[i] & 0xff) + 0x100, 16).substring(1));
    }

    return "[" + stringBuffer.toString() + "]";
  }
}
