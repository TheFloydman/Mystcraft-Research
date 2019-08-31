package thefloydman.mystcraftresearch.research;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.xcompwiz.mystcraft.api.symbol.IAgeSymbol;
import com.xcompwiz.mystcraft.symbol.modifiers.SymbolBiome;
import com.xcompwiz.mystcraft.symbol.modifiers.SymbolBlock;
import com.xcompwiz.mystcraft.symbol.modifiers.SymbolColor;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import thefloydman.mystcraftresearch.MystcraftResearch;
import thefloydman.mystcraftresearch.capability.ICapabilityMystcraftResearch;
import thefloydman.mystcraftresearch.capability.ProviderCapabilityMystcraftResearch;
import thefloydman.mystcraftresearch.proxy.CommonProxy;
import thefloydman.mystcraftresearch.util.MystcraftColor;

public class Research {

	public static Map<Object, ResourceLocation> symbolMap = new HashMap<Object, ResourceLocation>();
	public static Map<EnumDyeColor, MystcraftColor> colorMap = new HashMap<EnumDyeColor, MystcraftColor>();
	public static Map<ResourceLocation, List<String>> flagMap = new HashMap<ResourceLocation, List<String>>();

	public static void init() {
		mapColors();
		mapSymbols();
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

	protected static void mapColors() {
		colorMap.put(EnumDyeColor.BLACK, MystcraftColor.BLACK);
		colorMap.put(EnumDyeColor.BLUE, MystcraftColor.BLUE);
		colorMap.put(EnumDyeColor.BROWN, MystcraftColor.OLIVE);
		colorMap.put(EnumDyeColor.CYAN, MystcraftColor.CYAN);
		colorMap.put(EnumDyeColor.GRAY, MystcraftColor.GREY);
		colorMap.put(EnumDyeColor.GREEN, MystcraftColor.DARK_GREEN);
		colorMap.put(EnumDyeColor.LIGHT_BLUE, MystcraftColor.TEAL);
		colorMap.put(EnumDyeColor.LIME, MystcraftColor.GREEN);
		colorMap.put(EnumDyeColor.MAGENTA, MystcraftColor.MAGENTA);
		colorMap.put(EnumDyeColor.ORANGE, MystcraftColor.MAROON);
		colorMap.put(EnumDyeColor.PINK, MystcraftColor.NAVY);
		colorMap.put(EnumDyeColor.PURPLE, MystcraftColor.PURPLE);
		colorMap.put(EnumDyeColor.RED, MystcraftColor.RED);
		colorMap.put(EnumDyeColor.SILVER, MystcraftColor.SILVER);
		colorMap.put(EnumDyeColor.WHITE, MystcraftColor.WHITE);
		colorMap.put(EnumDyeColor.YELLOW, MystcraftColor.YELLOW);
	}

	protected static void mapSymbols() {
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
			} else if (symbol instanceof SymbolColor) {
				for (MystcraftColor color : MystcraftColor.values()) {
					if (color.resourceLocation.equals(((SymbolColor) symbol).getRegistryName())) {
						symbolMap.put(color, ((SymbolColor) symbol).getRegistryName());
					}
				}
			}
		}
	}

	public static MystcraftColor dyeColorToMystColor(EnumDyeColor dye, MystcraftColor color) {
		return colorMap.get(dye);
	}

	public static EnumDyeColor mystColorToDyeColor(MystcraftColor color, EnumDyeColor dye) {
		Set<Entry<EnumDyeColor, MystcraftColor>> set = colorMap.entrySet();
		for (Entry<EnumDyeColor, MystcraftColor> entry : set) {
			if (entry.getValue().equals(color)) {
				return entry.getKey();
			}
		}
		return EnumDyeColor.BLACK;
	}

	public static void addFlag(IAgeSymbol symbol, String flag) {
		addFlag(symbol.getRegistryName(), flag);
	}

	public static void addFlag(ResourceLocation loc, String flag) {
		List<String> flags = flagMap.get(loc);
		if (!flags.contains(flag)) {
			flags.add(flag);
			flagMap.put(loc, flags);
		}
	}

}
