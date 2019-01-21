package thegoldenproof.objectpriorities;

import java.io.File;
import java.lang.reflect.Field;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import thegoldenproof.objectpriorities.command.OPrioritiesCMD;
import thegoldenproof.objectpriorities.config.PriorityParser;
import thegoldenproof.objectpriorities.network.PacketHandler;
import thegoldenproof.objectpriorities.priorities.PriorityMapper;
import thegoldenproof.objectpriorities.proxy.IProxy;
import thegoldenproof.objectpriorities.util.Reference;

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION, dependencies = "required-after:simplerpg")
public class ObjectPriorities {
	
	public static File CONFIG_DIR;
	public static boolean simpleRPGLoaded = false;
	
	@Instance
	public static ObjectPriorities instance;
	
	private static Logger logger;
	
	@SidedProxy(clientSide = "thegoldenproof.objectpriorities.proxy.ClientProxy", serverSide = "thegoldenproof.objectpriorities.proxy.ServerProxy")
	public static IProxy proxy;
	
	@EventHandler
	public static void PreInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		proxy.logPhysicalSide();
		
		CONFIG_DIR = new File(event.getModConfigurationDirectory(), Reference.NAME);
		
		if (!CONFIG_DIR.exists())
		{
			CONFIG_DIR.mkdirs();
		}
		
		//PREGENERATED_PRIORITY_FILE = new File(CONFIG_DIR, "priorities.json");

		PacketHandler.register();
		
	}
	
	@EventHandler
	public static void init(FMLInitializationEvent event) {
		
	}
	
	@EventHandler
	public static void PostInit(FMLPostInitializationEvent event) {
		simpleRPGLoaded = Loader.isModLoaded("simplerpg");
		info(simpleRPGLoaded ?
				"SimpleRPG's player level will be used as Object Control Authority. This can be disabled in config.":
				"SimpleRPG is not loaded. Minecraft's player level will be used instead.");
	}
	
	@Mod.EventHandler
	public void serverStarting(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new OPrioritiesCMD());

		long start = System.currentTimeMillis();

		info("Starting server-side priority mapping.");
		
		PriorityParser.init();
		PriorityMapper.map();

		info("Registered " + "" + " priority values. (took " + (System.currentTimeMillis() - start) + " ms)");
	}
	
	private static Logger getLogger() {
		if (logger == null) {
			final Logger temp_logger = LogManager.getLogger();
			temp_logger.error("[" + ObjectPriorities.class.getSimpleName() + "]: getLogger called before logger has been initalised! Providing default logger");
			return temp_logger;
		}
		return logger;
	}
	
	
	
	//Logging
	
	public static void debug(final Object... messages) {
		for (final Object msg : messages) {
			getLogger().debug(msg);
		}
	}
	
	public static void info(final Object... messages) {
		for (final Object msg : messages) {
			getLogger().info(msg);
		}
	}
	
	public static void warn(final Object... messages) {
		for (final Object msg : messages) {
			getLogger().warn(msg);
		}
	}
	
	public static void error(final Object... messages) {
		for (final Object msg : messages) {
			getLogger().error(msg);
		}
	}
	
	public static void fatal(final Object... messages) {
		for (final Object msg : messages) {
			getLogger().fatal(msg);
		}
	}
	
	public static void dump(final Object... objects) {
		for (final Object obj : objects) {
			final Field[] fields = obj.getClass().getDeclaredFields();
			info("Dump of " + obj + ":");
			for (int i = 0; i < fields.length; i++) {
				try {
					fields[i].setAccessible(true);
					info(fields[i].getName() + " - " + fields[i].get(obj));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					info("Error getting field " + fields[i].getName());
					info(e.getLocalizedMessage());
				}
			}
		}
	}
	
}
