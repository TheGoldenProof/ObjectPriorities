package thegoldenproof.objectpriorities.proxy;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;

public class ClientProxy implements IProxy {

	@Override
	public Side getPhysicalSide() {
		return Side.CLIENT;
	}
	
}
