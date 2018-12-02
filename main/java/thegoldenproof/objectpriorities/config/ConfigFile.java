package thegoldenproof.objectpriorities.config;

import net.minecraftforge.common.config.Config;
import thegoldenproof.objectpriorities.util.Reference;

@Config(modid = Reference.MOD_ID, name = Reference.NAME + "/" + Reference.NAME)
public class ConfigFile {
	
	public static final General general = new General();
	public static class General {
		
		@Config.Comment("Use SimpleRPG's level for Object Control Authority (if installed) instead of Minecraft's player level? (default: true)")
		public boolean useSimpleRPG = true;
		
	}
}
