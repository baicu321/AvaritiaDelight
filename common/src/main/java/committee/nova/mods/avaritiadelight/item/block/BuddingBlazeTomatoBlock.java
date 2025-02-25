package committee.nova.mods.avaritiadelight.item.block;

import committee.nova.mods.avaritiadelight.registry.ADBlocks;
import committee.nova.mods.avaritiadelight.registry.ADItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemConvertible;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import vectorwing.farmersdelight.common.block.BuddingTomatoBlock;

public class BuddingBlazeTomatoBlock extends BuddingTomatoBlock {
    public BuddingBlazeTomatoBlock() {
        super(Settings.copy(Blocks.WHEAT));
    }

    @Override
    public BlockState getPlant(BlockView world, BlockPos pos) {
        return ADBlocks.BUDDING_BLAZE_TOMATO.get().getDefaultState();
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction facing, BlockState facingState, WorldAccess level, BlockPos currentPos, BlockPos facingPos) {
        if (state.get(AGE) == 3)
            level.setBlockState(currentPos, ADBlocks.BLAZE_TOMATO.get().getDefaultState(), 3);
        return super.getStateForNeighborUpdate(state, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    public void growPastMaxAge(BlockState state, ServerWorld level, BlockPos pos, Random random) {
        level.setBlockState(pos, ADBlocks.BLAZE_TOMATO.get().getDefaultState());
    }

    @Override
    public void grow(ServerWorld level, Random random, BlockPos pos, BlockState state) {
        int maxAge = this.getMaxAge();
        int ageGrowth = Math.min(this.getAge(state) + this.getBonemealAgeIncrease(level), 7);
        if (ageGrowth <= maxAge)
            level.setBlockState(pos, state.with(AGE, ageGrowth));
        else {
            int remainingGrowth = ageGrowth - maxAge - 1;
            level.setBlockState(pos, ADBlocks.BLAZE_TOMATO.get().getDefaultState().with(BlazeTomatoBlock.VINE_AGE, remainingGrowth));
        }
    }

    @Override
    protected ItemConvertible getBaseSeedId() {
        return ADItems.BLAZE_TOMATO_SEEDS.get();
    }
}
