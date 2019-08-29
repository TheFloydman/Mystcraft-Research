package thefloydman.mystcraftresearch.init;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import thefloydman.mystcraftresearch.block.BlockSymbolRecordingDesk;
import thefloydman.mystcraftresearch.util.Reference;

public class MystcraftResearchBlocks {

	@ObjectHolder(value = Reference.BlockNames.SYMBOL_RECORDING_DESK)
	public static final Block SYMBOL_RECORDING_DESK = null;

	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		
		event.getRegistry().register(new BlockSymbolRecordingDesk());
		
	}
	
	public static void registerItems(final RegistryEvent.Register<Item> event) {

		event.getRegistry().register(new ItemBlock(SYMBOL_RECORDING_DESK).setRegistryName(SYMBOL_RECORDING_DESK.getRegistryName()));

	}
	
	public static void registerRenders(ModelRegistryEvent event) {

		registerRender(Item.getItemFromBlock(SYMBOL_RECORDING_DESK));
		
	}
	
	public static void registerRender(Item item) {
		ModelLoader.setCustomModelResourceLocation(item, 0,
				new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}

}
