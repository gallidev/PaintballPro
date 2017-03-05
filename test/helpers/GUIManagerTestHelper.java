package helpers;

import enums.Menu;
import gui.GUIManager;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import java.util.ArrayList;

/**
 * Created by jack on 01/03/2017.
 */
public class GUIManagerTestHelper extends GUIManager {

    public Menu currentMenu = Menu.MainMenu;

    public GUIManagerTestHelper() {
        super();
    }

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

    public static Button findButtonByTextInParent(String text, Parent root) {
        ArrayList<Button> buttons = navigateParentForButtons(root);
        for (Button b : buttons) {
            if (b.getText().equals(text))
                return b;
        }
        throw new RuntimeException("Could not find " + text + " in " + root.toString());
    }

    @Override
    public void transitionTo(Menu menu, Object o) {
        currentMenu = menu;
    }

}
