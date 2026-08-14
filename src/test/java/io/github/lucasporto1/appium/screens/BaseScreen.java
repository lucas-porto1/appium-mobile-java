package io.github.lucasporto1.appium.screens;

import io.appium.java_client.android.AndroidDriver;
import java.time.Duration;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

abstract class BaseScreen {

  protected final AndroidDriver driver;
  private final Duration timeout;
  private final WebDriverWait wait;

  protected BaseScreen(AndroidDriver driver, Duration timeout) {
    this.driver = driver;
    this.timeout = timeout;
    this.wait = new WebDriverWait(driver, timeout);
  }

  protected Duration timeout() {
    return timeout;
  }

  protected WebElement visible(By locator) {
    return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
  }

  protected void tap(By locator) {
    wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
  }

  protected Alert alert() {
    return wait.until(ExpectedConditions.alertIsPresent());
  }
}
