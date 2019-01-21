package thegoldenproof.objectpriorities.events;

import java.text.DecimalFormat;

import com.google.common.math.LongMath;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import thegoldenproof.objectpriorities.ObjectPriorities;
import thegoldenproof.objectpriorities.priorities.PriorityMapper;
import thegoldenproof.objectpriorities.util.PriorityHelper;
import thegoldenproof.objectpriorities.util.Reference;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = Reference.MOD_ID)
public class TooltipEvent {
	
	@SubscribeEvent
	public static void applyTooltips(ItemTooltipEvent event) {
		
		ItemStack current = event.getItemStack();
		final DecimalFormat FORMATTER = new DecimalFormat("#,###.##");
		
		if(PriorityHelper.doesItemHavePriority(current)) {
			
			int priority = PriorityHelper.getPriority(current);
			EntityPlayer clientPlayer = Minecraft.getMinecraft().player;
			
			event.getToolTip().add(TextFormatting.LIGHT_PURPLE + I18n.format("op.priority.priority_tooltip_prefix") + " " + /*TextFormatting.WHITE +*/ FORMATTER.format(priority));
			
		}
		
	}
	
}
