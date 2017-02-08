package gui;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

/**
 * Helper methods for loading and saving user settings
 */
public class UserSettingsManager {
	
	/**
	 * Load the user's settings from disk
	 * @return user's settings
	 */
	public static UserSettings loadSettings() {
		// Run load settings with a counter of 0
		// This should prevent infinite loops from occurring
		return loadSettings(0);
	}
	
	/**
	 * Load the user's settings from disk
	 * @param i counter to avoid an infinite loop (call with 0)
	 * @return user's settings
	 */
	private static UserSettings loadSettings(int i) {
		try {
			// Load the JSON file
			JsonReader reader = new JsonReader(new FileReader("res/preferences.json"));
			Gson gson = new Gson();
			// Turn the JSON into a user settings class
			UserSettings s = gson.fromJson(reader, UserSettings.class);
			reader.close();
			// Return the user's settings
			return s;
		} catch (Exception e) {
			// Check to see if this method has been called less than three times
			if (i < 2) {
				// Method called less than three times, so we should try to
				// save the default settings and then load the settings again
				saveSettings(new UserSettings());
				// Try loading again, incrementing the safety counter
				return loadSettings(i + 1);
			} else {
				// Method has been called three or more times, which shouldn't
				// happen under regular use, so exit the program
				e.printStackTrace();
				System.exit(1);
				return null;
			}
			
		}
	}
	
	/**
	 * Save the user's settings to disk
	 * @param s settings to save
	 */
	public static void saveSettings(UserSettings s) {
		try {
			// Create the file
			File file = new File("res/preferences.json");
			file.createNewFile();
			Gson gson = new Gson();
			// Convert the object to JSON
			String json = gson.toJson(s);
			// Save to the file
			PrintWriter out = new PrintWriter(file, "UTF-8");
			out.println(json);
			out.close();
		} catch (Exception e) {
			// Exception occurred, so exit
			e.printStackTrace();
			System.exit(1);
		}
	}
}
