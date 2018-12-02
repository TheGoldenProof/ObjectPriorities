package thegoldenproof.objectpriorities.network;

import java.util.Map;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import thegoldenproof.objectpriorities.network.SyncPrioPKT.PrioPKTInfo;
import thegoldenproof.objectpriorities.priorities.PriorityMapper;
import thegoldenproof.objectpriorities.util.Reference;
import thegoldenproof.objectpriorities.util.SimpleStack;

public class PacketHandler {
	
	private static final SimpleNetworkWrapper HANDLER = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);
	
	public static void register() {
		int disc = 0;
		HANDLER.registerMessage(SyncPrioPKT.Handler.class, SyncPrioPKT.class, disc++, Side.CLIENT);
	}
	
	public static void sendNonLocal(IMessage msg, EntityPlayerMP player)
	{
		if (player.mcServer.isDedicatedServer() || !player.getName().equals(player.mcServer.getServerOwner()))
		{
			HANDLER.sendTo(msg, player);
		}
	}
	
	public static void sendFragmentedPrioPacketToAll()
	{
		SyncPrioPKT pkt = new SyncPrioPKT(serializePrioData());
		for (EntityPlayerMP player : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers())
		{
			sendNonLocal(pkt, player);
		}
	}
	
	private static PrioPKTInfo[] serializePrioData()
	{
		PrioPKTInfo[] ret = new PrioPKTInfo[PriorityMapper.priorities.size()];
		int i = 0;
		for (Map.Entry<SimpleStack, Integer> entry : PriorityMapper.priorities.entrySet())
		{
			SimpleStack stack = entry.getKey();
			int id = Item.REGISTRY.getIDForObject(Item.REGISTRY.getObject(stack.id));
			ret[i] = new PrioPKTInfo(id, stack.damage, entry.getValue());
			i++;
		}
		return ret;
	}
	
}
