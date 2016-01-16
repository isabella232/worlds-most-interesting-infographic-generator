package com.worldsmostinterestinginfographic;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

import com.worldsmostinterestinginfographic.servlet.CallbackServletModule;

public class MyGuiceServletConfig extends GuiceServletContextListener {

  @Override
  protected Injector getInjector() {
    return Guice.createInjector(new CallbackServletModule());
  }
}
