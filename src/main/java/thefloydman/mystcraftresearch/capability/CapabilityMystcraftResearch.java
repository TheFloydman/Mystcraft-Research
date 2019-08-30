package thefloydman.mystcraftresearch.capability;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.xcompwiz.mystcraft.api.symbol.IAgeSymbol;
import com.xcompwiz.mystcraft.symbol.modifiers.SymbolBiome;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.biome.Biome;
import thefloydman.mystcraftresearch.network.MystcraftResearchPacketHandler;
import thefloydman.mystcraftresearch.proxy.CommonProxy;
import thefloydman.mystcraftresearch.util.Reference;

public class CapabilityMystcraftResearch implements ICapabilityMystcraftResearch {

	protected List<IAgeSymbol> playerSymbols = new ArrayList<IAgeSymbol>();

	@Override
	public void learnSymbol(@Nonnull IAgeSymbol symbol, @Nullable EntityPlayerMP player) {
		if (player != null) {
			if (!this.knowsSymbol(symbol)) {
				this.playerSymbols.add(symbol);

				if (symbol instanceof SymbolBiome) {
					MystcraftResearchPacketHandler.sendTranslatedMessage(player, Reference.Message.LEARN_BIOME.key);
				}
			} else {

				if (symbol instanceof SymbolBiome) {
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
		return this.playerSymbols;
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

}
