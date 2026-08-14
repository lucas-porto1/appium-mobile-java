package io.github.lucasporto.appium.tests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.lucasporto.appium.screens.HomeScreen;
import org.junit.jupiter.api.Test;

class AlertDialogsTest extends BaseMobileTest {

  @Test
  void confirmsANativeAndroidAlert() {
    var alertDialogs = new HomeScreen(driver(), elementTimeout()).openAlertDialogs();

    alertDialogs.openConfirmDialog();

    assertTrue(alertDialogs.alertMessage().contains("Lorem ipsum"));
    alertDialogs.confirmAlert();
  }
}
