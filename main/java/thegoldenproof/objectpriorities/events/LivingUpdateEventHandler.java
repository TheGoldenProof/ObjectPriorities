package thegoldenproof.objectpriorities.events;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pilot.simplerpg.capabilities.IRpgPlayer;
import pilot.simplerpg.capabilities.RpgPlayerProvider;
import thegoldenproof.objectpriorities.ObjectPriorities;
import thegoldenproof.objectpriorities.config.ConfigFile;
import thegoldenproof.objectpriorities.util.PriorityHelper;
import thegoldenproof.objectpriorities.util.Reference;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class LivingUpdateEventHandler {
	
	@SubscribeEvent
	public static void heavyItems(LivingUpdateEvent event) {
		double sFactor = ConfigFile.effectCalcs.slownessFactor;
		double fFactor = ConfigFile.effectCalcs.fatigueFactor;
		
		EntityLivingBase entity = event.getEntityLiving();
		if (entity instanceof EntityPlayerMP) {
			EntityPlayerMP p = (EntityPlayerMP)(entity);
			int level;
			if (ObjectPriorities.simpleRPGLoaded && ConfigFile.general.useSimpleRPG) {
				IRpgPlayer rpg = (IRpgPlayer) p.getCapability(RpgPlayerProvider.RPG_PLAYER_CAP, (EnumFacing) null);
				level = rpg.getLevel();
			} else {
				level = p.experienceLevel;
			}
			
			int priority = -1;
			int currentHighest = -1;
			for (ItemStack stack : p.inventory.mainInventory) {
				if (PriorityHelper.doesItemHavePriority(stack)) {
					currentHighest = PriorityHelper.getPriority(stack) > currentHighest ? PriorityHelper.getPriority(stack) : currentHighest;
				}
			}
			for (ItemStack stack : p.inventory.armorInventory) {
				if (PriorityHelper.doesItemHavePriority(stack)) {
					currentHighest = PriorityHelper.getPriority(stack) > currentHighest ? PriorityHelper.getPriority(stack) : currentHighest;
				}
			}
			for (ItemStack stack : p.inventory.offHandInventory) {
				if (PriorityHelper.doesItemHavePriority(stack)) {
					currentHighest = PriorityHelper.getPriority(stack) > currentHighest ? PriorityHelper.getPriority(stack) : currentHighest;
				}
			}
			priority = currentHighest;
			if (priority > level) {
				p.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("slowness"), 10, (int)(Math.ceil((priority-level)/sFactor))));
				p.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("mining_fatigue"), 10, (int)(Math.ceil((priority-level)/fFactor))));
			}
		}
	}
	
}
