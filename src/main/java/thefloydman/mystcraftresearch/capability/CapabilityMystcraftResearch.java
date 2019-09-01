package thefloydman.mystcraftresearch.capability;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.xcompwiz.mystcraft.api.symbol.IAgeSymbol;
import com.xcompwiz.mystcraft.data.ModItems;
import com.xcompwiz.mystcraft.item.ItemFolder;
import com.xcompwiz.mystcraft.page.Page;
import com.xcompwiz.mystcraft.symbol.modifiers.SymbolBiome;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thefloydman.mystcraftresearch.network.MystcraftResearchPacketHandler;
import thefloydman.mystcraftresearch.proxy.CommonProxy;
import thefloydman.mystcraftresearch.research.EnumFlag;
import thefloydman.mystcraftresearch.util.Reference;

public class CapabilityMystcraftResearch implements ICapabilityMystcraftResearch {

	protected List<IAgeSymbol> playerSymbols = new ArrayList<IAgeSymbol>();
	public Map<ResourceLocation, Map<String, Boolean>> flags = new HashMap<ResourceLocation, Map<String, Boolean>>();

	@Override
	public void learnSymbol(@Nonnull IAgeSymbol symbol, @Nullable EntityPlayerMP player) {
		if (symbol instanceof SymbolBiome) {
			if (!this.knowsSymbol(symbol)) {
				this.playerSymbols.add(symbol);
				if (player != null) {
					MystcraftResearchPacketHandler.sendTranslatedMessage(player, Reference.Message.LEARN_BIOME.key);
				}
			} else {
				if (player != null) {
					MystcraftResearchPacketHandler.sendTranslatedMessage(player, Reference.Message.KNOWN_BIOME.key);
				}
			}
		}
	}

	@Override
	public void forgetSymbol(IAgeSymbol symbol) {
		if (this.knowsSymbol(symbol)) {
			this.playerSymbols.remove(symbol);
		}
	}

	@Override
	public List<IAgeSymbol> getKnownSymbols() {
		List<IAgeSymbol> knownSymbols = new ArrayList<IAgeSymbol>();
		Map<ResourceLocation, Map<String, Boolean>> allFlags = this.getAllFlags();
		for (Entry<ResourceLocation, Map<String, Boolean>> symbolEntry : allFlags.entrySet()) {
			boolean symbolLearned = true;
			for (Entry<String, Boolean> flagEntry : symbolEntry.getValue().entrySet()) {
				if (flagEntry.getValue().equals(Boolean.valueOf(false))) {
					symbolLearned = false;
					break;
				}
			}
			if (symbolLearned == true) {
				IAgeSymbol symbol = CommonProxy.symbolApi.getSymbol(symbolEntry.getKey());
				if (symbol != null) {
					knownSymbols.add(CommonProxy.symbolApi.getSymbol(symbolEntry.getKey()));
				}
			}
		}
		return knownSymbols;
	}

	@Override
	public void setKnownSymbols(List<IAgeSymbol> symbols) {
		this.playerSymbols = symbols;
	}

	@Override
	public boolean knowsSymbol(IAgeSymbol symbol) {
		boolean knows = false;
		for (IAgeSymbol knownSymbol : this.getKnownSymbols()) {
			if (knownSymbol.equals(symbol)) {
				knows = true;
				break;
			}
		}
		return knows;
	}

	@Override
	public ItemStack getSymbolsAsFolder() {
		ItemStack folderStack = new ItemStack(ModItems.folder);
		List<IAgeSymbol> symbols = this.getKnownSymbols();
		for (IAgeSymbol symbol : symbols) {
			ItemStack pageStack = Page.createSymbolPage(symbol.getRegistryName());
			((ItemFolder) ModItems.folder).addPage(null, folderStack, pageStack);
		}
		return folderStack;
	}

	@Override
	public void forgetAllSymbols() {
		this.playerSymbols = new ArrayList<IAgeSymbol>();
	}

	@Override
	public void setFlag(IAgeSymbol symbol, EnumFlag flag, boolean tripped) {
		Map<String, Boolean> flagMap = this.getFlag(symbol.getRegistryName());
		flagMap.put(flag.name, tripped);
		Map<ResourceLocation, Map<String, Boolean>> allFlags = this.getAllFlags();
		allFlags.put(symbol.getRegistryName(), flagMap);
		this.setAllFlags(allFlags);
	}

	public Map<String, Boolean> getFlag(ResourceLocation loc) {
		if (!this.getAllFlags().containsKey(loc)) {
			
		}
		return this.getAllFlags().get(loc);
	}

	@Override
	public Map<ResourceLocation, Map<String, Boolean>> getAllFlags() {
		return this.flags;
	}

	@Override
	public void setAllFlags(Map<ResourceLocation, Map<String, Boolean>> map) {
		this.flags = map;
	}

}
