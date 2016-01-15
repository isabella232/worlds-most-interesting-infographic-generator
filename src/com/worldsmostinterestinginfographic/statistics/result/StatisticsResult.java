package com.worldsmostinterestinginfographic.statistics.result;

public interface StatisticsResult<T> {
  T getResult();
  String getError();
}
