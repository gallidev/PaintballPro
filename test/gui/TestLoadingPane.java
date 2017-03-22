package gui;

import helpers.JavaFXTestHelper;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for Loading Pane
 *
 * @author Jack Hughes
 */
public class TestLoadingPane {

	/**
	 * Start the JavaFX application
	 * @throws Exception test failed
	 */
	@Before
	public void setUp() throws Exception {
		JavaFXTestHelper.setupApplication();
		JavaFXTestHelper.waitForPlatform();
	}

	/**
	 * Test for checking the starting and stopping of the loading pane
	 * @throws Exception test failed
	 */
	@Test
	public void testLoad() throws Exception {
		GridPane gridPane = new GridPane();
		LoadingPane loadingPane = new LoadingPane(gridPane);

		assertTrue(loadingPane.getChildrenUnmodifiable().contains(gridPane));

		loadingPane.startLoading();

		JavaFXTestHelper.waitForPlatform();

		assertFalse(loadingPane.getChildrenUnmodifiable().contains(gridPane));

		loadingPane.stopLoading();

		JavaFXTestHelper.waitForPlatform();

		assertTrue(loadingPane.getChildrenUnmodifiable().contains(gridPane));

	}
}