package thegoldenproof.objectpriorities.json;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;
import thegoldenproof.objectpriorities.ObjectPriorities;

public interface GeneralSimpleItem {
	
	public static enum Serializer implements JsonSerializer<GeneralSimpleItem>, JsonDeserializer<GeneralSimpleItem> {
		INSTANCE;

		@Override
		public GeneralSimpleItem deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			
			String s = json.getAsString();
						
			if (s.startsWith("OD|")) {
				return GSIOreDict.create(s.substring("OD|".length()));
			} else if (s.startsWith("FLUID|")) {
				String fluidName = s.substring("FLUID|".length());
				Fluid fluid = FluidRegistry.getFluid(fluidName);
				if (fluid == null)
					throw new JsonParseException("Tried to identify nonexistent Fluid " + fluidName);
				return GSIFluid.create(fluid);
			} else {
				int pipeIndex = s.lastIndexOf('|');
				if (pipeIndex < 0)
				{
					throw new JsonParseException(String.format("Cannot parse '%s' as itemstack. Missing | to separate metadata.", s));
				}
				String itemName = s.substring(0, pipeIndex);
				String itemDamageString = s.substring(pipeIndex + 1);
				int itemDamage;
				if (itemDamageString.equals("*")) {
					itemDamage = OreDictionary.WILDCARD_VALUE;
				}
				else {
					try {
						itemDamage = Integer.parseInt(itemDamageString);
					} catch (NumberFormatException e) {
						throw new JsonParseException(String.format("Could not parse '%s' to metadata-integer", itemDamageString), e);
					}
				}

				return GSIItem.create(itemName, itemDamage);
			}
		}

		@Override
		public JsonElement serialize(GeneralSimpleItem src, Type typeOfSrc, JsonSerializationContext context) {
			return new JsonPrimitive(src.json());
		}
	}

	// "Explode" a wildcarded item into all of its variants
	static Iterable<GeneralSimpleItem> getVariants(String id) {
		Item i = Item.getByNameOrId(id);
		if (i == null) {
			ObjectPriorities.error("null item in getVariants");
			return Collections.emptyList();
		}

		// Adapted from JEI StackHelper.getSubtypes
		NonNullList<ItemStack> variants = NonNullList.create();
		for (CreativeTabs group : i.getCreativeTabs()) {
			if (group == null) {
				variants.add(new ItemStack(i));
			} else {
				NonNullList<ItemStack> subItems = NonNullList.create();
				try {
					i.getSubItems(group, subItems);
				} catch (RuntimeException | LinkageError e) {
					ObjectPriorities.warn("couldn't get variants of {}: {}", i, e);
				}

				for (ItemStack sub : subItems) {
					if (!sub.isEmpty()) {
						variants.add(sub);
					}
				}
			}
		}

		// collapse by metadata
		Set<GeneralSimpleItem> ret = new HashSet<>();
		for (ItemStack variant : variants) {
			ret.add(new GSIItem(variant.getItem().getRegistryName().toString(), variant.getItemDamage()));
		}
		return ret;
	}

	@Override
	public boolean equals(Object o);

	public String json();
	
}
