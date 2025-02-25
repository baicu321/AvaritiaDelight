package committee.nova.mods.avaritiadelight.item.block;

import committee.nova.mods.avaritiadelight.registry.ADItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vectorwing.farmersdelight.common.block.TomatoVineBlock;
import vectorwing.farmersdelight.common.registry.ModSounds;

public class BlazeTomatoBlock extends TomatoVineBlock {
    public BlazeTomatoBlock() {
        super(Settings.create().noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.CROP).pistonBehavior(PistonBehavior.DESTROY));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        int age = state.get(this.getAgeProperty());
        boolean isMature = age == this.getMaxAge();
        if (!isMature && player.getStackInHand(hand).isOf(Items.BONE_MEAL))
            return ActionResult.PASS;
        else if (isMature) {
            int quantity = 1 + world.random.nextInt(2);
            Block.dropStack(world, pos, new ItemStack(ADItems.BLAZE_TOMATO.get(), quantity));
            world.playSound(null, pos, ModSounds.ITEM_TOMATO_PICK_FROM_BUSH.get(), SoundCategory.BLOCKS, 1.0F, 0.8F + world.random.nextFloat() * 0.4F);
            world.setBlockState(pos, state.with(this.getAgeProperty(), 0), 2);
            return ActionResult.SUCCESS;
        } else
            return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    protected ItemConvertible getSeedsItem() {
        return ADItems.BLAZE_TOMATO_SEEDS.get();
    }
}
