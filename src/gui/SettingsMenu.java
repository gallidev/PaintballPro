package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class SettingsMenu {
	// TODO: menu for displaying and changing UserSettings
	
	public static Scene getScene(GUIManager m) {
		// TODO Auto-generated method stub
		
		UserSettings s = m.getUserSettings();
		
		GridPane mainGrid = new GridPane();
		mainGrid.setAlignment(Pos.CENTER);
		mainGrid.setHgap(10);
		mainGrid.setVgap(10);
		mainGrid.setPadding(new Insets(25, 25, 25, 25));
		
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
		
		GridPane topGrid = new GridPane();
		topGrid.setAlignment(Pos.CENTER);
		topGrid.setHgap(10);
		topGrid.setVgap(10);
		topGrid.setPadding(new Insets(25, 25, 25, 25));
		
		
		Label usernameLabel = new Label("Username");
		TextField usernameText = new TextField();
		usernameText.setText(s.getUsername());
		
		topGrid.add(musicLabel, 0, 0);
		topGrid.add(musicSlider, 1, 0);
		topGrid.add(sfxLabel, 0, 1);
		topGrid.add(sfxSlider, 1, 1);
		topGrid.add(usernameLabel, 0, 2);
		topGrid.add(usernameText, 1, 2);
		
		String currentCharacter = s.getCharacterStyle();
		String currentWeapon = s.getWeaponStyle();
		
		ImageView characterImage = new ImageView(currentCharacter);
		characterImage.setId(currentCharacter);
		ImageView weaponImage = new ImageView(currentWeapon);
		weaponImage.setId(currentWeapon);
		
		
		Button characterButton = new Button();
		characterButton.setText("Change");
		characterButton.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent event) {
		    	characterImage.setId(CharacterStyles.getNext(characterImage.getId()));
		    	characterImage.setImage(new Image(characterImage.getId()));
		        System.out.println("ActionEvent: " + event);
		    }     
		});		
		topGrid.add(MenuControls.centreInPane(characterImage), 0, 3);
		topGrid.add(MenuControls.centreInPane(characterButton), 0, 4);
		
		
		Button weaponButton = new Button();
		weaponButton.setText("Change");
		weaponButton.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent event) {
		    	weaponImage.setId(WeaponStyles.getNext(weaponImage.getId()));
		    	weaponImage.setImage(new Image(weaponImage.getId()));
		        System.out.println("ActionEvent: " + event);
		    }     
		});	
		topGrid.add(MenuControls.centreInPane(weaponImage), 1, 3);
		topGrid.add(MenuControls.centreInPane(weaponButton), 1, 4);
		
		
		MenuOption[] set = {new MenuOption("Cancel", new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent event) {
		    	m.transitionTo("Main", null);
		        System.out.println("ActionEvent: " + event);
		    }     
		}), new MenuOption("Apply", new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent event) {
		    	s.setCharacterStyle(characterImage.getId());
		    	s.setWeaponStyle(weaponImage.getId());
		    	s.setUsername(usernameText.getText());
		    	s.setMusicVolume((int) musicSlider.getValue());
		    	s.setSfxVolume((int) sfxSlider.getValue());
		    	m.transitionTo("Main", null);
		        System.out.println("ActionEvent: " + event);
		    }     
		})};
		
		GridPane buttonGrid = MenuOptionSet.optionSetToGridPane(set);
		
		
		mainGrid.add(topGrid, 0, 0);
		mainGrid.add(buttonGrid, 0, 1);
		
		return new Scene(mainGrid, m.width, m.height);
	}
}
