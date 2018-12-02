package thegoldenproof.objectpriorities.proxy;

import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;

public class ServerProxy implements IProxy {

	@Override
	public Side getPhysicalSide() {
		return Side.SERVER;
	}
	
}
