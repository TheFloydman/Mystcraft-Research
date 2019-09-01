package thefloydman.mystcraftresearch.capability;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.xcompwiz.mystcraft.api.symbol.IAgeSymbol;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thefloydman.mystcraftresearch.research.EnumFlag;

public interface ICapabilityMystcraftResearch {

	public void learnSymbol(IAgeSymbol symbol, @Nullable EntityPlayerMP player);

	public void forgetSymbol(IAgeSymbol symbol);
	
	public void forgetAllSymbols();

	public List<IAgeSymbol> getKnownSymbols();

	public void setKnownSymbols(List<IAgeSymbol> symbols);

	public boolean knowsSymbol(IAgeSymbol symbol);
	
	public ItemStack getSymbolsAsFolder();
	
	public void setFlag(@Nullable IAgeSymbol symbol, EnumFlag flag, boolean tripped);
	
	public Map<ResourceLocation, Map<String, Boolean>> getAllFlags();
	
	public void setAllFlags(Map<ResourceLocation, Map<String, Boolean>> map);

}