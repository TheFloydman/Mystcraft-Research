package thefloydman.mystcraftresearch.research;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xcompwiz.mystcraft.api.symbol.IAgeSymbol;
import com.xcompwiz.mystcraft.symbol.modifiers.SymbolBiome;
import com.xcompwiz.mystcraft.symbol.modifiers.SymbolBlock;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thefloydman.mystcraftresearch.MystcraftResearch;
import thefloydman.mystcraftresearch.capability.ICapabilityMystcraftResearch;
import thefloydman.mystcraftresearch.capability.ProviderCapabilityMystcraftResearch;
import thefloydman.mystcraftresearch.proxy.CommonProxy;

public class Research {

	public static Map<Biome, ResourceLocation> biomeMap = new HashMap<Biome, ResourceLocation>();
	public static Map<IBlockState, ResourceLocation> blockMap = new HashMap<IBlockState, ResourceLocation>();

	public Research() {
		List<IAgeSymbol> allSymbols = CommonProxy.symbolApi.getAllRegisteredSymbols();
		for (IAgeSymbol symbol : allSymbols) {
			if (symbol instanceof SymbolBlock) {
				blockMap.put(((SymbolBlock) symbol).blockDescriptor.blockstate, symbol.getRegistryName());
			} else if (symbol instanceof SymbolBiome) {
				biomeMap.put(((SymbolBiome) symbol)., symbol.getRegistryName());
			}
		}
	}

	@SideOnly(Side.SERVER)
	public static void learnSymbol(EntityPlayer player, IAgeSymbol symbol) {
		if (symbol != null) {
			ICapabilityMystcraftResearch cap = player
					.getCapability(ProviderCapabilityMystcraftResearch.MYSTCRAFT_RESEARCH, null);
			cap.learnSymbol(symbol, (EntityPlayerMP) player);
		} else {
			MystcraftResearch.logger.error("Cannot learn symbol because it is null.");
		}
	}

	@SideOnly(Side.SERVER)
	public static void learnBiome(EntityPlayer player, Biome biome) {
		IAgeSymbol symbol = CommonProxy.symbolApi
				.getSymbol(new ResourceLocation("mystcraft", "Biome" + String.valueOf(Biome.getIdForBiome(biome))));
		learnSymbol(player, symbol);
	}

	@SideOnly(Side.SERVER)
	public static void learnBlock(EntityPlayer player, IBlockState state) {
		if (blockMap.containsKey(state)) {
			IAgeSymbol symbol = CommonProxy.symbolApi.getSymbol(blockMap.get(state));
			learnSymbol(player, symbol);
		}
	}

}
