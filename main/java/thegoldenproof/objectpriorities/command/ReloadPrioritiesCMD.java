package thegoldenproof.objectpriorities.command;

import javax.annotation.Nonnull;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import thegoldenproof.objectpriorities.config.PriorityParser;
import thegoldenproof.objectpriorities.network.PacketHandler;
import thegoldenproof.objectpriorities.priorities.PriorityMapper;

public class ReloadPrioritiesCMD extends CommandBase
{
	@Nonnull
	@Override
	public String getName()
	{
		return "reloadPriorities";
	}
	
	@Nonnull
	@Override
	public String getUsage(@Nonnull ICommandSender sender)
	{
		return "op.command.reload.usage";
	}

	@Override
	public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] params)
	{
		sender.sendMessage(new TextComponentTranslation("op.command.reload.started"));

		PriorityMapper.clearMaps();
		PriorityParser.init();
		PriorityMapper.map();

		sender.sendMessage(new TextComponentTranslation("op.command.reload.success"));

		PacketHandler.sendFragmentedPrioPacketToAll();
	}

	@Override
	public int getRequiredPermissionLevel() 
	{
		return 4;
	}
}