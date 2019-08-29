package thefloydman.mystcraftresearch.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import thefloydman.mystcraftresearch.client.gui.GuiSymbolRecordingDesk;
import thefloydman.mystcraftresearch.inventory.ContainerSymbolRecordingDesk;
import thefloydman.mystcraftresearch.tileentity.TileEntitySymbolRecordingDesk;

public class MystcraftResearchGuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(final int id, final EntityPlayer player, final World world, final int x,
			final int y, final int z) {
		if (id == MystcraftResearchGUIs.SYMBOL_RECORDING_DESK.ordinal()) {
			final TileEntitySymbolRecordingDesk tileEntity = (TileEntitySymbolRecordingDesk) player.world
					.getTileEntity(new BlockPos(x, y, z));
			return new ContainerSymbolRecordingDesk(player.inventory, tileEntity);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(final int id, final EntityPlayer player, final World world, final int x,
			final int y, final int z) {
		if (id == MystcraftResearchGUIs.SYMBOL_RECORDING_DESK.ordinal()) {
			return new GuiSymbolRecordingDesk(
					(ContainerSymbolRecordingDesk) getServerGuiElement(id, player, world, x, y, z));
		}
		return null;
	}
}