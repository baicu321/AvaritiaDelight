package committee.nova.mods.avaritiadelight.item.block;

import committee.nova.mods.avaritiadelight.item.block.entity.ExtremeCookingPotBlockEntity;
import committee.nova.mods.avaritiadelight.registry.ADBlockEntities;
import committee.nova.mods.avaritiadelight.util.BlockEntityUtil;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.MapColor;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.block.CookingPotBlock;
import vectorwing.farmersdelight.common.block.state.CookingPotSupport;
import vectorwing.farmersdelight.common.registry.ModSounds;
import vectorwing.farmersdelight.common.tag.ModTags;

public class ExtremeCookingPotBlock extends CookingPotBlock {
    public ExtremeCookingPotBlock() {
        super(Settings.create().mapColor(MapColor.IRON_GRAY).strength(0.5F, 6.0F).sounds(BlockSoundGroup.LANTERN));
    }

    private CookingPotSupport getTrayState(WorldAccess level, BlockPos pos) {
        return level.getBlockState(pos.down()).isIn(ModTags.TRAY_HEAT_SOURCES) ? CookingPotSupport.TRAY : CookingPotSupport.NONE;
    }

    @Override
    public ActionResult onUse(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult result) {
        ItemStack heldStack = player.getStackInHand(hand);
        if (heldStack.isEmpty() && player.isSneaking()) {
            level.setBlockState(pos, state.with(SUPPORT, state.get(SUPPORT).equals(CookingPotSupport.HANDLE) ? this.getTrayState(level, pos) : CookingPotSupport.HANDLE));
            level.playSound(null, pos, SoundEvents.BLOCK_LANTERN_PLACE, SoundCategory.BLOCKS, 0.7F, 1.0F);
        } else if (player instanceof ServerPlayerEntity serverPlayer) {
            BlockEntity tileEntity = level.getBlockEntity(pos);
            if (tileEntity instanceof ExtremeCookingPotBlockEntity cookingPotEntity) {
                ItemStack servingStack = cookingPotEntity.useHeldItemOnMeal(heldStack);
                if (servingStack != ItemStack.EMPTY) {
                    if (!player.getInventory().insertStack(servingStack))
                        player.dropItem(servingStack, false);
                    level.playSound(null, pos, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.BLOCKS, 1.0F, 1.0F);
                } else MenuRegistry.openExtendedMenu(serverPlayer, cookingPotEntity);
            }

            return ActionResult.SUCCESS;
        }

        return ActionResult.SUCCESS;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ExtremeCookingPotBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World level, BlockState state, BlockEntityType<T> type) {
        return BlockWithEntity.checkType(type, ADBlockEntities.EXTREME_COOKING_POT.get(), level.isClient ? ExtremeCookingPotBlockEntity::animationTick : ExtremeCookingPotBlockEntity::cookingTick);
    }

    @Override
    public ItemStack getPickStack(BlockView level, BlockPos pos, BlockState state) {
        return new ItemStack(this);
    }

    @Override
    public void onPlaced(World level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (stack.hasCustomName()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof ExtremeCookingPotBlockEntity pot) {
                pot.setCustomName(stack.getName());
            }
        }

    }

    @Override
    public void randomDisplayTick(BlockState state, World level, BlockPos pos, Random random) {
        BlockEntity tileEntity = level.getBlockEntity(pos);
        if (tileEntity instanceof ExtremeCookingPotBlockEntity cookingPotEntity) {
            if (cookingPotEntity.isHeated()) {
                SoundEvent boilSound = !cookingPotEntity.getMeal().isEmpty() ? (SoundEvent) ModSounds.BLOCK_COOKING_POT_BOIL_SOUP.get() : (SoundEvent) ModSounds.BLOCK_COOKING_POT_BOIL.get();
                double x = (double) pos.getX() + 0.5;
                double y = (double) pos.getY();
                double z = (double) pos.getZ() + 0.5;
                if (random.nextInt(10) == 0) {
                    level.playSound(x, y, z, boilSound, SoundCategory.BLOCKS, 0.5F, random.nextFloat() * 0.2F + 0.9F, false);
                }
            }
        }

    }

    @Override
    public int getComparatorOutput(BlockState blockState, World level, BlockPos pos) {
        BlockEntity tileEntity = level.getBlockEntity(pos);
        if (tileEntity instanceof ExtremeCookingPotBlockEntity pot)
            return BlockEntityUtil.calcRedstoneFromItemHandler(pot);
        else
            return 0;
    }
}
