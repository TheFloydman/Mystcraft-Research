package thefloydman.mystcraftresearch.network;

import java.util.List;

import com.xcompwiz.mystcraft.api.symbol.IAgeSymbol;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import thefloydman.mystcraftresearch.network.packets.PacketSendStatusMessage;
import thefloydman.mystcraftresearch.network.packets.PacketSyncResearchCapability;
import thefloydman.mystcraftresearch.util.Reference;

public class MystcraftResearchPacketHandler {

	public static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);

	private static int index = 0;

	public MystcraftResearchPacketHandler() {

	}

	public static void register() {
		CHANNEL.registerMessage(PacketSendStatusMessage.Handler.class, PacketSendStatusMessage.class, index++,
				Side.CLIENT);
		CHANNEL.registerMessage(PacketSyncResearchCapability.Handler.class, PacketSyncResearchCapability.class, index++,
				Side.CLIENT);
	}

	public static void sendTranslatedMessage(EntityPlayerMP player, String key) {
		CHANNEL.sendTo(new PacketSendStatusMessage(key), player);
	}

	public static void syncResearch(EntityPlayerMP player, NBTTagList list) {
		CHANNEL.sendTo(new PacketSyncResearchCapability(list), player);
	}

}