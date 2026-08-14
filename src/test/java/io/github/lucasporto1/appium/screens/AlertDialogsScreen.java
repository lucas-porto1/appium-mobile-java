package io.github.lucasporto1.appium.screens;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import java.time.Duration;

public final class AlertDialogsScreen extends BaseScreen {

  public AlertDialogsScreen(AndroidDriver driver, Duration timeout) {
    super(driver, timeout);
  }

  public void openConfirmDialog() {
    tap(AppiumBy.accessibilityId("OK Cancel dialog with a message"));
  }

  public String alertMessage() {
    return alert().getText();
  }

  public void confirmAlert() {
    alert().accept();
  }
}
