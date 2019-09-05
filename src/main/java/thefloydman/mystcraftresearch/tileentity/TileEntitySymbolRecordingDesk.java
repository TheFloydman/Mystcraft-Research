package thefloydman.mystcraftresearch.tileentity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.xcompwiz.mystcraft.Mystcraft;
import com.xcompwiz.mystcraft.api.item.IItemWritable;
import com.xcompwiz.mystcraft.data.ModAchievements;
import com.xcompwiz.mystcraft.fluids.FluidUtils;
import com.xcompwiz.mystcraft.item.ItemPage;
import com.xcompwiz.mystcraft.tileentity.FluidTankFiltered;
import com.xcompwiz.mystcraft.tileentity.IOInventory;
import com.xcompwiz.mystcraft.tileentity.InventoryFilter;
import com.xcompwiz.mystcraft.tileentity.TileEntityBase;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileEntitySymbolRecordingDesk extends TileEntityBase implements InventoryFilter, ITickable {

	private IOInventory inventoryStacks;
	private FluidTankFiltered inkwell;

	public static final int SLOT_PAPER = 0;
	public static final int SLOT_SYMBOL = 1;
	public static final int SLOT_INK = 2;
	public static final int SLOT_BOTTLES = 3;

	public TileEntitySymbolRecordingDesk() {
		this.inventoryStacks = buildWorkInventory();
		this.inkwell = new FluidTankFiltered(1000);
		this.inkwell.setPermittedFluids(Mystcraft.validInks);
	}

	public void writeSymbol(EntityPlayer player, ResourceLocation symbol) {
		if (this.getWorld().isRemote)
			return;
		if (!hasEnoughInk()) {
			return;
		}

		if (this.inventoryStacks.getStackInSlot(SLOT_SYMBOL).isEmpty()
				&& !this.inventoryStacks.getStackInSlot(SLOT_PAPER).isEmpty()) {
			this.inventoryStacks.setStackInSlot(SLOT_SYMBOL,
					ItemPage.createItemstack(this.inventoryStacks.getStackInSlot(SLOT_PAPER)));
			if (this.inventoryStacks.getStackInSlot(SLOT_PAPER).getCount() <= 0) {
				this.inventoryStacks.setStackInSlot(SLOT_PAPER, ItemStack.EMPTY);
			}
		}

		if (this.inventoryStacks.getStackInSlot(SLOT_SYMBOL).isEmpty()) {
			return;
		}

		ItemStack target = this.inventoryStacks.getStackInSlot(SLOT_SYMBOL);
		if (target.isEmpty())
			return;
		if (target.getItem() instanceof IItemWritable
				&& ((IItemWritable) target.getItem()).writeSymbol(player, target, symbol)) {
			useink();
			if (player instanceof EntityPlayerMP) {
				ModAchievements.TRIGGER_WRITE.trigger((EntityPlayerMP) player);
			}
			return;
		}
	}

	private boolean hasEnoughInk() {
		FluidStack fluid = this.inkwell.getFluid();
		return (fluid != null && fluid.amount >= Mystcraft.inkcost);
	}

	@Override
	public boolean canAcceptItem(int slot, @Nonnull ItemStack stack) {
		if (slot == SLOT_PAPER && stack.getItem().equals(Items.PAPER)) {
			return true;
		} else if (slot == SLOT_INK) {
			FluidStack fluidStack = FluidUtil.getFluidContained(stack);
			if (fluidStack != null) {
				return this.inkwell.isFluidPermitted(fluidStack.getFluid());
			}
		}
		return false;
	}

	protected IOInventory buildWorkInventory() {
		return (new IOInventory(this, new int[] { SLOT_PAPER }, new int[0], EnumFacing.VALUES))
				.setMiscSlots(new int[] { SLOT_SYMBOL, SLOT_INK, SLOT_BOTTLES }).setListener(() -> onChange(true))
				.applyFilter(this, new int[] { SLOT_PAPER });
	}

	private void useink() {
		this.inkwell.drain(Mystcraft.inkcost, true);
	}

	public void onChange(boolean isWorkInv) {
		if (isWorkInv) {
			for (int i = 0; i < this.inventoryStacks.getSlots(); i++) {
				handleItemChange(this.inventoryStacks.getStackInSlot(i));
			}
		}
	}

	public void handleItemChange(@Nonnull ItemStack itemstack) {
	}

	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
				|| capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return (T) this.inventoryStacks.getCapability(facing);
		}
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return (T) this.inkwell;
		}
		return null;
	}

	@Nullable
	public FluidStack getInk() {
		return this.inkwell.getFluid();
	}

	public void setInk(@Nullable FluidStack fluid) {
		this.inkwell.setFluid(fluid);
	}

	public FluidTankFiltered getInkwell() {
		return this.inkwell;
	}

	public IOInventory getMainItemHandler() {
		return this.inventoryStacks;
	}

	@Override
	public void readCustomNBT(NBTTagCompound compound) {
		super.readCustomNBT(compound);
		this.inkwell.readFromNBT(compound.getCompoundTag("fluid"));
		this.inventoryStacks.readNBT(compound.getCompoundTag("items"));
	}

	@Override
	public void writeCustomNBT(NBTTagCompound compound) {
		super.writeCustomNBT(compound);
		NBTTagCompound tag = new NBTTagCompound();
		this.inkwell.writeToNBT(tag);
		compound.setTag("fluid", tag);
		compound.setTag("items", this.inventoryStacks.writeNBT());
	}

	@Override
	public void update() {
		if (this.world.isRemote) {
			return;
		}
		if (!this.inventoryStacks.getStackInSlot(SLOT_INK).isEmpty()) {
			ItemStack fluidContainer = this.inventoryStacks.getStackInSlot(SLOT_INK);
			ItemStack emptyContainer = fluidContainer.getItem().getContainerItem(fluidContainer);
			if (emptyContainer.isEmpty()
					|| !mergeItemStacksLeft(this.inventoryStacks.getStackInSlot(SLOT_BOTTLES), emptyContainer)
							.equals(this.inventoryStacks.getStackInSlot(SLOT_BOTTLES))) {
				ItemStack result = FluidUtils.fillTankWithContainer(this.inkwell, fluidContainer);
				if (!result.isEmpty()) {
					this.inventoryStacks.setStackInSlot(SLOT_BOTTLES,
							mergeItemStacksLeft(this.inventoryStacks.getStackInSlot(SLOT_BOTTLES), result));
					if (fluidContainer.getCount() <= 0) {
						this.inventoryStacks.setStackInSlot(SLOT_INK, ItemStack.EMPTY);
					}
				}
			}
		}
		if (!this.inventoryStacks.getStackInSlot(SLOT_INK).isEmpty()) {
			ItemStack container = this.inventoryStacks.getStackInSlot(SLOT_INK);
			FluidActionResult far = FluidUtil.tryFillContainer(container, this.inkwell, 1000, null, false);
			if (far.isSuccess()) {
				if (mergeItemStacksLeft(this.inventoryStacks.getStackInSlot(SLOT_BOTTLES),
						far.getResult()) != this.inventoryStacks.getStackInSlot(SLOT_BOTTLES)) {
					this.inventoryStacks.setStackInSlot(SLOT_BOTTLES,
							mergeItemStacksLeft(this.inventoryStacks.getStackInSlot(SLOT_BOTTLES),
									FluidUtil.tryFillContainer(container, this.inkwell, 1000, null, true).getResult()));
					if (container.getCount() <= 0) {
						this.inventoryStacks.setStackInSlot(SLOT_INK, ItemStack.EMPTY);
					}
				}
			}
		}
	}

	@Nonnull
	private static ItemStack mergeItemStacksLeft(@Nonnull ItemStack left, @Nonnull ItemStack right) {
		if (right.isEmpty())
			return left;
		if (left.isEmpty())
			return right;
		if (left.getItem() != right.getItem())
			return left;
		if (left.hasTagCompound() != right.hasTagCompound())
			return left;
		if (left.hasTagCompound() && !left.getTagCompound().equals(right.getTagCompound()))
			return left;
		if (left.getItem().getHasSubtypes() && left.getItemDamage() != right.getItemDamage())
			return left;
		if (left.getCount() + right.getCount() > left.getMaxStackSize()) {
			return left;
		}
		left = left.copy();
		left.grow(right.getCount());
		right.setCount(0);
		return left;
	}

	@Nonnull
	public ItemStack fillTankWithContainer(IFluidTank tank, ItemStack containerStack) {
		ItemStack container = containerStack.copy();
		container.setCount(1);
		FluidStack fluid = FluidUtil.getFluidContained(container);
		if (fluid != null) {
			int used = tank.fill(fluid, false);
			if (used == fluid.amount) {
				containerStack.shrink(1);
				tank.fill(fluid, true);
				return this.emptyContainer(container);
			}
		}

		return ItemStack.EMPTY;
	}

	@Nonnull
	public ItemStack emptyContainer(@Nonnull ItemStack container) {
		if (container.isEmpty())
			return ItemStack.EMPTY;
		if (container.getCount() > 1) {
			container.splitStack(1);
		}
		if (container.getItem().hasContainerItem(container)) {
			return container.getItem().getContainerItem(container);
		}
		return ItemStack.EMPTY;
	}
}
