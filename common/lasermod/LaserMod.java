package lasermod;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import lasermod.api.LaserRegistry;
import lasermod.laser.DefaultLaser;
import lasermod.lib.Reference;
import lasermod.network.NetworkManager;
import lasermod.proxy.CommonProxy;

/**
 * @author ProPercivalalb
 */
@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION, dependencies = Reference.MOD_DEPENDENCIES)
public class LaserMod {

	@Instance(value = Reference.MOD_ID)
	public static LaserMod instance;
	
	@SidedProxy(clientSide = Reference.SP_CLIENT, serverSide = Reference.SP_SERVER)
    	public static CommonProxy proxy;
	
	public static NetworkManager NETWORK_MANAGER;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		
		ModBlocks.inti();
		ModItems.inti();
		ModEntities.inti();
		
		/*
		* Temporary place for the creative tab. Clear it up when you want (or I'll do it on Sunday)
		* Sorry if this causes any errors, I am not using an IDE for this.
		*/
		
		public static CreativeTabs tabLaser = new CreativeTabs("tabLaser");
		
		proxy.onPreLoad();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		NETWORK_MANAGER = new NetworkManager(Reference.CHANNEL_NAME);
    	NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
			
		proxy.registerHandlers();
	}
	
	@EventHandler
	public void modsLoaded(FMLPostInitializationEvent event) {
		LaserRegistry.registerLaser("default", new DefaultLaser());
	}
}
