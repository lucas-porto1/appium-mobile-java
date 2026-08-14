package io.github.lucasporto1.appium.screens;

import io.appium.java_client.android.AndroidDriver;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

public final class DragAndDropScreen extends BaseScreen {

  private static final By FIRST_DOT = By.id("io.appium.android.apis:id/drag_dot_1");
  private static final By SECOND_DOT = By.id("io.appium.android.apis:id/drag_dot_2");
  private static final By RESULT = By.id("io.appium.android.apis:id/drag_result_text");

  public DragAndDropScreen(AndroidDriver driver, Duration timeout) {
    super(driver, timeout);
  }

  public void dragFirstDotToSecond() {
    var source = visible(FIRST_DOT);
    var target = visible(SECOND_DOT);
    var finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
    var gesture = new Sequence(finger, 1);

    gesture.addAction(
        finger.createPointerMove(Duration.ZERO, PointerInput.Origin.fromElement(source), 0, 0));
    gesture.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
    gesture.addAction(new Pause(finger, Duration.ofMillis(500)));
    gesture.addAction(
        finger.createPointerMove(
            Duration.ofMillis(800), PointerInput.Origin.fromElement(target), 0, 0));
    gesture.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

    driver.perform(List.of(gesture));
  }

  public String resultMessage() {
    return visible(RESULT).getText();
  }
}
