package thegoldenproof.objectpriorities.command;

import javax.annotation.Nonnull;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import thegoldenproof.objectpriorities.config.PriorityParser;
import thegoldenproof.objectpriorities.util.MathUtils;

public class RemovePriorityCMD extends CommandBase
{
	@Nonnull
	@Override
	public String getName()
	{
		return "removePriority";
	}

	@Nonnull
	@Override
	public String getUsage(@Nonnull ICommandSender sender)
	{
		return "op.command.remove.usage";
	}
	
	@Override
	public int getRequiredPermissionLevel() 
	{
		return 4;
	}

	@Override
	public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] params) throws CommandException
	{
		String name;
		int meta = 0;

		if (params.length == 0)
		{
			ItemStack heldItem = getCommandSenderAsPlayer(sender).getHeldItem(EnumHand.MAIN_HAND);
			if (heldItem.isEmpty())
			{
				heldItem = getCommandSenderAsPlayer(sender).getHeldItem(EnumHand.OFF_HAND);
			}

			if (heldItem.isEmpty())
			{
				throw new WrongUsageException(getUsage(sender));
			}

			name = heldItem.getItem().getRegistryName().toString();
			meta = heldItem.getItemDamage();
		}
		else
		{
			name = params[0];

			if (params.length > 1)
			{
				meta = MathUtils.parseInteger(params[1]);

				if (meta < 0)
				{
					throw new CommandException("op.command.remove.invalidmeta", params[1]);
				}
			}
		}

		if (PriorityParser.addToFile(name, meta, 0))
		{
			sender.sendMessage(new TextComponentTranslation("op.command.remove.success", name));
			sender.sendMessage(new TextComponentTranslation("op.command.reload.notice"));
		}
		else
		{
			throw new CommandException("op.command.remove.invaliditem", name);
		}
	}
}