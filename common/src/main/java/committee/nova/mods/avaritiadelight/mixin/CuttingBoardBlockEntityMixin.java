package committee.nova.mods.avaritiadelight.mixin;

import committee.nova.mods.avaritiadelight.registry.ADItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.block.CuttingBoardBlock;
import vectorwing.farmersdelight.common.block.entity.CuttingBoardBlockEntity;
import vectorwing.farmersdelight.common.block.entity.SyncedBlockEntity;
import vectorwing.farmersdelight.common.utility.ItemUtils;

@Pseudo
@Mixin(CuttingBoardBlockEntity.class)
public abstract class CuttingBoardBlockEntityMixin extends SyncedBlockEntity {
    @Unique
    private ItemStack avaritia_delight$tempToolStack = ItemStack.EMPTY;

    public CuttingBoardBlockEntityMixin(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
        super(tileEntityTypeIn, pos, state);
    }

    @Inject(method = "processStoredItemUsingTool", at = @At(value = "HEAD"))
    private void beforeCutting(ItemStack toolStack, PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        this.avaritia_delight$tempToolStack = toolStack;
    }

    @Inject(method = "processStoredItemUsingTool", at = @At(value = "TAIL"))
    private void afterCutting(ItemStack toolStack, PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        this.avaritia_delight$tempToolStack = ItemStack.EMPTY;
    }

    @Inject(method = "removeItem", at = @At("HEAD"))
    private void dropBeef(CallbackInfoReturnable<ItemStack> cir) {
        assert this.world != null;
        if (!this.removed && (this.avaritia_delight$tempToolStack.isOf(ADItems.NEUTRONIUM_KNIFE.get()) || this.avaritia_delight$tempToolStack.isOf(ADItems.INFINITY_KNIFE.get())) && this.world.random.nextInt(10) == 0) {
            Direction direction = this.getCachedState().get(CuttingBoardBlock.FACING).rotateYCounterclockwise();
            ItemUtils.spawnItemEntity(this.world, new ItemStack(ADItems.COSMIC_BEEF.get()), this.pos.getX() + 0.5 + direction.getOffsetX() * 0.2, this.pos.getY() + 0.2, this.pos.getZ() + 0.5 + direction.getOffsetZ() * 0.2, direction.getOffsetX() * 0.2, 0.0, direction.getOffsetZ() * 0.2);
        }
    }
}
