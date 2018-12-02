package thegoldenproof.objectpriorities.proxy;

import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import thegoldenproof.objectpriorities.ObjectPriorities;

public interface IProxy {
	
	default void logPhysicalSide() {
		ObjectPriorities.info("Physical Side: " + getPhysicalSide());
	}

	Side getPhysicalSide();
	
}
