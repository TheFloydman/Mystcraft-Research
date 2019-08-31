package thefloydman.mystcraftresearch.client.gui;

import javax.annotation.Nonnull;

import org.lwjgl.input.Keyboard;

import com.xcompwiz.mystcraft.client.gui.GuiContainerElements;
import com.xcompwiz.mystcraft.client.gui.GuiElementSurfaceControlsBase;
import com.xcompwiz.mystcraft.client.gui.element.GuiElementFluidTank;
import com.xcompwiz.mystcraft.client.gui.element.GuiElementPageSurface;
import com.xcompwiz.mystcraft.client.gui.element.GuiElementPageSurface.PositionableItem;
import com.xcompwiz.mystcraft.client.gui.element.GuiElementTextField;
import com.xcompwiz.mystcraft.inventory.IFluidTankProvider;
import com.xcompwiz.mystcraft.network.MystcraftPacketHandler;
import com.xcompwiz.mystcraft.network.packet.MPacketGuiMessage;
import com.xcompwiz.mystcraft.page.Page;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import thefloydman.mystcraftresearch.inventory.ContainerSymbolRecordingDesk;
import thefloydman.mystcraftresearch.tileentity.TileEntitySymbolRecordingDesk;
import thefloydman.mystcraftresearch.util.Reference;

public class GuiSymbolRecordingDesk extends GuiContainerElements {
	protected ContainerSymbolRecordingDesk container;
	protected final ResourceLocation texture = new ResourceLocation(Reference.MOD_ID,
			"textures/gui/symbol_recording_desk.png");
	protected int[] sizeGui = new int[] { 248, 230 };
	protected int[] posGui = new int[] { (this.width - this.sizeGui[0]) / 2, (this.height - this.sizeGui[1]) / 2 };
	protected int[] sizeInv = new int[] { 176, 90 };
	protected int[] posInvGui = new int[] { 36, 140 };
	protected int[] posInvTex = new int[] { 0, 0 };
	protected int[] sizePaper = new int[] { 32, 62 };
	protected int[] posPaperGui = new int[] { 0, 168 };
	protected int[] posPaperTex = new int[] { 31, 90 };
	protected int[] sizeInk = new int[] { 32, 230 };
	protected int[] posInkGui = new int[] { 216, 102 };
	protected int[] posInkTex = new int[] { 0, 90 };
	protected int[] sizeText = new int[] { 176, 18 };
	protected int[] posText = new int[] { 36, 0 };
	protected int[] sizeSurface = new int[] { 176, 114 };
	protected int[] posSurface = new int[] { 36, 22 };
	protected int[] sizeFluid = new int[] { 18, 70 };
	protected int[] posFluid = new int[] { 223, 131 };
	protected int[] sizeInkEmpty = new int[] { 16, 16 };
	protected int[] posInkEmpty = new int[] { 64, 90 };
	protected int[] sizePaperEmpty = new int[] { 16, 16 };
	protected int[] posPaperEmpty = new int[] { 64, 106 };

	public class GuiElementSurfaceControls extends GuiElementSurfaceControlsBase {
		public GuiElementSurfaceControls(Minecraft mc, int guiLeft, int guiTop, int width, int height) {
			super(mc, guiLeft, guiTop, width, height);
		}

		@Nonnull
		public ItemStack getItemStack() {
			return GuiSymbolRecordingDesk.this.container.getInventoryItem();
		}

		public void copy(GuiElementPageSurface.PositionableItem collectionelement) {
			ResourceLocation symbol = Page.getSymbol(collectionelement.itemstack);
			if (symbol == null)
				return;
			if (collectionelement.count <= 0)
				return;
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setString("WriteSymbol", symbol.toString());
			MystcraftPacketHandler.CHANNEL.sendToServer(new MPacketGuiMessage(
					GuiSymbolRecordingDesk.this.mc.player.openContainer.windowId, nbttagcompound));
			GuiSymbolRecordingDesk.this.container.processMessage(GuiSymbolRecordingDesk.this.mc.player, nbttagcompound);
		}

		@Override
		public void pickup(PositionableItem arg0) {
		}

		@Override
		public void place(int arg0, boolean arg1) {
		}
	}

	public GuiSymbolRecordingDesk(ContainerSymbolRecordingDesk con) {
		super(con);
		this.container = (ContainerSymbolRecordingDesk) this.inventorySlots;
		this.xSize = this.sizeGui[0];
		this.ySize = this.sizeGui[1];
		this.guiLeft = this.posGui[0];
		this.guiTop = this.posGui[1];
	}

	public void validate() {
		Keyboard.enableRepeatEvents(true);

		GuiElementSurfaceControls surfacemanager = new GuiElementSurfaceControls(this.mc, this.posText[0],
				this.posText[1], this.sizeSurface[0], this.sizeSurface[1] + this.sizeText[1] + 4);
		GuiElementTextField txt_box = new GuiElementTextField(surfacemanager, surfacemanager, "SearchBox",
				this.posText[0], this.posText[1], this.sizeText[0], this.sizeText[1]);
		addElement(txt_box);
		GuiElementPageSurface surface = new GuiElementPageSurface(surfacemanager, this.mc, this.posSurface[0],
				this.posSurface[1], this.sizeSurface[0], this.sizeSurface[1]);
		surfacemanager.addListener(surface);
		addElement(surface);

		IFluidTankProvider fluidprovider = this.container.getInkTankProvider();
		addElement(new GuiElementFluidTank(this.container, this.mc, this.posFluid[0], this.posFluid[1],
				this.sizeFluid[0], this.sizeFluid[1], fluidprovider));
	}

	protected void _drawBackgroundLayer(int mouseX, int mouseY, float f) {
		this.xSize = this.sizeGui[0];
		this.ySize = this.sizeGui[1];
		this.guiLeft = this.posGui[0];
		this.guiTop = this.posGui[1];
		this.posGui = new int[] { (this.width - this.sizeGui[0]) / 2, (this.height - this.sizeGui[1]) / 2 };
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(this.texture);
		drawTexturedModalRect(this.posGui[0] + this.posPaperGui[0], this.posGui[1] + this.posPaperGui[1],
				this.posPaperTex[0], this.posPaperTex[1], this.sizePaper[0], this.sizePaper[1]);
		drawTexturedModalRect(this.posGui[0] + this.posInvGui[0], this.posGui[1] + this.posInvGui[1], this.posInvTex[0],
				this.posInvTex[1], this.sizeInv[0], this.sizeInv[1]);
		drawTexturedModalRect(this.posGui[0] + this.posInkGui[0], this.posGui[1] + this.posInkGui[1], this.posInkTex[0],
				this.posInkTex[1], this.sizeInk[0], this.sizeInk[1]);
	}
}