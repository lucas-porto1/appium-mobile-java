package io.github.lucasporto1.appium.support;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.github.lucasporto1.appium.config.TestEnvironment;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.support.ui.WebDriverWait;

public final class ApplicationReady {

  private static final By HOME_ELEMENT = AppiumBy.accessibilityId("App");
  private static final List<By> STARTUP_DIALOG_BUTTONS =
      List.of(
          AppiumBy.id("com.android.permissioncontroller:id/permission_allow_button"),
          AppiumBy.id(
              "com.android.permissioncontroller:id/permission_allow_foreground_only_button"),
          AppiumBy.androidUIAutomator(
              "new UiSelector().resourceId(\"android:id/aerr_wait\").packageName(\"android\")"));

  private ApplicationReady() {}

  public static void waitUntilReady(AndroidDriver driver, TestEnvironment environment) {
    new WebDriverWait(driver, environment.elementTimeout())
        .until(
            ignored -> {
              if (isDisplayed(driver, HOME_ELEMENT)) {
                return true;
              }

              STARTUP_DIALOG_BUTTONS.stream()
                  .filter(locator -> isDisplayed(driver, locator))
                  .findFirst()
                  .ifPresent(locator -> driver.findElement(locator).click());

              return false;
            });
  }

  private static boolean isDisplayed(AndroidDriver driver, By locator) {
    try {
      return driver.findElement(locator).isDisplayed();
    } catch (NoSuchElementException | StaleElementReferenceException exception) {
      return false;
    }
  }
}
