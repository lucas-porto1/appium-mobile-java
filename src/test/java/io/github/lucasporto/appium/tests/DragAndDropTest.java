package io.github.lucasporto.appium.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.lucasporto.appium.screens.HomeScreen;
import org.junit.jupiter.api.Test;

class DragAndDropTest extends BaseMobileTest {

  @Test
  void dragsAnElementUsingW3cTouchActions() {
    var dragAndDrop = new HomeScreen(driver(), elementTimeout()).openDragAndDrop();

    dragAndDrop.dragFirstDotToSecond();

    assertEquals("Dropped!", dragAndDrop.resultMessage());
  }
}
