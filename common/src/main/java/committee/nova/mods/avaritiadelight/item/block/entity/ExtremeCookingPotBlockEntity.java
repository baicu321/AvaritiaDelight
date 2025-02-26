package committee.nova.mods.avaritiadelight.item.block.entity;

import committee.nova.mods.avaritiadelight.AvaritiaDelight;
import committee.nova.mods.avaritiadelight.recipe.ExtremeCookingPotRecipe;
import committee.nova.mods.avaritiadelight.registry.ADBlockEntities;
import committee.nova.mods.avaritiadelight.screen.handler.ExtremeCookingPotScreenHandler;
import committee.nova.mods.avaritiadelight.util.ImplementedInventory;
import dev.architectury.registry.menu.ExtendedMenuProvider;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Nameable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.block.CookingPotBlock;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;
import vectorwing.farmersdelight.common.block.entity.HeatableBlockEntity;
import vectorwing.farmersdelight.common.block.entity.SyncedBlockEntity;
import vectorwing.farmersdelight.common.registry.ModParticleTypes;
import vectorwing.farmersdelight.common.utility.ItemUtils;

import java.util.Optional;

public class ExtremeCookingPotBlockEntity extends SyncedBlockEntity implements ExtendedMenuProvider, ImplementedInventory, HeatableBlockEntity, Nameable {
    private static final int RESULT_SLOT = 81, CONTAINER_SLOT = 82, RESULT_WITH_CONTAINER_SLOT = 83;
    private final DefaultedList<ItemStack> stacks = DefaultedList.ofSize(84, ItemStack.EMPTY);
    @Nullable
    private Text customName;
    private int cookTime;
    private int cookTimeTotal;
    @Nullable
    private ItemStack mealContainerStack;
    private final PropertyDelegate cookingProgress = new PropertyDelegate() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> cookTime;
                case 1 -> cookTimeTotal;
                default -> throw new IllegalStateException("Unexpected value: " + index);
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0:
                    cookTime = value;
                case 1:
                    cookTimeTotal = value;
            }
        }

        @Override
        public int size() {
            return 2;
        }
    };

    public ExtremeCookingPotBlockEntity(BlockPos pos, BlockState state) {
        super(ADBlockEntities.EXTREME_COOKING_POT.get(), pos, state);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.stacks.clear();
        Inventories.readNbt(nbt.getCompound("Items"), this.stacks);
        if (nbt.contains("CustomName"))
            this.customName = Text.Serializer.fromJson(nbt.getString("CustomName"));
        this.cookTime = nbt.getInt("cookTime");
        this.cookTimeTotal = nbt.getInt("cookTimeTotal");
        if (nbt.contains("mealContainerStack"))
            this.mealContainerStack = ItemStack.fromNbt(nbt.getCompound("mealContainerStack"));
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        NbtCompound compound = new NbtCompound();
        Inventories.writeNbt(compound, this.stacks);
        nbt.put("Items", compound);
        if (this.customName != null)
            nbt.putString("CustomName", Text.Serializer.toJson(this.customName));
        nbt.putInt("cookTime", this.cookTime);
        nbt.putInt("cookTimeTotal", this.cookTimeTotal);
        if (this.mealContainerStack != null)
            nbt.put("mealContainerStack", this.mealContainerStack.writeNbt(new NbtCompound()));
    }

    @Override
    public Text getName() {
        return this.customName;
    }

    public void setCustomName(@Nullable Text customName) {
        this.customName = customName;
    }

    @Override
    public Text getDisplayName() {
        return this.customName != null ? this.customName : Text.translatable("screen.%s.extreme_cooking_pot".formatted(AvaritiaDelight.MOD_ID));
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new ExtremeCookingPotScreenHandler(syncId, this, playerInventory, this, this.cookingProgress);
    }

    @Override
    public void saveExtraData(PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return this.stacks;
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return Inventory.canPlayerUse(this, player);
    }

    public boolean isHeated() {
        return this.world != null && this.isHeated(this.world, this.pos);
    }

    private boolean hasInput() {
        for (int i = 0; i < RESULT_SLOT; ++i)
            if (!this.getStack(i).isEmpty())
                return true;
        return false;
    }

    public static void cookingTick(World level, BlockPos pos, BlockState state, ExtremeCookingPotBlockEntity cookingPot) {
        boolean isHeated = cookingPot.isHeated(level, pos);
        boolean didInventoryChange = false;
        if (isHeated && cookingPot.hasInput()) {
            Optional<ExtremeCookingPotRecipe> recipe = level.getRecipeManager().getFirstMatch(ExtremeCookingPotRecipe.Type.INSTANCE, cookingPot, level);
            if (recipe.isPresent() && cookingPot.canCook(recipe.get())) {
                didInventoryChange = cookingPot.processCooking(recipe.get(), cookingPot);
            } else {
                cookingPot.cookTime = 0;
            }
        } else if (cookingPot.cookTime > 0) {
            cookingPot.cookTime = MathHelper.clamp(cookingPot.cookTime - 2, 0, cookingPot.cookTimeTotal);
        }

        ItemStack mealStack = cookingPot.getMeal();
        if (!mealStack.isEmpty()) {
            if (!cookingPot.doesMealHaveContainer(mealStack)) {
                cookingPot.moveMealToOutput();
                didInventoryChange = true;
            } else if (!cookingPot.getStack(CONTAINER_SLOT).isEmpty()) {
                cookingPot.useStoredContainersOnMeal();
                didInventoryChange = true;
            }
        }

        if (didInventoryChange)
            cookingPot.inventoryChanged();
    }

    private void moveMealToOutput() {
        ItemStack mealStack = this.getStack(RESULT_SLOT);
        ItemStack outputStack = this.getStack(RESULT_WITH_CONTAINER_SLOT);
        int mealCount = Math.min(mealStack.getCount(), mealStack.getMaxCount() - outputStack.getCount());
        if (outputStack.isEmpty()) {
            this.setStack(RESULT_WITH_CONTAINER_SLOT, mealStack.split(mealCount));
        } else if (outputStack.getItem() == mealStack.getItem()) {
            mealStack.decrement(mealCount);
            outputStack.increment(mealCount);
        }

    }

    protected boolean canCook(ExtremeCookingPotRecipe recipe) {
        if (this.hasInput()) {
            ItemStack resultStack = recipe.getOutput(this.world.getRegistryManager());
            if (resultStack.isEmpty()) {
                return false;
            } else {
                ItemStack storedMealStack = this.getStack(RESULT_SLOT);
                if (storedMealStack.isEmpty())
                    return true;
                else if (!ItemStack.areItemsEqual(storedMealStack, resultStack))
                    return false;
                else if (storedMealStack.getCount() + resultStack.getCount() <= this.getStack(RESULT_SLOT).getMaxCount())
                    return true;
                else
                    return storedMealStack.getCount() + resultStack.getCount() <= resultStack.getMaxCount();

            }
        } else {
            return false;
        }
    }

    private boolean processCooking(ExtremeCookingPotRecipe recipe, ExtremeCookingPotBlockEntity cookingPot) {
        if (this.world == null) {
            return false;
        } else {
            ++this.cookTime;
            this.cookTimeTotal = recipe.getCookTime();
            if (this.cookTime < this.cookTimeTotal) {
                return false;
            } else {
                this.cookTime = 0;
                this.mealContainerStack = recipe.getOutputContainer();
                ItemStack resultStack = recipe.getOutput(this.world.getRegistryManager());
                ItemStack storedMealStack = this.getStack(RESULT_SLOT);
                if (storedMealStack.isEmpty()) {
                    this.setStack(RESULT_SLOT, resultStack.copy());
                } else if (ItemStack.areItemsEqual(storedMealStack, resultStack)) {
                    storedMealStack.increment(resultStack.getCount());
                }

                //FIXME:: Unlocker?
//                cookingPot.setLastRecipe(recipe);

                for (int i = 0; i < 6; ++i) {
                    ItemStack slotStack = this.getStack(i);
                    if (slotStack.getItem().hasRecipeRemainder())
                        this.ejectIngredientRemainder(slotStack.getItem().getRecipeRemainder().getDefaultStack());
                    else if (CookingPotBlockEntity.INGREDIENT_REMAINDER_OVERRIDES.containsKey(slotStack.getItem()))
                        this.ejectIngredientRemainder(CookingPotBlockEntity.INGREDIENT_REMAINDER_OVERRIDES.get(slotStack.getItem()).getDefaultStack());

                    if (!slotStack.isEmpty())
                        slotStack.decrement(1);
                }

                return true;
            }
        }
    }

    protected void ejectIngredientRemainder(ItemStack remainderStack) {
        Direction direction = this.getCachedState().get(CookingPotBlock.FACING).rotateYCounterclockwise();
        double x = this.pos.getX() + 0.5 + direction.getOffsetX() * 0.25;
        double y = this.pos.getY() + 0.7;
        double z = this.pos.getZ() + 0.5 + direction.getOffsetZ() * 0.25;
        ItemUtils.spawnItemEntity(this.world, remainderStack, x, y, z, direction.getOffsetX() * 0.08, 0.25, direction.getOffsetZ() * 0.08);
    }

    public ItemStack getContainer() {
        ItemStack mealStack = this.getMeal();
        return !mealStack.isEmpty() && !this.mealContainerStack.isEmpty() ? this.mealContainerStack : mealStack.getItem().getRecipeRemainder().getDefaultStack();
    }

    private void useStoredContainersOnMeal() {
        ItemStack mealStack = this.getStack(RESULT_SLOT);
        ItemStack containerInputStack = this.getStack(CONTAINER_SLOT);
        ItemStack outputStack = this.getStack(RESULT_WITH_CONTAINER_SLOT);
        if (this.isContainerValid(containerInputStack) && outputStack.getCount() < outputStack.getMaxCount()) {
            int smallerStackCount = Math.min(mealStack.getCount(), containerInputStack.getCount());
            int mealCount = Math.min(smallerStackCount, mealStack.getMaxCount() - outputStack.getCount());
            if (outputStack.isEmpty()) {
                containerInputStack.decrement(mealCount);
                this.setStack(RESULT_WITH_CONTAINER_SLOT, mealStack.split(mealCount));
            } else if (outputStack.getItem() == mealStack.getItem()) {
                mealStack.decrement(mealCount);
                containerInputStack.decrement(mealCount);
                outputStack.increment(mealCount);
            }
        }

    }

    public ItemStack getMeal() {
        return this.getStack(RESULT_SLOT);
    }

    private boolean doesMealHaveContainer(ItemStack meal) {
        return !this.mealContainerStack.isEmpty() || meal.getItem().hasRecipeRemainder();
    }

    public boolean isContainerValid(ItemStack containerItem) {
        return !containerItem.isEmpty() && (!this.mealContainerStack.isEmpty() ? ItemStack.areItemsEqual(this.mealContainerStack, containerItem) : ItemStack.areItemsEqual(this.getMeal(), containerItem));
    }

    public ItemStack useHeldItemOnMeal(ItemStack container) {
        if (this.isContainerValid(container) && !this.getMeal().isEmpty()) {
            container.decrement(1);
            this.inventoryChanged();
            return this.getMeal().split(1);
        } else {
            return ItemStack.EMPTY;
        }
    }

    public static void animationTick(World level, BlockPos pos, BlockState state, ExtremeCookingPotBlockEntity cookingPot) {
        if (cookingPot.isHeated(level, pos)) {
            Random random = level.random;
            double x;
            double y;
            double z;
            if (random.nextFloat() < 0.2F) {
                x = (double) pos.getX() + 0.5 + (random.nextDouble() * 0.6 - 0.3);
                y = (double) pos.getY() + 0.7;
                z = (double) pos.getZ() + 0.5 + (random.nextDouble() * 0.6 - 0.3);
                level.addParticle(ParticleTypes.BUBBLE_POP, x, y, z, 0.0, 0.0, 0.0);
            }

            if (random.nextFloat() < 0.05F) {
                x = (double) pos.getX() + 0.5 + (random.nextDouble() * 0.4 - 0.2);
                y = (double) pos.getY() + 0.5;
                z = (double) pos.getZ() + 0.5 + (random.nextDouble() * 0.4 - 0.2);
                double motionY = random.nextBoolean() ? 0.015 : 0.005;
                level.addParticle(ModParticleTypes.STEAM.get(), x, y, z, 0.0, motionY, 0.0);
            }
        }
    }
}
