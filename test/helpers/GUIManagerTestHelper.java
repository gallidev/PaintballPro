package helpers;

import enums.Menu;
import gui.GUIManager;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;

import java.util.ArrayList;

/**
 * Test helper for simulating a GUIManager
 */
public class GUIManagerTestHelper extends GUIManager {

    // Field to keep track of the current menu
    public Menu currentMenu = Menu.MAIN_MENU;

    /**
     * Construct a new simulation of a GUIManager
     */
    public GUIManagerTestHelper() {
        super();
    }

    /**
     * Helper method for finding all table views in a given parent node
     * @param root parent node to search from
     * @return array of table views
     */
    public static ArrayList<TableView> findTableViewInParent(Parent root) {
        ArrayList<TableView> returnArr = new ArrayList<>();
        for (Node n : root.getChildrenUnmodifiable()) {
            if (n instanceof TableView) {
                returnArr.add((TableView)n);
            } else if (n instanceof Parent) {
                returnArr.addAll(findTableViewInParent((Parent)n));
            }
        }
        return returnArr;
    }

    /**
     * Helper method to find all buttons in a given parent node
     * @param root parent node to search from
     * @return array of buttons
     */
    private static ArrayList<Button> navigateParentForButtons(Parent root) {
        ArrayList<Button> returnArr = new ArrayList<>();
        for (Node n : root.getChildrenUnmodifiable()) {
            if (n instanceof Button) {
                returnArr.add((Button)n);
            } else if (n instanceof Parent) {
                returnArr.addAll(navigateParentForButtons((Parent)n));
            }
        }
        return returnArr;
    }

    /**
     * Helper method to find a given button in a parent node based upon the button's text value
     * @param text name of the button
     * @param root parent node to search from
     * @return button containing the text value, or a runtime exception if not found
     */
    public static Button findButtonByTextInParent(String text, Parent root) {
        ArrayList<Button> buttons = navigateParentForButtons(root);
        for (Button b : buttons) {
            if (b.getText().equals(text))
                return b;
        }
        throw new RuntimeException("Could not find " + text + " in " + root.toString());
    }

    /**
     * Helper method to find all radio buttons in a given parent node
     * @param root parent node to search from
     * @return array of radio buttons
     */
    public static ArrayList<RadioButton> navigateParentForRadioButtons(Parent root) {
        ArrayList<RadioButton> returnArr = new ArrayList<>();
        for (Node n : root.getChildrenUnmodifiable()) {
            if (n instanceof RadioButton) {
                returnArr.add((RadioButton)n);
            } else if (n instanceof Parent) {
                returnArr.addAll(navigateParentForRadioButtons((Parent)n));
            }
        }
        return returnArr;
    }

    /**
     * Fake transition for the GUIManager (only storing the new menu, and not any processing of it)
     * @param menu the menu type to switch to
     * @param o objects to be passed to the target scene
     */
    @Override
    public void transitionTo(Menu menu, Object... o) {
        currentMenu = menu;
    }

}
