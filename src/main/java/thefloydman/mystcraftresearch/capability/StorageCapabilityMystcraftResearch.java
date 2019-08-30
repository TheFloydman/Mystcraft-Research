package thefloydman.mystcraftresearch.capability;

import com.xcompwiz.mystcraft.api.symbol.IAgeSymbol;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import thefloydman.mystcraftresearch.proxy.CommonProxy;

public class StorageCapabilityMystcraftResearch implements IStorage<ICapabilityMystcraftResearch> {

	@Override
	public NBTBase writeNBT(Capability<ICapabilityMystcraftResearch> capability, ICapabilityMystcraftResearch instance,
			EnumFacing side) {
		NBTTagList nbt = new NBTTagList();
		if (instance != null) {
			for (IAgeSymbol symbol : instance.getKnownSymbols()) {
					nbt.appendTag(new NBTTagString(symbol.getRegistryName().toString()));
			}
		}
		return nbt;
	}

	@Override
	public void readNBT(Capability<ICapabilityMystcraftResearch> capability, ICapabilityMystcraftResearch instance,
			EnumFacing side, NBTBase nbt) {
		if (instance != null) {
			if (nbt != null) {
				NBTTagList list = (NBTTagList) nbt;
				for (NBTBase base : list) {
					IAgeSymbol symbol = CommonProxy.symbolApi
							.getSymbol(new ResourceLocation(((NBTTagString) base).getString()));
					if (symbol != null) {
						instance.learnSymbol(symbol, null);
					}
				}
			}
		}
	}

}