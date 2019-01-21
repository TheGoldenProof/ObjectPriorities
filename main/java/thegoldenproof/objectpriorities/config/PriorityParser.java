package thegoldenproof.objectpriorities.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.annotations.SerializedName;

import thegoldenproof.objectpriorities.ObjectPriorities;
import thegoldenproof.objectpriorities.json.GSIItem;
import thegoldenproof.objectpriorities.json.GSIOreDict;
import thegoldenproof.objectpriorities.json.GeneralSimpleItem;

public final class PriorityParser
{
	private static final Gson GSON = new GsonBuilder().registerTypeAdapter(GeneralSimpleItem.class, GeneralSimpleItem.Serializer.INSTANCE).setPrettyPrinting().create();
	private static final File CONFIG = new File(ObjectPriorities.CONFIG_DIR, "priorities.json");

	public static class PriorityFile
	{
		public final List<PriorityEntry> entries;

		public PriorityFile(List<PriorityEntry> entries)
		{
			this.entries = entries;
		}
	}

	public static class PriorityEntry
	{
		@SerializedName("item")
		public final GeneralSimpleItem gsi;
		public final int priority;

		private PriorityEntry(GeneralSimpleItem gsi, int priority)
		{
			this.gsi = gsi;
			this.priority = priority;
		}

		@Override
		public boolean equals(Object o)
		{
			return o == this || o instanceof PriorityEntry && gsi.equals(((PriorityEntry) o).gsi) && priority == ((PriorityEntry) o).priority;
		}

		@Override
		public int hashCode() {
			int result = gsi != null ? gsi.hashCode() : 0;
			result = 31 * result + (int) (priority ^ (priority >>> 32));
			return result;
		}
	}

	public static PriorityFile currentEntries;
	private static boolean dirty = false;

	public static void init()
	{
		flush();

		if (!CONFIG.exists())
		{
			try
			{
				if (CONFIG.createNewFile())
				{
					writeDefaultFile();
				}
			}
			catch (IOException e)
			{
				ObjectPriorities.fatal("Exception in file I/O: couldn't create custom configuration files.");
			}
		}

		try (BufferedReader reader = new BufferedReader(new FileReader(CONFIG))) {
			currentEntries = GSON.fromJson(reader, PriorityFile.class);
			currentEntries.entries.removeIf(e -> e.gsi == null || e.priority < 0 || !(e.gsi instanceof GSIItem || e.gsi instanceof GSIOreDict));
		} catch (IOException | JsonParseException e) {
			ObjectPriorities.fatal("Couldn't read custom priority file");
			e.printStackTrace();
			currentEntries = new PriorityFile(new ArrayList<>());
		}
	}

	private static GeneralSimpleItem getGsi(String str, int meta)
	{
		if (str.contains(":"))
		{
			return GSIItem.create(str, meta);
		}
		else
		{
			return GSIOreDict.create(str);
		}
	}
	
	public static boolean alwaysAddToFile(String toAdd, int meta, int priority) {
		
		GeneralSimpleItem gsi = getGsi(toAdd, meta);
		PriorityEntry entry = new PriorityEntry(gsi, priority);
		
		int setAt = -1;
		
		for (int i = 0; i < currentEntries.entries.size(); i++)
		{
			if (currentEntries.entries.get(i).gsi.equals(gsi))
			{
				setAt = i;
				break;
			}
		}

		if (setAt == -1)
		{
			currentEntries.entries.add(entry);
		} else
		{
			currentEntries.entries.set(setAt, entry);
		}
		
		dirty = true;
		return true;
	}

	public static boolean addToFile(String toAdd, int meta, int priority)
	{
		GeneralSimpleItem gsi = getGsi(toAdd, meta);
		PriorityEntry entry = new PriorityEntry(gsi, priority);
		
		boolean flag = true;
		for (int i = 0; i < currentEntries.entries.size(); i++) {
			if (currentEntries.entries.get(i).gsi.equals(gsi)) {
				if (currentEntries.entries.get(i).priority != priority && currentEntries.entries.get(i).priority != 0) {
					flag = false;
				} else {
					flag = true;
				}
			}
		}
		
		//ObjectPriorities.debug(flag);
		
		if (flag) {
			return alwaysAddToFile(toAdd, meta, priority);
		} else {return true;}
		
	}

	public static boolean removeFromFile(String toRemove, int meta)
	{
		GeneralSimpleItem gsi = getGsi(toRemove, meta);
		Iterator<PriorityEntry> iter = currentEntries.entries.iterator();

		boolean removed = false;
		while (iter.hasNext())
		{
			if (iter.next().gsi.equals(gsi))
			{
				iter.remove();
				dirty = true;
				removed = true;
			}
		}

		return removed;
	}
	
	public static void clearAll() {
		CONFIG.delete();
	}

	private static void flush()
	{
		if (dirty)
		{
			try
			{
				Files.write(GSON.toJson(currentEntries), CONFIG, Charsets.UTF_8);
			} catch (IOException e) {
				e.printStackTrace();
			}

			dirty = false;
		}
	}

	private static void writeDefaultFile()
	{
		JsonObject elem = (JsonObject) GSON.toJsonTree(new PriorityFile(new ArrayList<>()));
		elem.add("__comment", new JsonPrimitive("Use the in-game commands to edit this file"));
		try
		{
			Files.write(GSON.toJson(elem), CONFIG, Charsets.UTF_8);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}