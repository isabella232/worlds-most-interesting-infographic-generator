package com.worldsmostinterestinginfographic.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public enum LoggingUtils {
  INSTANCE;

  public static String anonymize(String statement) {
    byte[] bytesOfMessage = statement.getBytes(StandardCharsets.UTF_8);

    MessageDigest md = null;

    try {
      md = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }

    byte[] thedigest = md.digest(bytesOfMessage);

    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < thedigest.length; i++) {
      sb.append(Integer.toString((thedigest[i] & 0xff) + 0x100, 16).substring(1));
    }

    return "[" + sb.toString() + "]";
  }
}
