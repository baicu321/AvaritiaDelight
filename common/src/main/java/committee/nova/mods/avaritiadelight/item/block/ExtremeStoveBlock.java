package committee.nova.mods.avaritiadelight.item.block;

import committee.nova.mods.avaritiadelight.item.block.entity.ExtremeStoveBlockEntity;
import committee.nova.mods.avaritiadelight.registry.ADBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.*;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.CampfireCookingRecipe;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.block.StoveBlock;
import vectorwing.farmersdelight.common.registry.ModSounds;
import vectorwing.farmersdelight.common.utility.MathUtils;

import java.util.Optional;

public class ExtremeStoveBlock extends StoveBlock {
    public ExtremeStoveBlock(Settings properties) {
        super(properties);
    }

    @Override
    public ActionResult onUse(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack heldStack = player.getStackInHand(hand);
        Item heldItem = heldStack.getItem();
        if (state.get(LIT)) {
            if (heldStack.isIn(ItemTags.SHOVELS)) {
                this.extinguish(state, level, pos);
                heldStack.damage(1, player, action -> action.sendToolBreakStatus(hand));
                return ActionResult.SUCCESS;
            }

            if (heldItem == Items.WATER_BUCKET) {
                if (!level.isClient())
                    level.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                this.extinguish(state, level, pos);
                if (!player.isCreative())
                    player.setStackInHand(hand, new ItemStack(Items.BUCKET));
                return ActionResult.SUCCESS;
            }
        } else {
            if (heldItem instanceof FlintAndSteelItem) {
                level.playSound(player, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, MathUtils.RAND.nextFloat() * 0.4F + 0.8F);
                level.setBlockState(pos, state.with(Properties.LIT, Boolean.TRUE), 11);
                heldStack.damage(1, player, action -> action.sendToolBreakStatus(hand));
                return ActionResult.SUCCESS;
            }

            if (heldItem instanceof FireChargeItem) {
                level.playSound(null, pos, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 1.0F, (MathUtils.RAND.nextFloat() - MathUtils.RAND.nextFloat()) * 0.2F + 1.0F);
                level.setBlockState(pos, state.with(Properties.LIT, Boolean.TRUE), 11);
                if (!player.isCreative())
                    heldStack.decrement(1);
                return ActionResult.SUCCESS;
            }
        }

        BlockEntity tileEntity = level.getBlockEntity(pos);
        if (tileEntity instanceof ExtremeStoveBlockEntity stoveEntity) {
            int stoveSlot = stoveEntity.getNextEmptySlot();
            if (stoveSlot < 0 || stoveEntity.isStoveBlockedAbove())
                return ActionResult.PASS;
            Optional<CampfireCookingRecipe> recipe = stoveEntity.getMatchingRecipe(new SimpleInventory(heldStack), stoveSlot);
            if (recipe.isPresent()) {
                if (!level.isClient && stoveEntity.addItem(player.getAbilities().creativeMode ? heldStack.copy() : heldStack, recipe.get(), stoveSlot))
                    return ActionResult.SUCCESS;
                return ActionResult.CONSUME;
            }
        }
        return ActionResult.PASS;
    }

    @Override
    public void randomDisplayTick(BlockState stateIn, World level, BlockPos pos, Random rand) {
        if (stateIn.get(CampfireBlock.LIT)) {
            double x = pos.getX() + 0.5;
            double y = pos.getY();
            double z = pos.getZ() + 0.5;
            if (rand.nextInt(10) == 0)
                level.playSound(x, y, z, ModSounds.BLOCK_STOVE_CRACKLE.get(), SoundCategory.BLOCKS, 1.0F, 1.0F, false);

            Direction direction = stateIn.get(HorizontalFacingBlock.FACING);
            Direction.Axis direction$axis = direction.getAxis();
            double horizontalOffset = rand.nextDouble() * 0.6 - 0.3;
            double xOffset = direction$axis == Direction.Axis.X ? (double) direction.getOffsetX() * 0.52 : horizontalOffset;
            double yOffset = rand.nextDouble() * 6.0 / 16.0;
            double zOffset = direction$axis == Direction.Axis.Z ? (double) direction.getOffsetZ() * 0.52 : horizontalOffset;
            level.addParticle(ParticleTypes.SMOKE, x + xOffset, y + yOffset, z + zOffset, 0.0, 0.0, 0.0);
            level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, x + xOffset, y + yOffset, z + zOffset, 0.0, 0.0, 0.0);
        }
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ExtremeStoveBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World level, BlockState state, BlockEntityType<T> type) {
        return state.get(LIT) ? checkType(type, ADBlockEntities.EXTREME_STOVE.get(), level.isClient ? ExtremeStoveBlockEntity::animationTick : ExtremeStoveBlockEntity::cookingTick) : null;
    }
}
