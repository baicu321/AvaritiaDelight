package committee.nova.mods.avaritiadelight.item.block.entity;

import committee.nova.mods.avaritiadelight.registry.ADBlockEntities;
import committee.nova.mods.avaritiadelight.registry.ADItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.CampfireCookingRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.block.StoveBlock;
import vectorwing.farmersdelight.common.block.entity.SyncedBlockEntity;
import vectorwing.farmersdelight.common.mixin.accessor.RecipeManagerAccessor;
import vectorwing.farmersdelight.common.utility.ItemUtils;

import java.util.Optional;

public class ExtremeStoveBlockEntity extends SyncedBlockEntity {
    private static final VoxelShape GRILLING_AREA = Block.createCuboidShape(3.0, 0.0, 3.0, 13.0, 1.0, 13.0);
    private static final int INVENTORY_SLOT_COUNT = 6;
    private final DefaultedList<ItemStack> stacks = DefaultedList.ofSize(INVENTORY_SLOT_COUNT, ItemStack.EMPTY);
    private final int[] cookingTimes = new int[6];
    private final int[] cookingTimesTotal = new int[6];
    private Identifier[] lastRecipeIDs = new Identifier[6];

    public ExtremeStoveBlockEntity(BlockPos pos, BlockState state) {
        super(ADBlockEntities.EXTREME_STOVE.get(), pos, state);
    }

    @Override
    public void readNbt(NbtCompound compound) {
        super.readNbt(compound);
        this.stacks.clear();
        if (compound.contains("Inventory"))
            Inventories.readNbt(compound.getCompound("Inventory"), this.stacks);
        else
            Inventories.readNbt(compound, this.stacks);

        int[] arrayCookingTimesTotal;
        if (compound.contains("CookingTimes", 11)) {
            arrayCookingTimesTotal = compound.getIntArray("CookingTimes");
            System.arraycopy(arrayCookingTimesTotal, 0, this.cookingTimes, 0, Math.min(this.cookingTimesTotal.length, arrayCookingTimesTotal.length));
        }

        if (compound.contains("CookingTotalTimes", 11)) {
            arrayCookingTimesTotal = compound.getIntArray("CookingTotalTimes");
            System.arraycopy(arrayCookingTimesTotal, 0, this.cookingTimesTotal, 0, Math.min(this.cookingTimesTotal.length, arrayCookingTimesTotal.length));
        }

    }

    @Override
    public void writeNbt(NbtCompound compound) {
        this.writeItems(compound);
        compound.putIntArray("CookingTimes", this.cookingTimes);
        compound.putIntArray("CookingTotalTimes", this.cookingTimesTotal);
    }

    private NbtCompound writeItems(NbtCompound compound) {
        super.writeNbt(compound);
        NbtCompound nbt = new NbtCompound();
        Inventories.writeNbt(nbt, this.stacks);
        compound.put("Inventory", nbt);
        return compound;
    }

    public static void cookingTick(World level, BlockPos pos, BlockState state, ExtremeStoveBlockEntity stove) {
        boolean isStoveLit = state.get(StoveBlock.LIT);
        if (stove.isStoveBlockedAbove()) {
            for (int i = 0; i < stove.stacks.size(); i++) {
                ItemStack stack = stove.stacks.get(i);
                if (!stack.isEmpty()) {
                    stove.stacks.set(i, ItemStack.EMPTY);
                    Block.dropStack(level, pos, stack);
                }
                stove.inventoryChanged();
            }
        } else if (isStoveLit)
            stove.cookAndOutputItems();
        else
            for (int i = 0; i < stove.stacks.size(); ++i)
                if (stove.cookingTimes[i] > 0)
                    stove.cookingTimes[i] = MathHelper.clamp(stove.cookingTimes[i] - 2, 0, stove.cookingTimesTotal[i]);
    }

