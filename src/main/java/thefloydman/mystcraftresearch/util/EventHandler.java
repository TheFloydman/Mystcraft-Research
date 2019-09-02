package thefloydman.mystcraftresearch.util;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thefloydman.mystcraftresearch.capability.ICapabilityMystcraftResearch;
import thefloydman.mystcraftresearch.capability.ProviderCapabilityMystcraftResearch;
import thefloydman.mystcraftresearch.init.MystcraftResearchBlocks;
import thefloydman.mystcraftresearch.init.MystcraftResearchItems;

@EventBusSubscriber
public class EventHandler {

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		MystcraftResearchBlocks.registerBlocks(event);
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		MystcraftResearchBlocks.registerItems(event);
		MystcraftResearchItems.registerItems(event);
	}

	@SubscribeEvent
	public static void registerRenders(ModelRegistryEvent event) {
		MystcraftResearchBlocks.registerRenders(event);
		MystcraftResearchItems.registerRenders(event);
	}

	@SubscribeEvent
	public static void attachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
		Entity entity = event.getObject();
		if (entity instanceof EntityPlayer) {
			event.addCapability(Reference.makeRL("research"), new ProviderCapabilityMystcraftResearch());
		}
	}

	@SubscribeEvent
	public static void clonePlayer(PlayerEvent.Clone event) {

		if (event.isWasDeath()) {

			EntityPlayer entityOld = event.getOriginal();
			EntityPlayer entityNew = event.getEntityPlayer();

			ICapabilityMystcraftResearch capResearchOld = entityOld
					.getCapability(ProviderCapabilityMystcraftResearch.MYSTCRAFT_RESEARCH, null);
			ICapabilityMystcraftResearch capResearchNew = entityNew
					.getCapability(ProviderCapabilityMystcraftResearch.MYSTCRAFT_RESEARCH, null);
			capResearchNew.setAllFlags(capResearchOld.getAllFlags());

		}

	}

}
