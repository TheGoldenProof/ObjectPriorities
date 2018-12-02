package thegoldenproof.objectpriorities.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import thegoldenproof.objectpriorities.config.PriorityParser;

public class ClearPrioritiesCMD extends CommandBase {

	@Override
	public String getName() {
		return "clearPriorities";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "CAREFUL! Removes ALL priorities from ALL items!";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		PriorityParser.clearAll();

		sender.sendMessage(new TextComponentTranslation("op.command.reload.notice"));
		
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 4;
	}

}
