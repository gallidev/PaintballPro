package gui;

import helpers.GUIManagerTestHelper;
import helpers.JavaFXTestHelper;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test for the Settings Menu class
 *
 * @author Jack Hughes
 */
public class TestSettingsMenu {

	private Scene scene;

	/**
	 * Setup the JavaFX application
	 * @throws Exception test failed
	 */
	@Before
	public void setUp() throws Exception {
		JavaFXTestHelper.setupApplication();
		JavaFXTestHelper.waitForPlatform();
		GUIManager guiManager = new GUIManager();
		Platform.runLater(() -> {
			guiManager.setStage(new Stage());
		});
		JavaFXTestHelper.waitForPlatform();
		scene = SettingsMenu.getScene(guiManager);
	}

	/**
	 * Test changing the music volume slider
	 * @throws Exception test failed
	 */
	@Test
	public void testMusicSlider() throws Exception {
		((Slider) scene.lookup("#MusicSlider")).setValue(100.0);
		scene.lookup("#MusicSlider").fireEvent(new ActionEvent());
		assertTrue(GUIManager.getUserSettings().getMusicVolume() == 100);

		((Slider) scene.lookup("#MusicSlider")).setValue(0.0);
		scene.lookup("#SFXSlider").fireEvent(new ActionEvent());
		Thread.sleep(2000);
		assertTrue(GUIManager.getUserSettings().getMusicVolume() == 0);
	}

	/**
	 * Test changing the SFX volume slider
	 * @throws Exception test failed
	 */
	@Test
	public void testSFXSlider() throws Exception {
		((Slider) scene.lookup("#SFXSlider")).setValue(100.0);
		scene.lookup("#SFXSlider").fireEvent(new ActionEvent());
		assertTrue(GUIManager.getUserSettings().getSfxVolume() == 100);

		((Slider) scene.lookup("#SFXSlider")).setValue(0.0);
		scene.lookup("#SFXSlider").fireEvent(new ActionEvent());
		Thread.sleep(2000);
		assertTrue(GUIManager.getUserSettings().getSfxVolume() == 0);
	}

	/**
	 * Test changing the shading checkbox
	 * @throws Exception test failed
	 */
	@Test
	public void testShading() throws Exception {
		((CheckBox) scene.lookup("#ShadingCheckbox")).setSelected(false);
		scene.lookup("#ShadingCheckbox").fireEvent(new ActionEvent());
		Thread.sleep(2000);
		assertTrue(!GUIManager.getUserSettings().getShading());

		((CheckBox) scene.lookup("#ShadingCheckbox")).setSelected(true);
		scene.lookup("#ShadingCheckbox").fireEvent(new ActionEvent());
		Thread.sleep(2000);
		assertTrue(GUIManager.getUserSettings().getShading());
	}

	/**
	 * Test changing the resolution combo box
	 * @throws Exception test failed
	 */
	@Test
	public void testResolution() throws Exception {
		((ComboBox<String>) scene.lookup("#ResolutionComboBox")).getSelectionModel().selectFirst();
		String first = ((ComboBox<String>) scene.lookup("#ResolutionComboBox")).getSelectionModel().getSelectedItem();
		scene.lookup("#ResolutionComboBox").fireEvent(new ActionEvent());
		Thread.sleep(2000);
		assertTrue(GUIManager.getUserSettings().getResolution().equals(first));
	}

	/**
	 * Test that the back button can be pressed without any exceptions
	 * @throws Exception test failed
	 */
	@Test
	public void testBack() throws Exception {
		GUIManagerTestHelper.findButtonByTextInParent("Back", scene.getRoot()).fire();
		Thread.sleep(500);
	}

	/**
	 * Tear down the test
	 * @throws Exception tear down failed
	 */
	@After
	public void tearDown() throws Exception {
		scene = null;
	}
}