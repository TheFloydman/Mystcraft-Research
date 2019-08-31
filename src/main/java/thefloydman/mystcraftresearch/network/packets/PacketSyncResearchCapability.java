package thefloydman.mystcraftresearch.network.packets;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.xcompwiz.mystcraft.api.symbol.IAgeSymbol;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thefloydman.mystcraftresearch.capability.ICapabilityMystcraftResearch;
import thefloydman.mystcraftresearch.capability.ProviderCapabilityMystcraftResearch;
import thefloydman.mystcraftresearch.proxy.CommonProxy;

public class PacketSyncResearchCapability implements IMessage {

	List<IAgeSymbol> symbolList;

	public PacketSyncResearchCapability(List<IAgeSymbol> symbols) {
		this.symbolList = symbols;
	}

	public PacketSyncResearchCapability() {
		this.symbolList = new ArrayList<IAgeSymbol>();
	}

	public static class Handler implements IMessageHandler<PacketSyncResearchCapability, IMessage> {

		@SideOnly(Side.CLIENT)
		@Override
		public IMessage onMessage(PacketSyncResearchCapability msg, MessageContext ctx) {
			EntityPlayer player = Minecraft.getMinecraft().player;
			ICapabilityMystcraftResearch cap = player
					.getCapability(ProviderCapabilityMystcraftResearch.MYSTCRAFT_RESEARCH, null);
			if (cap != null) {
				cap.setKnownSymbols(msg.symbolList);
			}
			return null;
		}
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.symbolList = new ArrayList<IAgeSymbol>();
		while (buf.isReadable()) {
			int length = buf.readInt();
			String name = buf.readCharSequence(length, Charset.defaultCharset()).toString();
			IAgeSymbol symbol = CommonProxy.symbolApi.getSymbol(new ResourceLocation(name));
			if (symbol != null) {
				this.symbolList.add(symbol);
			}
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		for (IAgeSymbol symbol : this.symbolList) {
			String name = symbol.getRegistryName().toString();
			buf.writeInt(name.length());
			buf.writeCharSequence(name, Charset.defaultCharset());
		}
	}

}
