package thefloydman.mystcraftresearch.item;

import com.xcompwiz.mystcraft.core.MystcraftCommonProxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import thefloydman.mystcraftresearch.capability.ICapabilityMystcraftResearch;
import thefloydman.mystcraftresearch.capability.ProviderCapabilityMystcraftResearch;
import thefloydman.mystcraftresearch.research.Knowledge;
import thefloydman.mystcraftresearch.util.Reference;

public class ItemJournalOfTheArt extends Item {

	public ItemJournalOfTheArt() {
		this.setCreativeTab(MystcraftCommonProxy.tabMystCommon);
		this.setMaxStackSize(1);
		this.setRegistryName(Reference.makeRL("journal_of_the_art"));
		this.setUnlocalizedName(Reference.MOD_ID + ".journal_of_the_art");
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		if (!world.isRemote) {
			Biome biome = world.getBiome(player.getPosition());
			ICapabilityMystcraftResearch cap = player
					.getCapability(ProviderCapabilityMystcraftResearch.MYSTCRAFT_RESEARCH, null);
			cap.learnKnowledge(new Knowledge(biome), (EntityPlayerMP) player);
		}
		return super.onItemRightClick(world, player, hand);
	}

}
