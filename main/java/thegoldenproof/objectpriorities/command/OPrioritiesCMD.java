package thegoldenproof.objectpriorities.command;

import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;

public class OPrioritiesCMD extends CommandTreeBase {
	
	public OPrioritiesCMD() {
		addSubcommand(new SetPriorityCMD());
		addSubcommand(new ReloadPrioritiesCMD());
		addSubcommand(new RemovePriorityCMD());
		addSubcommand(new ClearPrioritiesCMD());
	}

	@Override
	public String getName() {
		return "priorities";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "op.command.main.usage";
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}
	

}
