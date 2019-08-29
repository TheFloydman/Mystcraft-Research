package thefloydman.mystcraftresearch.block;

import com.xcompwiz.mystcraft.core.MystcraftCommonProxy;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import thefloydman.mystcraftresearch.MystcraftResearch;
import thefloydman.mystcraftresearch.gui.MystcraftResearchGUIs;
import thefloydman.mystcraftresearch.tileentity.TileEntitySymbolRecordingDesk;
import thefloydman.mystcraftresearch.util.Reference;

public class BlockSymbolRecordingDesk extends Block {

	public static final AxisAlignedBB AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

	public BlockSymbolRecordingDesk() {

		super(Material.WOOD);
		this.setHardness(1.0f);
		this.setSoundType(SoundType.WOOD);
		this.setUnlocalizedName(Reference.MOD_ID + ".symbol_recording_desk");
		this.setRegistryName(Reference.BlockNames.SYMBOL_RECORDING_DESK);
		this.setCreativeTab(MystcraftCommonProxy.tabMystCommon);
		this.setDefaultState(blockState.getBaseState());

	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return true;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return true;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB;
	}

	@Override
	public boolean onBlockActivated(final World world, final BlockPos pos, final IBlockState state,
			final EntityPlayer player, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY,
			final float hitZ) {

		player.openGui((Object) MystcraftResearch.instance, MystcraftResearchGUIs.SYMBOL_RECORDING_DESK.ordinal(),
				world, pos.getX(), pos.getY(), pos.getZ());

		return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);

	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntitySymbolRecordingDesk();
	}
}