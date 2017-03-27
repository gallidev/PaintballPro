package gui;

import enums.Menu;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;

/**
 * Class to create a scene for the settings menu
 *
 * @author Jack Hughes
 */
public class SettingsMenu {

	/**
	 * Create and return a settings menu scene for a given GUI manager
	 *
	 * @param guiManager GUI manager to use
	 * @return scene for the settings menu
	 */
	public static Scene getScene(GUIManager guiManager) {
		// Obtain the user's settings
		UserSettings s = GUIManager.getUserSettings();

		// Create the main grid (to contain the options grid, and the apply/cancel buttons)
		GridPane mainGrid = new GridPane();
		mainGrid.setAlignment(Pos.CENTER);
		mainGrid.setHgap(10);
		mainGrid.setVgap(10);
		mainGrid.setPadding(MenuControls.scaleByResolution(25));

		Label titleLabel = new Label("Settings");
		titleLabel.setStyle("-fx-font-size: 26px;");

		// Create the option grid (grid to contain all possible options)
		GridPane optGrid = new GridPane();
		optGrid.setAlignment(Pos.CENTER);
		optGrid.setHgap(10);
		optGrid.setVgap(10);
		optGrid.setPadding(MenuControls.scaleByResolution(25));

		// Create the music label and slider
		Label musicLabel = new Label("Music Volume");

		Slider musicSlider = new Slider();
		musicSlider.setId("MusicSlider");
		musicSlider.setMin(0);
		musicSlider.setMax(100);
		musicSlider.setValue(s.getMusicVolume());
		musicSlider.setShowTickLabels(true);
		musicSlider.setShowTickMarks(true);
		musicSlider.setMajorTickUnit(50);
		musicSlider.setMinorTickCount(5);
		musicSlider.setBlockIncrement(10);
		musicSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			s.setMusicVolume((int) musicSlider.getValue());
			guiManager.notifySettingsObservers();
		});

		// Create the sound FX label and slider
		Label sfxLabel = new Label("SFX Volume");

		Slider sfxSlider = new Slider();
		sfxSlider.setId("SFXSlider");
		sfxSlider.setMin(0);
		sfxSlider.setMax(100);
		sfxSlider.setValue(s.getSfxVolume());
		sfxSlider.setShowTickLabels(true);
		sfxSlider.setShowTickMarks(true);
		sfxSlider.setMajorTickUnit(50);
		sfxSlider.setMinorTickCount(5);
		sfxSlider.setBlockIncrement(10);
		sfxSlider.valueProperty().addListener(((observable, oldValue, newValue) -> {
			s.setSfxVolume((int) sfxSlider.getValue());
			guiManager.notifySettingsObservers();
		}));

		// Create the shading option label and checkbox
		Label shadingLabel = new Label("Use shading (default on)");

		CheckBox shadingCheckbox = new CheckBox();
		shadingCheckbox.setId("ShadingCheckbox");
		shadingCheckbox.setSelected(s.getShading());
		shadingCheckbox.selectedProperty().addListener(((observable, oldValue, newValue) -> {
			s.setShading(shadingCheckbox.isSelected());
			guiManager.notifySettingsObservers();
		}));

		// Create the resolution label and combo box
		Label resolutionLabel = new Label("Resolution");

		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		ComboBox<String> resolutionComboBox = new ComboBox<>();
		resolutionComboBox.setId("ResolutionComboBox");
		boolean found = false;
		for (int i = 0; i < UserSettings.possibleResolutions.length; i++) {
			String res = UserSettings.possibleResolutions[i];
			try {
				String[] components = res.split("x");
				if (Integer.parseInt(components[0]) <= primaryScreenBounds.getWidth() && Integer.parseInt(components[1]) <= primaryScreenBounds.getHeight()) {
					resolutionComboBox.getItems().add(res);
					if (res.equals(s.getResolution())) {
						resolutionComboBox.getSelectionModel().select(i);
						found = true;
					}
				}
			} catch (NumberFormatException e) {
				// The resolution could not be converted to integers, this should not happen under normal use
			}
		}
		if (!found) {
			resolutionComboBox.getSelectionModel().select("1024x576");
		}
		resolutionComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
			s.setResolution(newValue);
			String[] resolution = newValue.split("x");
			guiManager.getStage().setWidth(Double.parseDouble(resolution[0]));
			guiManager.width = guiManager.getStage().getWidth();
			guiManager.getStage().setHeight(Double.parseDouble(resolution[1]));
			guiManager.height = guiManager.getStage().getHeight();
			Platform.runLater(() -> {
				guiManager.getStage().centerOnScreen();
			});
			mainGrid.setPadding(MenuControls.scaleByResolution(25));
			optGrid.setPadding(MenuControls.scaleByResolution(25));
		});


		// Add all of the options to the options grid
		optGrid.add(musicLabel, 0, 0);
		optGrid.add(musicSlider, 1, 0);
		optGrid.add(sfxLabel, 0, 1);
		optGrid.add(sfxSlider, 1, 1);
		optGrid.add(shadingLabel, 0, 2);
		optGrid.add(shadingCheckbox, 1, 2);
		optGrid.add(resolutionLabel, 0, 3);
		optGrid.add(resolutionComboBox, 1, 3);

		// Create a array of options for the cancel and apply buttons
		MenuOption[] set = {new MenuOption("Back", true, new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// Transition back to the main menu
				guiManager.transitionTo(Menu.MAIN_MENU);
			}
		})};
		// Turn the array into a grid pane
		GridPane buttonGrid = MenuOptionSet.optionSetToGridPane(set);

		// Add the options grid and the button grid to the main grid
		mainGrid.add(MenuControls.centreInPane(titleLabel), 0, 0);
		mainGrid.add(optGrid, 0, 1);
		mainGrid.add(buttonGrid, 0, 2);

		// Create a new scene using the main grid
		return guiManager.createScene(mainGrid);
	}
}
