package gui;

import helpers.JavaFXTestHelper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for the Menu Controls helper class
 */
public class TestMenuControls {

    /**
     * Test for centering a node in a pane correctly
     * @throws Exception test failed
     */
    @Test
    public void centreInPane() throws Exception {
        JavaFXTestHelper.setupApplication();

        Button b = new Button("Test");
        GridPane p = MenuControls.centreInPane(b);

        assertTrue(p.getHgap() == 10);
        assertTrue(p.getVgap() == 10);
        assertTrue(p.getPadding().getTop() == MenuControls.scaleByResolution(5).getTop());
        assertTrue(p.getPadding().getBottom() == MenuControls.scaleByResolution(5).getBottom());
        assertTrue(p.getPadding().getLeft() == MenuControls.scaleByResolution(5).getLeft());
        assertTrue(p.getPadding().getRight() == MenuControls.scaleByResolution(5).getRight());
        assertTrue(p.getChildren().get(0) == b);
    }

}