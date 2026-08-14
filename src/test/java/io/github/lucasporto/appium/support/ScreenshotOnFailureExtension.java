package io.github.lucasporto.appium.support;

import io.github.lucasporto.appium.tests.BaseMobileTest;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public final class ScreenshotOnFailureExtension implements AfterTestExecutionCallback {

  @Override
  public void afterTestExecution(ExtensionContext context) {
    if (context.getExecutionException().isEmpty()) {
      return;
    }

    var testInstance = context.getRequiredTestInstance();
    if (testInstance instanceof BaseMobileTest mobileTest) {
      mobileTest.captureScreenshot(context.getRequiredTestMethod().getName());
    }
  }
}
