package io.github.lucasporto.appium.screens;

import io.appium.java_client.android.AndroidDriver;
import java.time.Duration;
import org.openqa.selenium.By;

public final class ControlsScreen extends BaseScreen {

  private static final By TEXT_FIELD = By.id("io.appium.android.apis:id/edit");
  private static final By FIRST_CHECKBOX = By.id("io.appium.android.apis:id/check1");

  public ControlsScreen(AndroidDriver driver, Duration timeout) {
    super(driver, timeout);
  }

  public void enterText(String text) {
    var textField = visible(TEXT_FIELD);
    textField.clear();
    textField.sendKeys(text);
  }

  public String enteredText() {
    return visible(TEXT_FIELD).getText();
  }

  public void toggleFirstCheckbox() {
    tap(FIRST_CHECKBOX);
  }

  public boolean isFirstCheckboxChecked() {
    return Boolean.parseBoolean(visible(FIRST_CHECKBOX).getAttribute("checked"));
  }
}
