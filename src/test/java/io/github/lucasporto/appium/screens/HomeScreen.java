package io.github.lucasporto.appium.screens;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import java.time.Duration;

public final class HomeScreen extends BaseScreen {

  public HomeScreen(AndroidDriver driver, Duration timeout) {
    super(driver, timeout);
  }

  public AlertDialogsScreen openAlertDialogs() {
    tap(AppiumBy.accessibilityId("App"));
    tap(AppiumBy.accessibilityId("Alert Dialogs"));
    return new AlertDialogsScreen(driver, timeout());
  }

  public ControlsScreen openLightThemeControls() {
    tap(AppiumBy.accessibilityId("Views"));
    tap(AppiumBy.accessibilityId("Controls"));
    tap(AppiumBy.accessibilityId("1. Light Theme"));
    return new ControlsScreen(driver, timeout());
  }

  public DragAndDropScreen openDragAndDrop() {
    tap(AppiumBy.accessibilityId("Views"));
    tap(AppiumBy.accessibilityId("Drag and Drop"));
    return new DragAndDropScreen(driver, timeout());
  }
}
