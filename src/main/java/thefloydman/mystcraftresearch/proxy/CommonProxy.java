package thefloydman.mystcraftresearch.proxy;

import com.xcompwiz.mystcraft.api.MystObjects;
import com.xcompwiz.mystcraft.api.exception.APIUndefined;
import com.xcompwiz.mystcraft.api.exception.APIVersionRemoved;
import com.xcompwiz.mystcraft.api.exception.APIVersionUndefined;
import com.xcompwiz.mystcraft.api.hook.SymbolAPI;

import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import thefloydman.mystcraftresearch.MystcraftResearch;
import thefloydman.mystcraftresearch.capability.CapabilityMystcraftResearch;
import thefloydman.mystcraftresearch.capability.ICapabilityMystcraftResearch;
import thefloydman.mystcraftresearch.capability.StorageCapabilityMystcraftResearch;
import thefloydman.mystcraftresearch.network.MystcraftResearchPacketHandler;
import thefloydman.mystcraftresearch.research.Research;
import thefloydman.mystcraftresearch.tileentity.TileEntitySymbolRecordingDesk;
import thefloydman.mystcraftresearch.util.Reference;

public class CommonProxy {

	public static SymbolAPI symbolApi;

	public void preInit(FMLPreInitializationEvent event) {
		MystcraftResearchPacketHandler.register();
		CapabilityManager.INSTANCE.register(ICapabilityMystcraftResearch.class,
				new StorageCapabilityMystcraftResearch(), CapabilityMystcraftResearch::new);
		GameRegistry.registerTileEntity(TileEntitySymbolRecordingDesk.class, Reference.makeRL("symbol_recording_desk"));
	}

	public void init(FMLInitializationEvent event) {
		MystcraftResearch.logger.info("Initializing Mystcraft Symbol API");
		try {
			symbolApi = (SymbolAPI) MystObjects.entryPoint.getProviderInstance().getAPIInstance("symbol-1");
		} catch (APIVersionRemoved e1) {
			MystcraftResearch.logger.error("API version removed!");
		} catch (APIVersionUndefined e2) {
			MystcraftResearch.logger.error("API version undefined!");
		} catch (APIUndefined e3) {
			MystcraftResearch.logger.error("API undefined!");
		}
	}

	public void postInit(FMLPostInitializationEvent event) {
		Research.init();
	}

}
