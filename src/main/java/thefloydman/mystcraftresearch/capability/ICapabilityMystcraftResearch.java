package thefloydman.mystcraftresearch.capability;

import java.util.List;

import javax.annotation.Nullable;

import com.xcompwiz.mystcraft.api.symbol.IAgeSymbol;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

public interface ICapabilityMystcraftResearch {

	public void learnSymbol(IAgeSymbol symbol, @Nullable EntityPlayerMP player);

	public void forgetSymbol(IAgeSymbol symbol);
	
	public void forgetAllSymbols();

	public List<IAgeSymbol> getKnownSymbols();

	public void setKnownSymbols(List<IAgeSymbol> symbols);

	public boolean knowsSymbol(IAgeSymbol symbol);
	
	public ItemStack getSymbolsInFolder();

}