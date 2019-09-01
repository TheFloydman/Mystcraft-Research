package thefloydman.mystcraftresearch.capability;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class StorageCapabilityMystcraftResearch implements IStorage<ICapabilityMystcraftResearch> {

	@Override
	public NBTBase writeNBT(Capability<ICapabilityMystcraftResearch> capability, ICapabilityMystcraftResearch instance,
			EnumFacing side) {
		NBTTagList nbt = new NBTTagList();
		if (instance != null) {
			for (Entry<ResourceLocation, Map<String, Boolean>> entrySymbol : instance.getAllFlags().entrySet()) {
				NBTTagCompound symbol = new NBTTagCompound();
				NBTTagList flagList = new NBTTagList();
				for (Entry<String, Boolean> entryFlag : entrySymbol.getValue().entrySet()) {
					NBTTagCompound flag = new NBTTagCompound();
					flag.setBoolean("tripped", entryFlag.getValue());
					flag.setString("name", entryFlag.getKey());
					flagList.appendTag(flag);
				}
				symbol.setString("name", entrySymbol.getKey().toString());
				symbol.setTag("flags", flagList);
				nbt.appendTag(symbol);
			}
		}
		return nbt;
	}

	@Override
	public void readNBT(Capability<ICapabilityMystcraftResearch> capability, ICapabilityMystcraftResearch instance,
			EnumFacing side, NBTBase nbt) {
		if (instance != null) {
			if (nbt != null) {
				Map<ResourceLocation, Map<String, Boolean>> symbolMap = new HashMap<ResourceLocation, Map<String, Boolean>>();
				NBTTagList list = (NBTTagList) nbt;
				for (NBTBase base : list) {
					NBTTagCompound symbolCompound = (NBTTagCompound) base;
					ResourceLocation loc = new ResourceLocation(symbolCompound.getString("name"));
					NBTTagList flagList = symbolCompound.getTagList("flags", 10);
					Map<String, Boolean> flagMap = new HashMap<String, Boolean>();
					for (NBTBase flagBase : flagList) {
						NBTTagCompound flagCompound = (NBTTagCompound) flagBase;
						String flagName = flagCompound.getString("name");
						boolean flagTripped = flagCompound.getBoolean("tripped");
						flagMap.put(flagName, flagTripped);
					}
					symbolMap.put(loc, flagMap);
				}
				instance.setAllFlags(symbolMap);
			}
		}
	}

}