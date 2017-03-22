package gui;

import helpers.GUIManagerTestHelper;
import helpers.JavaFXTestHelper;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.junit.Before;
import org.junit.Test;

import javax.xml.soap.Text;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Tests for Nickname and Server Selection Menu class
 *
 * @author Jack Hughes
 */
public class TestNicknameServerSelectMenu {

	/**
	 * Setup the JavaFX application
	 * @throws Exception test failed
	 */
	@Before
	public void setUp() throws Exception {
		JavaFXTestHelper.setupApplication();
	}

	/**
	 * Test for any errors when creating the nickname scene
	 * @throws Exception test failed
	 */
	@Test
	public void testGetScene() throws Exception {
		GUIManagerTestHelper g = new GUIManagerTestHelper();
		Scene s = NicknameServerSelectMenu.getScene(g);
	}

	/**
	 * Test to check that the radio button set works correctly
	 * @throws Exception
	 */
	@Test
	public void testRadioButtons() throws Exception {
		GUIManagerTestHelper g = new GUIManagerTestHelper();
		Scene s = NicknameServerSelectMenu.getScene(g);
		ArrayList<RadioButton> radioButtons = GUIManagerTestHelper.navigateParentForRadioButtons(s.getRoot());
		assertTrue(radioButtons.get(0).isSelected());
		assertFalse(radioButtons.get(1).isSelected());
		radioButtons.get(1).fire();
		Thread.sleep(1000);
		assertTrue(radioButtons.get(1).isSelected());
		assertFalse(radioButtons.get(0).isSelected());
	}

	/**
	 * Test to try to connect via discovery to no servers
	 * @throws Exception test failed
	 */
	@Test
	public void testConnectNoServerAutomatic() throws Exception {
		GUIManagerTestHelper g = new GUIManagerTestHelper();
		Scene s = NicknameServerSelectMenu.getScene(g);
		(GUIManagerTestHelper.findButtonByTextInParent("Connect", s.getRoot())).fire();
		Thread.sleep(5000);
	}
}