package rendering;

import gui.GUIManager;
import gui.MenuOption;
import gui.MenuOptionSet;
import gui.UserSettings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.SubScene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.InputEvent;
import javafx.scene.layout.GridPane;

/**
 * Class containing the settings menu for the pause screen
 *
 * @author Jack Hughes
 */
class PauseSettingsMenu extends SubScene {

	static GridPane gridPane = new GridPane();
	boolean opened = false;

	/**
	 * Create a new pause settings menu
	 *
	 * @param guiManager GUIManager for the game
	 */
	PauseSettingsMenu(GUIManager guiManager) {
		super(gridPane, guiManager.width, guiManager.height);
		gridPane.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9);");
		gridPane.getStylesheets().add("styles/menu.css");
        setScaleX(1024 / guiManager.width);
        setScaleY(576 / guiManager.height);

		gridPane.setAlignment(Pos.CENTER);
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(25, 25, 25, 25));

		// Obtain the user's settings
		UserSettings s = GUIManager.getUserSettings();

		// Create the option grid (grid to contain all possible options)
		GridPane optGrid = new GridPane();
		optGrid.setAlignment(Pos.CENTER);
		optGrid.setHgap(10);
		optGrid.setVgap(10);
		optGrid.setPadding(new Insets(25, 25, 25, 25));

		// Create the music label and slider
		Label musicLabel = new Label("Music Volume");

		Slider musicSlider = new Slider();
		musicSlider.setMin(0);
		musicSlider.setMax(100);
		musicSlider.setValue(s.getMusicVolume());
		musicSlider.setShowTickLabels(true);
		musicSlider.setShowTickMarks(true);
		musicSlider.setMajorTickUnit(50);
		musicSlider.setMinorTickCount(5);
		musicSlider.setBlockIncrement(10);
		musicSlider.addEventHandler(InputEvent.ANY, (event) -> {
			s.setMusicVolume((int) musicSlider.getValue());
			guiManager.notifySettingsObservers();
		});

		// Create the sound FX label and slider
		Label sfxLabel = new Label("SFX Volume");

		Slider sfxSlider = new Slider();
		sfxSlider.setMin(0);
		sfxSlider.setMax(100);
		sfxSlider.setValue(s.getSfxVolume());
		sfxSlider.setShowTickLabels(true);
		sfxSlider.setShowTickMarks(true);
		sfxSlider.setMajorTickUnit(50);
		sfxSlider.setMinorTickCount(5);
		sfxSlider.setBlockIncrement(10);
		sfxSlider.addEventHandler(InputEvent.ANY, (event) -> {
			s.setSfxVolume((int) sfxSlider.getValue());
			guiManager.notifySettingsObservers();
		});

		// Create the shading option label and checkbox
		Label shadingLabel = new Label("Use shading (default on)");

		CheckBox shadingCheckbox = new CheckBox();
		shadingCheckbox.setSelected(s.getShading());
		shadingCheckbox.setOnAction(event ->
		{
			s.setShading(shadingCheckbox.isSelected());
			GUIManager.renderer.getMap().toggleShading();
			guiManager.notifySettingsObservers();
		});

		// Add all of the options to the options grid
		optGrid.add(musicLabel, 0, 0);
		optGrid.add(musicSlider, 1, 0);
		optGrid.add(sfxLabel, 0, 1);
		optGrid.add(sfxSlider, 1, 1);
		optGrid.add(shadingLabel, 0, 2);
		optGrid.add(shadingCheckbox, 1, 2);

		// Create a array of options for the cancel and apply buttons
		MenuOption[] set = {
				new MenuOption("Back", true, (event) -> GUIManager.renderer.toggleSettingsMenu())
		};
		// Turn the array into a grid pane
		GridPane buttonGrid = MenuOptionSet.optionSetToGridPane(set);

		// Add the options grid and the button grid to the main grid
		gridPane.add(optGrid, 0, 0);
		gridPane.add(buttonGrid, 0, 1);

		guiManager.addButtonHoverSounds(gridPane);
	}
}
