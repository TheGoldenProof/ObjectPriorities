package thegoldenproof.objectpriorities.json;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import thegoldenproof.objectpriorities.ObjectPriorities;

public class GSIItem implements GeneralSimpleItem {
	static final Set<String> seenIds = new HashSet<>();

	public final String itemName;
	public final int damage;

	GSIItem(String itemName, int damage) {
		this.itemName = itemName;
		this.damage = damage;
	}

	public static GeneralSimpleItem create(Block block) {
		return create(block, 0);
	}

	public static GeneralSimpleItem create(Block block, int meta) {
		return create(block.getRegistryName(), meta);
	}

	public static GeneralSimpleItem create(ItemStack stack) {
		if (stack.isEmpty()) return null;
		return create(stack.getItem(), stack.getItemDamage());
	}

	public static GeneralSimpleItem create(Item item) {
		return create(item, 0);
	}

	private static GeneralSimpleItem create(Item item, int meta) {

		return create(item.getRegistryName(), meta);
	}

	private static GeneralSimpleItem create(ResourceLocation uniqueIdentifier, int damage) {
		if (uniqueIdentifier == null) return null;
		return create(uniqueIdentifier.toString(), damage);
	}

	public static GeneralSimpleItem create(String itemName, int damage) {
		GSIItem normStack;
		try {
			normStack = new GSIItem(itemName, damage);
		} catch (Exception e) {
			ObjectPriorities.fatal("Could not create GSIItem: {}", e.getMessage());
			return null;
		}
		seenIds.add(itemName);
		return normStack;
	}

	@Override
	public int hashCode() {
		return itemName.hashCode() ^ damage;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof GSIItem) {
			GSIItem other = (GSIItem) obj;

			return this.itemName.equals(other.itemName) && this.damage == other.damage;
		}

		return false;
	}

	@Override
	public String json() {
		return String.format("%s|%s", itemName, damage == OreDictionary.WILDCARD_VALUE ? "*" : damage);
	}

	@Override
	public String toString() {
		return String.format("%s:%s", itemName, damage == OreDictionary.WILDCARD_VALUE ? "*" : damage);
	}
}