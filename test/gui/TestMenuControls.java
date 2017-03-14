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
 * Created by jack on 01/03/2017.
 */
public class TestMenuControls {
    @Test
    public void centreInPane() throws Exception {
        JavaFXTestHelper.setupApplication();

        Button b = new Button("Test");
        GridPane p = MenuControls.centreInPane(b);

        assertTrue(p.getHgap() == 10);
        assertTrue(p.getVgap() == 10);
        assertTrue(p.getPadding().getTop() == 5);
        assertTrue(p.getPadding().getBottom() == 5);
        assertTrue(p.getPadding().getLeft() == 5);
        assertTrue(p.getPadding().getRight() == 5);
        assertTrue(p.getChildren().get(0) == b);
    }

}