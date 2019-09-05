package thefloydman.mystcraftresearch.network.packets;

import java.nio.charset.Charset;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketSendStatusMessage implements IMessage {

	String key;
	String additional;

	public PacketSendStatusMessage(@Nonnull String msg, @Nullable String add) {
		this.key = msg;
		this.additional = add;
		if (add == null) {
			this.additional = "";
		}
	}

	public PacketSendStatusMessage() {
		this.key = "";
		this.additional = "";
	}

	public static class Handler implements IMessageHandler<PacketSendStatusMessage, IMessage> {

		@SideOnly(Side.CLIENT)
		@Override
		public IMessage onMessage(PacketSendStatusMessage msg, MessageContext ctx) {
			EntityPlayer player = Minecraft.getMinecraft().player;
			TextComponentTranslation message = new TextComponentTranslation(msg.key);
			if (!msg.additional.trim().isEmpty()) {
				message.appendText(" ");
				message.appendSibling(new TextComponentTranslation(msg.additional));
			}
			player.sendStatusMessage(message, true);
			return null;
		}
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.key = buf.readCharSequence(buf.readInt(), Charset.defaultCharset()).toString();
		this.additional = buf.readCharSequence(buf.readableBytes(), Charset.defaultCharset()).toString();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.key.length());
		buf.writeCharSequence(this.key, Charset.defaultCharset());
		buf.writeCharSequence(this.additional, Charset.defaultCharset());
	}

}
