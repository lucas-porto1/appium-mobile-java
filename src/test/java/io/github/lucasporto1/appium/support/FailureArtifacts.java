package io.github.lucasporto1.appium.support;

import io.appium.java_client.android.AndroidDriver;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.openqa.selenium.OutputType;

final class FailureArtifacts {

  private static final Path ARTIFACT_DIRECTORY = Path.of("target", "failure-artifacts");

  private FailureArtifacts() {}

  static void capture(AndroidDriver driver, String testName) {
    var artifactName = safeName(testName) + "-" + System.currentTimeMillis();

    try {
      Files.createDirectories(ARTIFACT_DIRECTORY);
    } catch (IOException exception) {
      logCaptureError("artifact directory", exception);
      return;
    }

    captureScreenshot(driver, artifactName);
    capturePageSource(driver, artifactName);
  }

  private static void captureScreenshot(AndroidDriver driver, String artifactName) {
    try {
      Files.write(
          ARTIFACT_DIRECTORY.resolve(artifactName + ".png"),
          driver.getScreenshotAs(OutputType.BYTES));
    } catch (IOException | RuntimeException exception) {
      logCaptureError("screenshot", exception);
    }
  }

  private static void capturePageSource(AndroidDriver driver, String artifactName) {
    try {
      Files.writeString(
          ARTIFACT_DIRECTORY.resolve(artifactName + "-page-source.xml"),
          driver.getPageSource(),
          StandardCharsets.UTF_8);
    } catch (IOException | RuntimeException exception) {
      logCaptureError("page source", exception);
    }
  }

  private static String safeName(String value) {
    return value.replaceAll("[^a-zA-Z0-9._-]", "_");
  }

  private static void logCaptureError(String artifact, Exception exception) {
    System.err.println("Unable to capture failure " + artifact + ": " + exception.getMessage());
  }
}
