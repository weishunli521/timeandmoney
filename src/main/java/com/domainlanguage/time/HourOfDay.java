/**
 * Copyright (c) 2005 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */
package com.domainlanguage.time;

public class HourOfDay {

  private final int value;

  public static HourOfDay value(int initial) {
    return new HourOfDay(initial);
  }

  public static HourOfDay value(int initial, String am_pm) {
    return HourOfDay.value(HourOfDay.convertTo24hour(initial, am_pm));
  }

  private static int convertTo24hour(int hour, String am_pm) {
    if (!("AM".equalsIgnoreCase(am_pm) || "PM".equalsIgnoreCase(am_pm))) {
      throw new IllegalArgumentException("AM PM indicator invalid: " + am_pm + ", please use AM or PM");
    }
    if (hour < 0 | hour > 12) {
      throw new IllegalArgumentException("Illegal value for 12 hour: " + hour + ", please use a value between 0 and 11");
    }
    int translatedAmPm = "AM".equalsIgnoreCase(am_pm) ? 0 : 12;
    translatedAmPm -= hour == 12 ? 12 : 0;
    return hour + translatedAmPm;
  }

  private HourOfDay(int initial) {
    if (initial < 0 || initial > 23) {
      throw new IllegalArgumentException("Illegal value for 24 hour: " + initial + ", please use a value between 0 and 23");
    }
    value = initial;
  }

  public boolean isAfter(HourOfDay another) {
    return value > another.value;
  }

  public boolean isBefore(HourOfDay another) {
    return value < another.value;
  }

  public int value() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @Override
  public boolean equals(Object object) {
    if (object instanceof HourOfDay) {
      HourOfDay other = (HourOfDay) object;
      return value == other.value;
    }
    return false;
  }

  @Override
  public int hashCode() {
    return value;
  }
}
