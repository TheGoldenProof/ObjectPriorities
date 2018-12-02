package thegoldenproof.objectpriorities.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import thegoldenproof.objectpriorities.ObjectPriorities;
import thegoldenproof.objectpriorities.priorities.PriorityMapper;
import thegoldenproof.objectpriorities.util.SimpleStack;

public class SyncPrioPKT implements IMessage
{
	private PrioPKTInfo[] data;

	public SyncPrioPKT() {}

	public SyncPrioPKT(PrioPKTInfo[] data)
	{
		this.data = data;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		int size = ByteBufUtils.readVarInt(buf, 5);
		data = new PrioPKTInfo[size];

		for (int i = 0; i < size; i++)
		{
			data[i] = new PrioPKTInfo(ByteBufUtils.readVarInt(buf, 5), ByteBufUtils.readVarInt(buf, 5), buf.readInt());
		}
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeVarInt(buf, data.length, 5);

		for (PrioPKTInfo info : data)
		{
			ByteBufUtils.writeVarInt(buf, info.getId(), 5);
			ByteBufUtils.writeVarInt(buf, info.getDamage(), 5);
			buf.writeInt(info.getPriority());
		}
	}

	public static class Handler implements IMessageHandler<SyncPrioPKT, IMessage>
	{
		@Override
		public IMessage onMessage(final SyncPrioPKT pkt, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(new Runnable() {
				@Override
				public void run() {
					ObjectPriorities.info("Receiving priority data from server.");
					PriorityMapper.priorities.clear();

					for (PrioPKTInfo info : pkt.data)
					{
						Item i = Item.REGISTRY.getObjectById(info.getId());

						SimpleStack stack = new SimpleStack(i.getRegistryName(), info.getDamage());

						if (stack.isValid())
						{
							PriorityMapper.priorities.put(stack, info.getPriority());
						}
					}
				}
			});

			return null;
		}
	}

	public static class PrioPKTInfo {
		private int id, damage;
		private int prio;

		public PrioPKTInfo(int id, int damage, int prio) {
			this.id = id;
			this.damage = damage;
			this.prio = prio;
		}

		public int getDamage() {
			return damage;
		}

		public int getId() {
			return id;
		}

		public int getPriority() {
			return prio;
		}
	}
}