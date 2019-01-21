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
	
	public static EffectCalculations effectCalcs = new EffectCalculations();
	public static class EffectCalculations {
		@Config.Comment("Calculated like so: (priority-level)/x, where x is the factor. In easier terms, the level of slowness/fatigue is the difference between its and your level divided by this. (default: 10.0)")
		public double fatigueFactor = 10;
		public double slownessFactor = 10;
	}
	
}
