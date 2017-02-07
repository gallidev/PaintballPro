package gui;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class UserSettingsManager {
	
	public static UserSettings loadSettings() {
		return loadSettings(0);
	}
	public static UserSettings loadSettings(int i) {
		try {
			JsonReader reader = new JsonReader(new FileReader("preferences.json"));
			Gson gson = new Gson();
			UserSettings s = gson.fromJson(reader, UserSettings.class);
			reader.close();
			return s;
		} catch (Exception e) {
			e.printStackTrace();
			if (i < 2) {
				saveSettings(new UserSettings());
				return loadSettings(i + 1);
			} else {
				System.exit(1);
				return null;
			}
			
		}
	}
	
	public static void saveSettings(UserSettings s) {
		try {
			File file = new File("preferences.json");
			file.createNewFile();
			Gson gson = new Gson();
			String json = gson.toJson(s);
			PrintWriter out = new PrintWriter(file, "UTF-8");
			out.println(json);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
