package thefloydman.mystcraftresearch.research;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xcompwiz.mystcraft.api.symbol.IAgeSymbol;
import com.xcompwiz.mystcraft.symbol.modifiers.SymbolBiome;
import com.xcompwiz.mystcraft.symbol.modifiers.SymbolBlock;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thefloydman.mystcraftresearch.MystcraftResearch;
import thefloydman.mystcraftresearch.capability.ICapabilityMystcraftResearch;
import thefloydman.mystcraftresearch.capability.ProviderCapabilityMystcraftResearch;
import thefloydman.mystcraftresearch.proxy.CommonProxy;

public class Research {

	public static Map<Object, ResourceLocation> symbolMap = new HashMap<Object, ResourceLocation>();

	public static void init() {
		List<IAgeSymbol> allSymbols = CommonProxy.symbolApi.getAllRegisteredSymbols();
		for (IAgeSymbol symbol : allSymbols) {
			if (symbol instanceof SymbolBlock) {
				symbolMap.put(((SymbolBlock) symbol).blockDescriptor.blockstate, symbol.getRegistryName());
			} else if (symbol instanceof SymbolBiome) {
				for (Biome biome : ForgeRegistries.BIOMES) {
					ResourceLocation loc = new ResourceLocation("mystcraft",
							"Biome" + String.valueOf(Biome.getIdForBiome(biome)));
					if (symbol.getRegistryName().equals(loc)) {
						symbolMap.put(biome, loc);
					}
				}
			}
		}
	}

	public static void learnSymbol(EntityPlayer player, Object object) {
		if (symbolMap.containsKey(object)) {
			IAgeSymbol symbol = CommonProxy.symbolApi.getSymbol(symbolMap.get(object));
			if (symbol != null) {
				ICapabilityMystcraftResearch cap = player
						.getCapability(ProviderCapabilityMystcraftResearch.MYSTCRAFT_RESEARCH, null);
				cap.learnSymbol(symbol, (EntityPlayerMP) player);
			} else {
				MystcraftResearch.logger.error("Cannot learn symbol because it is null.");
			}
		}
	}

}
