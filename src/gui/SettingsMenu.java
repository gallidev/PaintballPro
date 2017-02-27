package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
		UserSettings s = m.getUserSettings();
		
		// Create the main grid (to contain the options grid, and the apply/cancel buttons)
		GridPane mainGrid = new GridPane();
		mainGrid.setAlignment(Pos.CENTER);
		mainGrid.setHgap(10);
		mainGrid.setVgap(10);
		mainGrid.setPadding(new Insets(25, 25, 25, 25));
		
		// Create the top grid (grid to contain all possible options)
		GridPane topGrid = new GridPane();
		topGrid.setAlignment(Pos.CENTER);
		topGrid.setHgap(10);
		topGrid.setVgap(10);
		topGrid.setPadding(new Insets(25, 25, 25, 25));
		
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
		
		// Create the username label and text field
		Label usernameLabel = new Label("Username");
		
		TextField usernameText = new TextField();
		usernameText.setText(s.getUsername());
		
		// Create the shading option label and checkbox
		Label shadingLabel = new Label("Use shading (default on)");
		
		CheckBox shadingCheckbox = new CheckBox();
		shadingCheckbox.setSelected(s.getShading());
		
		
		// Get the current character resource
		String currentCharacter = s.getCharacterStyle();
		// Create the current character image view, and set the
		// ID (the string containing the path to the resource)
		ImageView characterImage = new ImageView(currentCharacter);
		characterImage.setId(currentCharacter);
		
		// Create the button to change the look of the character
		Button characterButton = new Button();
		// Add the text to the button
		characterButton.setText("Change");
		// Set the event handler for the button
		characterButton.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent event) {
		    	// Update the ID to be the next possible ID
		    	characterImage.setId(CharacterStyles.getNext(characterImage.getId()));
		    	// Change the image to be the new ID (path to resource)
		    	characterImage.setImage(new Image(characterImage.getId()));
		    }     
		});		
		
		// Get the current weapon resource
		String currentWeapon = s.getWeaponStyle();
		// Create the current weapon image view, and set the
		// ID (the string containing the path to the resource)
		ImageView weaponImage = new ImageView(currentWeapon);
		weaponImage.setId(currentWeapon);
		
		// Create the button to change the look of the weapon
		Button weaponButton = new Button();
		// Add the text to the button
		weaponButton.setText("Change");
		// Set the event handler for the button
		weaponButton.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent event) {
		    	// Update the ID to be the next possible ID
		    	weaponImage.setId(WeaponStyles.getNext(weaponImage.getId()));
		    	// Change the image to be the new ID (path to resource)
		    	weaponImage.setImage(new Image(weaponImage.getId()));
		    }     
		});	
		
		// Add all of the options to the options grid
		topGrid.add(musicLabel, 0, 0);
		topGrid.add(musicSlider, 1, 0);
		topGrid.add(sfxLabel, 0, 1);
		topGrid.add(sfxSlider, 1, 1);
		topGrid.add(usernameLabel, 0, 2);
		topGrid.add(usernameText, 1, 2);
		topGrid.add(shadingLabel, 0, 3);
		topGrid.add(shadingCheckbox, 1, 3);
		topGrid.add(MenuControls.centreInPane(characterImage), 0, 4);
		topGrid.add(MenuControls.centreInPane(characterButton), 0, 5);
		topGrid.add(MenuControls.centreInPane(weaponImage), 1, 4);
		topGrid.add(MenuControls.centreInPane(weaponButton), 1, 5);
		
		// Create a array of options for the cancel and apply buttons
		MenuOption[] set = {new MenuOption("Cancel", new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent event) {
		    	// Cancel - so transition back to the main menu without setting anything
		    	m.transitionTo("Main", null);
		    }     
		}), new MenuOption("Apply", new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent event) {
		    	// Update the preferences (these will automatically be saved
		    	// when set is called)
		    	s.setCharacterStyle(characterImage.getId());
		    	s.setWeaponStyle(weaponImage.getId());
		    	s.setUsername(usernameText.getText());
		    	s.setMusicVolume((int) musicSlider.getValue());
		    	s.setSfxVolume((int) sfxSlider.getValue());
		    	s.setShading(shadingCheckbox.isSelected());
		    	m.notifySettingsObservers();
		    	// Transition back to the main menu
		    	m.transitionTo("Main", null);
		    }     
		})};
		
		// Turn the array into a grid pane
		GridPane buttonGrid = MenuOptionSet.optionSetToGridPane(set);
		
		// Add the options grid and the button grid to the main grid
		mainGrid.add(topGrid, 0, 0);
		mainGrid.add(buttonGrid, 0, 1);
		
		// Create a new scene using the main grid
		m.addButtonHoverSounds(mainGrid);
		Scene scene = new Scene(mainGrid, m.width, m.height);
		scene.getStylesheets().add("styles/menu.css");
		return scene;
	}
}
