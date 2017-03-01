package gui;

import enums.MenuEnum;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

/**
 * Class to create a scene for the settings menu
 */
public class SettingsMenu {

	/**
	 * Create and return a settings menu scene for a given GUI manager
	 * @param m GUI manager to use
	 * @return scene for the settings menu
	 */
	public static Scene getScene(GUIManager m) {
		// Obtain the user's settings
		UserSettings s = GUIManager.getUserSettings();
		
		// Create the main grid (to contain the options grid, and the apply/cancel buttons)
		GridPane mainGrid = new GridPane();
		mainGrid.setAlignment(Pos.CENTER);
		mainGrid.setHgap(10);
		mainGrid.setVgap(10);
		mainGrid.setPadding(new Insets(25, 25, 25, 25));
		
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
		musicSlider.addEventHandler(InputEvent.ANY, new EventHandler<InputEvent>() {
			@Override
			public void handle(InputEvent event) {
				s.setMusicVolume((int) musicSlider.getValue());
				m.notifySettingsObservers();
			}
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
		sfxSlider.addEventHandler(InputEvent.ANY, new EventHandler<InputEvent>() {
			@Override
			public void handle(InputEvent event) {
				s.setSfxVolume((int) sfxSlider.getValue());
				m.notifySettingsObservers();
			}
		});
		
		// Create the username label and text field
		Label usernameLabel = new Label("Username");
		
		TextField usernameText = new TextField();
		usernameText.setText(s.getUsername());
		usernameText.addEventHandler(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				s.setUsername(usernameText.getText());
				m.notifySettingsObservers();
			}
		});
		
		// Create the shading option label and checkbox
		Label shadingLabel = new Label("Use shading (default on)");
		
		CheckBox shadingCheckbox = new CheckBox();
		shadingCheckbox.setSelected(s.getShading());
		shadingCheckbox.addEventHandler(InputEvent.ANY, new EventHandler<InputEvent>() {
			@Override
			public void handle(InputEvent event) {
				s.setShading(shadingCheckbox.isSelected());
				m.notifySettingsObservers();
			}
		});
		
		// Add all of the options to the options grid
		optGrid.add(musicLabel, 0, 0);
		optGrid.add(musicSlider, 1, 0);
		optGrid.add(sfxLabel, 0, 1);
		optGrid.add(sfxSlider, 1, 1);
		optGrid.add(usernameLabel, 0, 2);
		optGrid.add(usernameText, 1, 2);
		optGrid.add(shadingLabel, 0, 3);
		optGrid.add(shadingCheckbox, 1, 3);

		// Create a array of options for the cancel and apply buttons
		MenuOption[] set = {new MenuOption("Back", true, new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent event) {
		    	// Transition back to the main menu
		    	m.transitionTo(MenuEnum.MainMenu, null);
		    }     
		})};
		// Turn the array into a grid pane
		GridPane buttonGrid = MenuOptionSet.optionSetToGridPane(set);
		
		// Add the options grid and the button grid to the main grid
		mainGrid.add(optGrid, 0, 0);
		mainGrid.add(buttonGrid, 0, 1);
		
		// Create a new scene using the main grid
		m.addButtonHoverSounds(mainGrid);
		Scene scene = new Scene(mainGrid, m.width, m.height);
		scene.getStylesheets().add("styles/menu.css");
		return scene;
	}
}
