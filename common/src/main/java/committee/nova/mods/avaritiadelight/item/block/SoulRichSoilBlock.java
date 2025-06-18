package committee.nova.mods.avaritiadelight.item.block;

import committee.nova.mods.avaritiadelight.registry.ADBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vectorwing.farmersdelight.common.block.RichSoilBlock;

public class SoulRichSoilBlock extends RichSoilBlock {
    public SoulRichSoilBlock(Settings properties) {
        super(properties);
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(hand);
        if (stack.isIn(ItemTags.HOES)) {
            world.setBlockState(pos, ADBlocks.SOUL_RICH_SOIL_FARMLAND.get().getDefaultState());
            stack.damage(1, player, living -> living.sendToolBreakStatus(hand));
            world.playSoundAtBlockCenter(pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1, 0, true);
            return ActionResult.SUCCESS;
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }
}
