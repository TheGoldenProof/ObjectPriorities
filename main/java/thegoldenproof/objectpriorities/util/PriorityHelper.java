package thegoldenproof.objectpriorities.util;

import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import thegoldenproof.objectpriorities.priorities.PriorityMapper;

public class PriorityHelper {
	
	public static final int ENCH_PRIORITY_BONUS = 1;
	
	public static boolean doesBlockHavePriority(Block block)
	{
		return block != null && doesItemHavePriority(new ItemStack(block));
	}

	public static boolean doesItemHavePriority(ItemStack stack)
	{
		if (stack.isEmpty())
		{
			return false;
		}

		SimpleStack iStack = new SimpleStack(stack);

		if (!iStack.isValid())
		{
			return false;
		}

		if (ItemHelper.isDamageable(stack))
		{
			iStack = iStack.withMeta(0);
		}

		return PriorityMapper.mapContains(iStack);
	}

	public static boolean doesItemHavePriority(Item item)
	{
		return item != null && doesItemHavePriority(new ItemStack(item));
	}
	
	public static int getPriority(Block block)
	{
		SimpleStack stack = new SimpleStack(new ItemStack(block));

		if (stack.isValid() && PriorityMapper.mapContains(stack))
		{
			return PriorityMapper.getPriority(stack);
		}

		return 0;
	}

	public static int getPriority(Item item)
	{
		SimpleStack stack = new SimpleStack(new ItemStack(item));

		if (stack.isValid() && PriorityMapper.mapContains(stack))
		{
			return PriorityMapper.getPriority(stack);
		}

		return 0;
	}

	/**
	 * Does not consider stack size
	 */
	public static int getPriority(ItemStack stack)
	{
		if (stack.isEmpty())
		{
			return 0;
		}

		SimpleStack iStack = new SimpleStack(stack);

		if (!iStack.isValid())
		{
			return 0;
		}

		if (!PriorityMapper.mapContains(iStack) && ItemHelper.isDamageable(stack))
		{
			//We don't have an priority value for id:metadata, so lets check if we have a value for id:0 and apply a damage multiplier based on that priority value.
			iStack = iStack.withMeta(0);

			if (PriorityMapper.mapContains(iStack))
			{
				int priority = PriorityMapper.getPriority(iStack);

				// maxDmg + 1 because vanilla lets you use the tool one more time
				// when item damage == max damage (shows as Durability: 0 / max)
				int relDamage = (stack.getMaxDamage() + 1 - stack.getItemDamage());

				if (relDamage <= 0)
				{
					// This may happen when mods overflow their max damage or item damage.
					// Don't use durability or enchants for priority calculation if this happens.
					return priority;
				}

				int result = priority * relDamage;

				if (result <= 0)
				{
					//Congratulations, big number is big.
					return priority;
				}

				result /= stack.getMaxDamage();
				boolean positive = result > 0;
				result += getEnchantPriorityBonus(stack);

				//If it was positive and then became negative that means it overflowed
				if (positive && result < 0) {
					return priority;
				}

				positive = result > 0;

				//If it was positive and then became negative that means it overflowed
				if (positive && result < 0) {
					return priority;
				}

				if (result <= 0)
				{
					return 1;
				}

				return result;
			}
		}
		else
		{
			if (PriorityMapper.mapContains(iStack))
			{
				return PriorityMapper.getPriority(iStack) + getEnchantPriorityBonus(stack);
			}
		}

		return 0;
	}
	
	private static int getEnchantPriorityBonus(ItemStack stack)
	{
		int result = 0;

		Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(stack);

		if (!enchants.isEmpty())
		{
			for (Map.Entry<Enchantment, Integer> entry : enchants.entrySet())
			{
				Enchantment ench = entry.getKey();
				if (ench == null || ench.getRarity().getWeight() == 0)
				{
					continue;
				}

				result += ENCH_PRIORITY_BONUS / ench.getRarity().getWeight() * entry.getValue();
			}
		}

		return result;
	}
	
}
