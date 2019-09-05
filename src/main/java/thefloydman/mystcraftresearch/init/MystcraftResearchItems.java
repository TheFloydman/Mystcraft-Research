package thefloydman.mystcraftresearch.init;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import thefloydman.mystcraftresearch.item.ItemJournalOfTheArt;
import thefloydman.mystcraftresearch.util.Reference;

public class MystcraftResearchItems {

	@ObjectHolder(value = Reference.ItemNames.JOURNAL_OF_THE_ART)
	public static final Item JOURNAL_OF_THE_ART = null;

	public static void registerItems(final RegistryEvent.Register<Item> event) {

		event.getRegistry().register(new ItemJournalOfTheArt());

	}

	public static void registerRenders(ModelRegistryEvent event) {

		registerRender(JOURNAL_OF_THE_ART);

	}

	public static void registerRender(Item item) {
		ModelLoader.setCustomModelResourceLocation(item, 0,
				new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}

}
