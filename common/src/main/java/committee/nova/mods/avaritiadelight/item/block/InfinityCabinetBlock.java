package committee.nova.mods.avaritiadelight.item.block;

import committee.nova.mods.avaritiadelight.item.block.entity.InfinityCabinetBlockEntity;
import committee.nova.mods.avaritiadelight.registry.ADBlockEntities;
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class InfinityCabinetBlock extends BarrelBlock {
    public InfinityCabinetBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (player instanceof ServerPlayerEntity serverPlayer)
            serverPlayer.openHandledScreen(this.createScreenHandlerFactory(state, world, pos));
        return ActionResult.SUCCESS;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new InfinityCabinetBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, ADBlockEntities.INFINITY_CABINET.get(), (world1, pos, state1, blockEntity) -> blockEntity.tick());
    }

    @Override
    public void onPlaced(World level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (stack.hasCustomName()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof InfinityCabinetBlockEntity infinityCabinet)
                infinityCabinet.setCustomName(stack.getName());
        }
    }
}
