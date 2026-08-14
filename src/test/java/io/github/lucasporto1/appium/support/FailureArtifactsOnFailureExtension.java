package io.github.lucasporto1.appium.support;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public final class FailureArtifactsOnFailureExtension implements AfterTestExecutionCallback {

  @Override
  public void afterTestExecution(ExtensionContext context) {
    if (context.getExecutionException().isEmpty()) {
      return;
    }

    var testInstance = context.getRequiredTestInstance();
    if (testInstance instanceof BaseMobileTest mobileTest) {
      var artifactName =
          context.getRequiredTestClass().getSimpleName()
              + "-"
              + context.getRequiredTestMethod().getName();
      mobileTest.activeDriver().ifPresent(driver -> FailureArtifacts.capture(driver, artifactName));
    }
  }
}
