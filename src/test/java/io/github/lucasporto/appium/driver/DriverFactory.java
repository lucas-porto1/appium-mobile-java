package io.github.lucasporto.appium.driver;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.github.lucasporto.appium.config.TestEnvironment;
import java.net.MalformedURLException;

public final class DriverFactory {

  private DriverFactory() {}

  public static AndroidDriver create(TestEnvironment environment) {
    var options =
        new UiAutomator2Options()
            .setDeviceName(environment.deviceName())
            .setApp(environment.appPath().toString())
            .setAutoGrantPermissions(true)
            .setNoReset(false)
            .setNewCommandTimeout(environment.newCommandTimeout());

    environment.deviceUdid().ifPresent(options::setUdid);

    try {
      return new AndroidDriver(environment.appiumServerUri().toURL(), options);
    } catch (MalformedURLException exception) {
      throw new IllegalStateException(
          "Invalid Appium server URL: " + environment.appiumServerUri(), exception);
    }
  }
}
