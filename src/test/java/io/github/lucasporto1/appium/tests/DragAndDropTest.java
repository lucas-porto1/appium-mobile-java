package io.github.lucasporto1.appium.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.lucasporto1.appium.screens.HomeScreen;
import io.github.lucasporto1.appium.support.BaseMobileTest;
import org.junit.jupiter.api.Test;

class DragAndDropTest extends BaseMobileTest {

  @Test
  void dragsAnElementUsingW3cTouchActions() {
    var dragAndDrop = new HomeScreen(driver(), elementTimeout()).openDragAndDrop();

    dragAndDrop.dragFirstDotToSecond();

    assertEquals("Dropped!", dragAndDrop.resultMessage());
  }
}
