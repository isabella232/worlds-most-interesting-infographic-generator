package com.worldsmostinterestinginfographic.servlet;

import com.google.inject.servlet.ServletModule;

import com.worldsmostinterestinginfographic.service.FacebookService;
import com.worldsmostinterestinginfographic.service.ServiceProvider;

public class CallbackServletModule extends ServletModule {
  @Override
  protected void configureServlets() {
    serve("/callback").with(CallbackServlet.class);
//    filter("/*").through(MyFilter.class);
//    filter("*.css").through(MyCssFilter.class);
//    // etc..
//
//    serve("*.html").with(MyServlet.class);
//    serve("/my/*").with(MyServlet.class);
//    // etc..
    bind(ServiceProvider.class).to(FacebookService.class);
  }
}
