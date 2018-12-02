package thegoldenproof.objectpriorities.json;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Pregenerated
{
	private static final Gson gson =  new GsonBuilder()
			.registerTypeAdapter(GeneralSimpleItem.class, GeneralSimpleItem.Serializer.INSTANCE)
			.enableComplexMapKeySerialization().setPrettyPrinting().create();

	public static boolean tryRead(File f, Map<GeneralSimpleItem, Integer> map)
	{
		try {
			Map<GeneralSimpleItem, Integer> m = read(f);
			map.clear();
			map.putAll(m);
			return true;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static Map<GeneralSimpleItem, Integer> read(File file) throws IOException
	{
		Type type = new TypeToken<Map<GeneralSimpleItem, Integer>>() {}.getType();
		try (BufferedReader reader = new BufferedReader(new FileReader(file)))
		{
			Map<GeneralSimpleItem, Integer> map = gson.fromJson(reader, type);
			map.remove(null);
			return map;
		}
	}

	public static void write(File file, Map<GeneralSimpleItem, Integer> map) throws IOException
	{
		Type type = new TypeToken<Map<GeneralSimpleItem, Integer>>() {}.getType();
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file)))
		{
			gson.toJson(map, type, writer);
		}
	}
}