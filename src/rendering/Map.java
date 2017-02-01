package rendering;

import com.google.gson.stream.JsonReader;

import java.io.FileReader;

class Map
{
	Map(String url)
	{
		ClassLoader loader = getClass().getClassLoader();

		try
		{
			JsonReader reader = new JsonReader(new FileReader(loader.getResource(url).toString()));
			System.out.println(reader.nextName() + ": " + reader.nextString());
			reader.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
