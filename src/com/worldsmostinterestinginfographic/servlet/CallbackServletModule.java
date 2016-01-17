package com.worldsmostinterestinginfographic.servlet;

import com.google.inject.Scopes;
import com.google.inject.servlet.ServletModule;

import com.worldsmostinterestinginfographic.service.FacebookService;
import com.worldsmostinterestinginfographic.service.ServiceProvider;

import javax.servlet.http.HttpServlet;

public class CallbackServletModule extends ServletModule {
  @Override
  protected void configureServlets() {
    serve("/callback").with(CallbackServlet.class);
    serve("/stats").with(StatisticsServlet.class);
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