    public static void animationTick(World level, BlockPos pos, BlockState state, ExtremeStoveBlockEntity stove) {
        for (int i = 0; i < stove.stacks.size(); ++i) {
            if (!stove.stacks.get(i).isEmpty() && level.random.nextFloat() < 0.2F) {
                Vec2f stoveItemVector = stove.getStoveItemOffset(i);
                Direction direction = state.get(StoveBlock.FACING);
                int directionIndex = direction.getHorizontal();
                Vec2f offset = directionIndex % 2 == 0 ? stoveItemVector : new Vec2f(stoveItemVector.y, stoveItemVector.x);
                double x = (double) pos.getX() + 0.5 - (double) ((float) direction.getOffsetX() * offset.x) + (double) ((float) direction.rotateYClockwise().getOffsetX() * offset.x);
                double y = (double) pos.getY() + 1.0;
                double z = (double) pos.getZ() + 0.5 - (double) ((float) direction.getOffsetZ() * offset.y) + (double) ((float) direction.rotateYClockwise().getOffsetZ() * offset.y);

                for (int k = 0; k < 3; ++k)
                    level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 5.0E-4, 0.0);
            }
        }
    }

    private void cookAndOutputItems() {
        if (this.world != null) {
            boolean didInventoryChange = false;

            for (int i = 0; i < this.stacks.size(); ++i) {
                ItemStack stoveStack = this.stacks.get(i);
                if (!stoveStack.isEmpty()) {
                    this.cookingTimes[i]++;
                    if (stoveStack.isOf(ADItems.COSMIC_BEEF.get())) {
                        if (this.cookingTimes[i] >= 10 * 20) {
                            ItemUtils.spawnItemEntity(this.world, new ItemStack(ADItems.COSMIC_BEEF_COOKED.get()), (double) this.pos.getX() + 0.5, (double) this.pos.getY() + 1.0, (double) this.pos.getZ() + 0.5, this.world.random.nextGaussian() * 0.009999999776482582, 0.10000000149011612, this.world.random.nextGaussian() * 0.009999999776482582);
                            this.stacks.set(i, ItemStack.EMPTY);
                            didInventoryChange = true;
                        }
                    } else if (this.cookingTimes[i] >= 20) {
                        Inventory inventoryWrapper = new SimpleInventory(stoveStack);
                        Optional<CampfireCookingRecipe> recipe = this.getMatchingRecipe(inventoryWrapper, i);
                        if (recipe.isPresent()) {
                            ItemStack resultStack = recipe.get().getOutput(this.world.getRegistryManager());
                            if (!resultStack.isEmpty())
                                ItemUtils.spawnItemEntity(this.world, resultStack.copy(), (double) this.pos.getX() + 0.5, (double) this.pos.getY() + 1.0, (double) this.pos.getZ() + 0.5, this.world.random.nextGaussian() * 0.009999999776482582, 0.10000000149011612, this.world.random.nextGaussian() * 0.009999999776482582);
                        }

                        this.stacks.set(i, ItemStack.EMPTY);
                        didInventoryChange = true;
                    }
                }
            }

            if (didInventoryChange)
                this.inventoryChanged();
        }
    }

    public int getNextEmptySlot() {
        for (int i = 0; i < this.stacks.size(); ++i) {
            ItemStack slotStack = this.stacks.get(i);
            if (slotStack.isEmpty())
                return i;
        }

        return -1;
    }

    public boolean addItem(ItemStack itemStackIn, CampfireCookingRecipe recipe, int slot) {
        if (0 <= slot && slot < this.stacks.size()) {
            ItemStack slotStack = this.stacks.get(slot);
            if (slotStack.isEmpty()) {
                this.cookingTimesTotal[slot] = recipe.getCookTime();
                this.cookingTimes[slot] = 0;
                this.stacks.set(slot, itemStackIn.split(1));
                this.lastRecipeIDs[slot] = recipe.getId();
                this.inventoryChanged();
                return true;
            }
        }
        return false;
    }

    public Optional<CampfireCookingRecipe> getMatchingRecipe(Inventory recipeWrapper, int slot) {
        if (this.world == null)
            return Optional.empty();
        else {
            if (this.lastRecipeIDs[slot] != null) {
                Recipe<Inventory> recipe = ((RecipeManagerAccessor) this.world.getRecipeManager()).getRecipeMap(RecipeType.CAMPFIRE_COOKING).get(this.lastRecipeIDs[slot]);
                if (recipe instanceof CampfireCookingRecipe cookingRecipe && recipe.matches(recipeWrapper, this.world))
                    return Optional.of(cookingRecipe);
            }
            return this.world.getRecipeManager().getFirstMatch(RecipeType.CAMPFIRE_COOKING, recipeWrapper, this.world);
        }
    }

    public DefaultedList<ItemStack> getInventory() {
        return this.stacks;
    }

    public boolean isStoveBlockedAbove() {
        if (this.world != null) {
            BlockState above = this.world.getBlockState(this.pos.up());
            return VoxelShapes.matchesAnywhere(GRILLING_AREA, above.getOutlineShape(this.world, this.pos.up()), BooleanBiFunction.AND);
        } else {
            return false;
        }
    }

    public Vec2f getStoveItemOffset(int index) {
        Vec2f[] OFFSETS = new Vec2f[]{new Vec2f(0.3F, 0.2F), new Vec2f(0.0F, 0.2F), new Vec2f(-0.3F, 0.2F), new Vec2f(0.3F, -0.2F), new Vec2f(0.0F, -0.2F), new Vec2f(-0.3F, -0.2F)};
        return OFFSETS[index];
    }

    @Override
    public @NotNull NbtCompound toInitialChunkDataNbt() {
        return this.writeItems(new NbtCompound());
    }

    @Override
    public @Nullable Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }
}
