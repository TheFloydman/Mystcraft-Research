package thefloydman.mystcraftresearch.capability;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.xcompwiz.mystcraft.api.symbol.IAgeSymbol;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thefloydman.mystcraftresearch.research.EnumFlag;

public interface ICapabilityMystcraftResearch {

	public List<IAgeSymbol> getKnownSymbols();

	public ItemStack getSymbolsAsFolder();

	public void setFlag(@Nullable IAgeSymbol symbol, EnumFlag flag, boolean tripped);

	public Map<ResourceLocation, Map<String, Boolean>> getAllFlags();

	public void setAllFlags(Map<ResourceLocation, Map<String, Boolean>> map);

}