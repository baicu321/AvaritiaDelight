package committee.nova.mods.avaritiadelight.item.block;

import committee.nova.mods.avaritiadelight.registry.ADBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import vectorwing.farmersdelight.common.block.RichSoilFarmlandBlock;

public class SoilRichSoilFarmlandBlock extends RichSoilFarmlandBlock {
    public SoilRichSoilFarmlandBlock(Settings properties) {
        super(properties);
    }

    public static void turnToSoulRichSoil(BlockState state, World level, BlockPos pos) {
        level.setBlockState(pos, Block.pushEntitiesUpBeforeBlockChange(state, ADBlocks.SOUL_RICH_SOIL.get().getDefaultState(), level, pos));
    }

    public boolean isFertile(BlockState state, BlockView world, BlockPos pos) {
        return state.isOf(ADBlocks.SOUL_RICH_SOIL_FARMLAND.get()) && state.get(MOISTURE) > 0;
    }

    public void scheduledTick(BlockState state, ServerWorld level, BlockPos pos, Random rand) {
        if (!state.canPlaceAt(level, pos))
            turnToSoulRichSoil(state, level, pos);
    }

    public BlockState getPlacementState(ItemPlacementContext context) {
        return !this.getDefaultState().canPlaceAt(context.getWorld(), context.getBlockPos()) ? ADBlocks.SOUL_RICH_SOIL.get().getDefaultState() : super.getPlacementState(context);
    }
}
