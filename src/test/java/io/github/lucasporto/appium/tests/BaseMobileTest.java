package io.github.lucasporto.appium.tests;

import io.appium.java_client.android.AndroidDriver;
import io.github.lucasporto.appium.config.TestEnvironment;
import io.github.lucasporto.appium.driver.DriverFactory;
import io.github.lucasporto.appium.support.ScreenshotOnFailureExtension;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.OutputType;

@ExtendWith(ScreenshotOnFailureExtension.class)
public abstract class BaseMobileTest {

  private AndroidDriver driver;
  private TestEnvironment environment;

  @BeforeEach
  void startSession() {
    environment = TestEnvironment.load();
    driver = DriverFactory.create(environment);
  }

  @AfterEach
  void stopSession() {
    if (driver != null) {
      driver.quit();
      driver = null;
    }
  }

  protected final AndroidDriver driver() {
    return driver;
  }

  protected final Duration elementTimeout() {
    return environment.elementTimeout();
  }

  public final void captureScreenshot(String testName) {
    if (driver == null) {
      return;
    }

    var safeName = testName.replaceAll("[^a-zA-Z0-9._-]", "_");
    var screenshotDirectory = Path.of("target", "screenshots");

    try {
      Files.createDirectories(screenshotDirectory);
      Files.write(
          screenshotDirectory.resolve(safeName + ".png"), driver.getScreenshotAs(OutputType.BYTES));
    } catch (IOException | RuntimeException exception) {
      System.err.println("Unable to capture failure screenshot: " + exception.getMessage());
    }
  }
}
