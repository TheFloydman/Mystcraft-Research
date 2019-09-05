package thefloydman.mystcraftresearch.research;

import java.util.ArrayList;
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
	public static List<String> allFlags = new ArrayList<String>();
	public static Map<ResourceLocation, Map<String, Boolean>> flagMap = new HashMap<ResourceLocation, Map<String, Boolean>>();

	public static void init() {
		mapSymbols();
	}

	public static void learnSymbol(EntityPlayer player, Object object, EnumFlag flag, boolean tripped) {
		if (symbolMap.containsKey(object)) {
			IAgeSymbol symbol = CommonProxy.symbolApi.getSymbol(symbolMap.get(object));
			if (symbol != null) {
				ICapabilityMystcraftResearch cap = player
						.getCapability(ProviderCapabilityMystcraftResearch.MYSTCRAFT_RESEARCH, null);
				cap.setFlag(symbol, flag, tripped);
			} else {
				MystcraftResearch.logger.error("Cannot learn symbol because it is null.");
			}
		}
	}

	protected static void mapSymbols() {
		List<IAgeSymbol> allSymbols = CommonProxy.symbolApi.getAllRegisteredSymbols();
		for (IAgeSymbol symbol : allSymbols) {
			if (symbol instanceof SymbolBlock) {
				mapBlockSymbol(symbol);
			} else if (symbol instanceof SymbolBiome) {
				mapBiomeSymbol(symbol);
			} else if (symbol instanceof SymbolColor) {
				mapColorSymbol(symbol);
			}
		}
	}

	protected static void mapBlockSymbol(IAgeSymbol symbol) {
		symbolMap.put(((SymbolBlock) symbol).blockDescriptor.blockstate, symbol.getRegistryName());
		setFlags(symbol, false, EnumFlag.TOOK_NOTES);
	}

	protected static void mapBiomeSymbol(IAgeSymbol symbol) {
		for (Biome biome : ForgeRegistries.BIOMES) {
			ResourceLocation loc = new ResourceLocation("mystcraft",
					"Biome" + String.valueOf(Biome.getIdForBiome(biome)));
			if (symbol.getRegistryName().equals(loc)) {
				symbolMap.put(biome, loc);
				setFlags(symbol, false, EnumFlag.TOOK_NOTES);
				break;
			}
		}
	}

	protected static void mapColorSymbol(IAgeSymbol symbol) {
		for (MystcraftColor color : MystcraftColor.values()) {
			SymbolColor symbolColor = (SymbolColor) symbol;
			if (color.resourceLocation.equals(symbolColor.getRegistryName())) {
				symbolMap.put(color, symbolColor.getRegistryName());
				setFlags(symbol, false, EnumFlag.TOOK_NOTES);
				if (color.equals(MystcraftColor.TEAL)) {
					setFlags(symbol, false, EnumFlag.LEARNED_BLUE, EnumFlag.LEARNED_GREEN);
				} else if (color.equals(MystcraftColor.DARK_GREEN)) {
					setFlags(symbol, false, EnumFlag.LEARNED_BLACK, EnumFlag.LEARNED_GREEN);
				} else if (color.equals(MystcraftColor.OLIVE)) {
					setFlags(symbol, false, EnumFlag.LEARNED_GREY, EnumFlag.LEARNED_GREEN);
				} else if (color.equals(MystcraftColor.NAVY)) {
					setFlags(symbol, false, EnumFlag.LEARNED_BLACK, EnumFlag.LEARNED_BLUE);
				} else if (color.equals(MystcraftColor.MAROON)) {
					setFlags(symbol, false, EnumFlag.LEARNED_BLACK, EnumFlag.LEARNED_RED);
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

	public static void addFlag(String flag) {
		if (!allFlags.contains(flag)) {
			allFlags.add(flag);
		}
	}

	public static void setFlag(IAgeSymbol symbol, EnumFlag flag, boolean tripped) {
		Map<ResourceLocation, Map<String, Boolean>> allFlags = getAllFlags();
		Map<String, Boolean> flagMap = allFlags.get(symbol.getRegistryName());
		if (flagMap == null) {
			flagMap = new HashMap<String, Boolean>();
		}
		flagMap.put(flag.name, tripped);
		allFlags.put(symbol.getRegistryName(), flagMap);
		setAllFlags(allFlags);
	}

	public static void setFlags(IAgeSymbol symbol, boolean tripped, EnumFlag... flags) {
		for (EnumFlag flag : flags) {
			setFlag(symbol, flag, tripped);
		}
	}

	public static Map<ResourceLocation, Map<String, Boolean>> getAllFlags() {
		return flagMap;
	}

	public static void setAllFlags(Map<ResourceLocation, Map<String, Boolean>> map) {
		flagMap = map;
	}

}
