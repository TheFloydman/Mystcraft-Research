package thefloydman.mystcraftresearch.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thefloydman.mystcraftresearch.capability.ICapabilityMystcraftResearch;
import thefloydman.mystcraftresearch.capability.ProviderCapabilityMystcraftResearch;
import thefloydman.mystcraftresearch.capability.StorageCapabilityMystcraftResearch;

public class PacketSyncResearchCapability implements IMessage {

	NBTTagList symbolInfo;

	public PacketSyncResearchCapability(NBTTagList nbt) {
		this.symbolInfo = nbt;
	}

	public PacketSyncResearchCapability() {
		this.symbolInfo = new NBTTagList();
	}

	public static class Handler implements IMessageHandler<PacketSyncResearchCapability, IMessage> {

		@SideOnly(Side.CLIENT)
		@Override
		public IMessage onMessage(PacketSyncResearchCapability msg, MessageContext ctx) {
			EntityPlayer player = Minecraft.getMinecraft().player;
			ICapabilityMystcraftResearch cap = player
					.getCapability(ProviderCapabilityMystcraftResearch.MYSTCRAFT_RESEARCH, null);
			if (cap != null) {
				cap.setAllFlags(StorageCapabilityMystcraftResearch.nbtToMap(msg.symbolInfo));
			}
			return null;
		}
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.symbolInfo = ByteBufUtils.readTag(buf).getTagList("list", 10);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setTag("list", this.symbolInfo);
		ByteBufUtils.writeTag(buf, compound);
	}

}
