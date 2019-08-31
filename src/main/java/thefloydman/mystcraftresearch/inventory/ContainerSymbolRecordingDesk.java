package thefloydman.mystcraftresearch.inventory;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.xcompwiz.mystcraft.api.item.IItemRenameable;
import com.xcompwiz.mystcraft.inventory.ContainerBase;
import com.xcompwiz.mystcraft.inventory.FluidTankProvider;
import com.xcompwiz.mystcraft.inventory.IFluidTankProvider;
import com.xcompwiz.mystcraft.inventory.SlotCollection;
import com.xcompwiz.mystcraft.inventory.SlotFiltered;
import com.xcompwiz.mystcraft.network.IGuiMessageHandler;
import com.xcompwiz.mystcraft.network.MystcraftPacketHandler;
import com.xcompwiz.mystcraft.network.packet.MPacketGuiMessage;
import com.xcompwiz.mystcraft.tileentity.IOInventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import thefloydman.mystcraftresearch.capability.ICapabilityMystcraftResearch;
import thefloydman.mystcraftresearch.capability.ProviderCapabilityMystcraftResearch;
import thefloydman.mystcraftresearch.tileentity.TileEntitySymbolRecordingDesk;

public class ContainerSymbolRecordingDesk extends ContainerBase implements IGuiMessageHandler {

	protected TileEntitySymbolRecordingDesk tileEntity;
	protected InventoryPlayer playerInventory;
	protected ItemStack folderStack = ItemStack.EMPTY;
	protected FluidStack fluid;
	protected FluidTankProvider fluidDataContainer;

	public ContainerSymbolRecordingDesk(InventoryPlayer inventoryplayer, TileEntitySymbolRecordingDesk te) {
		this.tileEntity = te;
		this.playerInventory = inventoryplayer;
		this.fluidDataContainer = new FluidTankProvider();
		this.fluidDataContainer.setTank(te.getInkwell());
		IOInventory inventory = te.getMainItemHandler();

		this.addSlotToContainer(
				new SlotFiltered(inventory, this.tileEntity, TileEntitySymbolRecordingDesk.SLOT_PAPER, -20, 20));
		this.addSlotToContainer(
				new SlotFiltered(inventory, this.tileEntity, TileEntitySymbolRecordingDesk.SLOT_SYMBOL, -20, 40));
		this.addSlotToContainer(
				new SlotFiltered(inventory, this.tileEntity, TileEntitySymbolRecordingDesk.SLOT_INK, -20, 60));
		this.addSlotToContainer(
				new SlotFiltered(inventory, this.tileEntity, TileEntitySymbolRecordingDesk.SLOT_BOTTLES, -20, 80));

		for (int i = 0; i < 3; i++) {
			for (int k = 0; k < 9; k++) {
				addSlotToContainer(new Slot(inventoryplayer, k + i * 9 + 9, 8 + k * 18, 135 + i * 18));
			}
		}

		for (int j = 0; j < 9; j++) {
			addSlotToContainer(new Slot(inventoryplayer, j, 8 + j * 18, 193));
		}

		SlotCollection maininv = new SlotCollection(this, 0, 27);
		SlotCollection hotbar = new SlotCollection(this, 27, 36);

		maininv.pushTargetFront(hotbar);
		hotbar.pushTargetFront(maininv);

		this.collections.add(maininv);
		this.collections.add(hotbar);

		ICapabilityMystcraftResearch cap = this.playerInventory.player
				.getCapability(ProviderCapabilityMystcraftResearch.MYSTCRAFT_RESEARCH, null);
		if (cap != null) {
			this.folderStack = cap.getSymbolsInFolder();
		}
	}

	@Nonnull
	public ItemStack getPageCollection() {
		return getInventoryItem();
	}

	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}

	@Nullable
	public String getTabItemName() {
		ItemStack itemstack = getInventoryItem();
		if (itemstack.isEmpty())
			return null;
		if (itemstack.getItem() instanceof IItemRenameable)
			return ((IItemRenameable) itemstack.getItem()).getDisplayName(this.playerInventory.player, itemstack);
		return null;
	}

	@Nonnull
	public ItemStack getInventoryItem() {
		return this.folderStack;
	}

	@Override
	public void processMessage(@Nonnull EntityPlayer player, @Nonnull NBTTagCompound data) {
		if (data.hasKey("WriteSymbol")) {
			this.tileEntity.writeSymbol(player, new ResourceLocation(data.getString("WriteSymbol")));
		}
		if (data.hasKey("SetFluid") && (this.tileEntity.getWorld()).isRemote) {
			this.tileEntity.setInk(FluidStack.loadFluidStackFromNBT(data.getCompoundTag("SetFluid")));
			this.fluid = this.tileEntity.getInk();
			this.fluidDataContainer.setFluid(this.fluid);
		}
	}

	private boolean hasFluidChanged(FluidStack fluid, FluidStack temp) {
		if (fluid == null && temp == null)
			return false;
		if (fluid == null && temp != null)
			return true;
		if (fluid != null && temp == null)
			return true;
		if (!fluid.isFluidStackIdentical(temp))
			return true;
		return false;
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		List<IMessage> packets = new ArrayList<IMessage>();
		FluidStack temp = this.tileEntity.getInk();
		if (hasFluidChanged(this.fluid, temp)) {
			if (temp != null)
				temp = temp.copy();
			this.fluid = temp;
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			NBTTagCompound fluidnbt = new NBTTagCompound();
			if (this.fluid != null)
				this.fluid.writeToNBT(fluidnbt);
			nbttagcompound.setTag("SetFluid", fluidnbt);
			packets.add(new MPacketGuiMessage(this.windowId, nbttagcompound));
		}
		if (packets.size() > 0) {
			for (IContainerListener listener : this.listeners) {
				if (listener instanceof EntityPlayerMP) {
					for (IMessage message : packets) {
						MystcraftPacketHandler.CHANNEL.sendTo(message, (EntityPlayerMP) listener);
					}
				}
			}
		}
	}

	public IFluidTankProvider getInkTankProvider() {
		return this.fluidDataContainer;
	}

}