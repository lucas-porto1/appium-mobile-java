package io.github.lucasporto.appium.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.lucasporto.appium.screens.HomeScreen;
import org.junit.jupiter.api.Test;

class ControlsTest extends BaseMobileTest {

  @Test
  void fillsAFieldAndSelectsACheckbox() {
    var controls = new HomeScreen(driver(), elementTimeout()).openLightThemeControls();

    controls.enterText("Lucas Porto");
    assertEquals("Lucas Porto", controls.enteredText());

    assertFalse(controls.isFirstCheckboxChecked());
    controls.toggleFirstCheckbox();
    assertTrue(controls.isFirstCheckboxChecked());
  }
}
