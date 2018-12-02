package thegoldenproof.objectpriorities.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import thegoldenproof.objectpriorities.config.PriorityParser;
import thegoldenproof.objectpriorities.util.MathUtils;

public class SetPriorityCMD extends CommandBase {

	@Override
	public String getName() {
		return "setPriority";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "op.command.set.usage";
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 4;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		
		if (args.length < 1) {
			throw new WrongUsageException(getUsage(sender));
		}
		
		String name;
		int meta;
		int priority;
		
		if (args.length == 1) {
			
			ItemStack heldItem = getCommandSenderAsPlayer(sender).getHeldItem(EnumHand.MAIN_HAND);
			
			if (heldItem.isEmpty()) {
				heldItem = getCommandSenderAsPlayer(sender).getHeldItem(EnumHand.OFF_HAND);
			}
			if (heldItem.isEmpty()) {
				throw new WrongUsageException(getUsage(sender));
			}
			
			name = heldItem.getItem().getRegistryName().toString();
			meta = heldItem.getItemDamage();
			priority = Integer.parseInt(args[0]);
			
			if (priority < 0) {
				throw new NumberInvalidException("op.command.set.invalidpriority", args[0]);
			}
			
		} else {
			
			name = args[0];
			meta = 0;
			boolean isOD = !name.contains(":");
			
			if (!isOD) {

				if (args.length > 2) {

					meta = MathUtils.parseInteger(args[1]);

					if (meta < 0) {
						throw new CommandException("op.command.set.invalidmeta", args[1]);
					}

					priority = MathUtils.parseInteger(args[2]);

					if (priority < 0) {
						throw new CommandException("op.command.set.invalidpriority", args[2]);
					}

				} else {

					priority = MathUtils.parseInteger(args[1]);

					if (priority < 0) {
						throw new NumberInvalidException("op.command.set.invalidpriority", args[1]);
					}

				}

			} else {

				priority = MathUtils.parseInteger(args[1]);

				if (priority < 0) {
					throw new NumberInvalidException("op.command.set.invalidpriority", args[1]);
				}

			}
			
		}
		
		if (PriorityParser.addToFile(name, meta, priority))
		{
			sender.sendMessage(new TextComponentTranslation("op.command.set.success", name, priority));
			sender.sendMessage(new TextComponentTranslation("op.command.reload.notice"));
		}
		else
		{
			throw new CommandException("op.command.set.invaliditem", name);
		}
		
	}

}
