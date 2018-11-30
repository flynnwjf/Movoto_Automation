package com.movoto.fixtures.impl.wd;

import java.net.URL;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.interactions.HasTouchScreen;
import org.openqa.selenium.interactions.TouchScreen;
import org.openqa.selenium.remote.RemoteTouchScreen;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

public class IOSDriverNG extends IOSDriver implements HasTouchScreen {
 
public RemoteTouchScreen touch;
 
 public IOSDriverNG(URL remoteAddress,
   Capabilities desiredCapabilities) {
  super(remoteAddress, desiredCapabilities);
  touch = new RemoteTouchScreen(getExecuteMethod());
 }

 public TouchScreen getTouch() {
  return touch;
 }

}