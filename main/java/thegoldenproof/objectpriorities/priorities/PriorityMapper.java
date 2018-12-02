package thegoldenproof.objectpriorities.priorities;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import thegoldenproof.objectpriorities.ObjectPriorities;
import thegoldenproof.objectpriorities.config.PriorityParser;
import thegoldenproof.objectpriorities.events.PriorityRemapEvent;
import thegoldenproof.objectpriorities.json.GSIItem;
import thegoldenproof.objectpriorities.json.GeneralSimpleItem;
import thegoldenproof.objectpriorities.json.Pregenerated;
import thegoldenproof.objectpriorities.util.SimpleStack;

public class PriorityMapper {
	
	public static final Map<SimpleStack, Integer> priorities = new LinkedHashMap<>();
	
	public static void map() {
		Map<GeneralSimpleItem, Integer> pregenvals = new HashMap<>();
		//Pregenerated.tryRead(ObjectPriorities.PREGENERATED_PRIORITY_FILE, pregenvals = new HashMap<>());
		addMappings(pregenvals);
		
		for (Map.Entry<GeneralSimpleItem, Integer> entry: pregenvals.entrySet()) {
			GSIItem gsiItem = (GSIItem)entry.getKey();
			Item obj = Item.REGISTRY.getObject(new ResourceLocation(gsiItem.itemName));
			if (obj != null)
			{
				priorities.put(new SimpleStack(obj.getRegistryName(), gsiItem.damage), entry.getValue());
			} else {
				ObjectPriorities.warn("Could not add priority for {}|{}. Can not get ItemID!", gsiItem.itemName, gsiItem.damage);
			}
		}

		MinecraftForge.EVENT_BUS.post(new PriorityRemapEvent());
	}
	
	public static void addMappings(Map<GeneralSimpleItem, Integer> map) {
		for (PriorityParser.PriorityEntry entry : PriorityParser.currentEntries.entries) {
			map.put(entry.gsi, entry.priority);
		}
	}
	
	public static boolean mapContains(SimpleStack stack) {
		return priorities.containsKey(stack);
	}
	
	public static int getPriority(SimpleStack stack) {
		return priorities.get(stack);
	}
	
	public static void clearMaps() {
		priorities.clear();
	}
	
}
