package io.github.lucasporto1.appium.support;

import io.appium.java_client.android.AndroidDriver;
import io.github.lucasporto1.appium.config.TestEnvironment;
import io.github.lucasporto1.appium.driver.DriverFactory;
import java.time.Duration;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(FailureArtifactsOnFailureExtension.class)
public abstract class BaseMobileTest {

  private AndroidDriver driver;
  private TestEnvironment environment;

  @BeforeEach
  void startSession() {
    environment = TestEnvironment.load();
    driver = DriverFactory.create(environment);
    ApplicationReady.waitUntilReady(driver, environment);
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

  final Optional<AndroidDriver> activeDriver() {
    return Optional.ofNullable(driver);
  }
}
