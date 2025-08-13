package committee.nova.mods.avaritiadelight.item.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.CampfireCookingRecipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Clearable;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class StarCampFireBlockEntity extends BlockEntity implements Clearable {
    private static final int field_31330 = 2;
    private static final int field_31331 = 4;
    private final DefaultedList<ItemStack> itemsBeingCooked;
    private final int[] cookingTimes;
    private final int[] cookingTotalTimes;
    private final RecipeManager.MatchGetter<Inventory, CampfireCookingRecipe> matchGetter;

    public StarCampFireBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityType.CAMPFIRE, pos, state);
        this.itemsBeingCooked = DefaultedList.ofSize(4, ItemStack.EMPTY);
        this.cookingTimes = new int[4];
        this.cookingTotalTimes = new int[4];
        this.matchGetter = RecipeManager.createCachedMatchGetter(RecipeType.CAMPFIRE_COOKING);
    }

    public static void litServerTick(World world, BlockPos pos, BlockState state, StarCampFireBlockEntity campfire) {
        boolean bl = false;

        for(int i = 0; i < campfire.itemsBeingCooked.size(); ++i) {
            ItemStack itemStack = (ItemStack)campfire.itemsBeingCooked.get(i);
            if (!itemStack.isEmpty()) {
                bl = true;
                int var10002 = campfire.cookingTimes[i]++;
                if (campfire.cookingTimes[i] >= campfire.cookingTotalTimes[i]) {
                    Inventory inventory = new SimpleInventory(new ItemStack[]{itemStack});
                    ItemStack itemStack2 = (ItemStack)campfire.matchGetter.getFirstMatch(inventory, world).map((recipe) -> recipe.craft(inventory, world.getRegistryManager())).orElse(itemStack);
                    if (itemStack2.isItemEnabled(world.getEnabledFeatures())) {
                        ItemScatterer.spawn(world, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), itemStack2);
                        campfire.itemsBeingCooked.set(i, ItemStack.EMPTY);
                        world.updateListeners(pos, state, state, 3);
                        world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(state));
                    }
                }
            }
        }

        if (bl) {
            markDirty(world, pos, state);
        }

    }

    public static void unlitServerTick(World world, BlockPos pos, BlockState state, StarCampFireBlockEntity campfire) {
        boolean bl = false;

        for(int i = 0; i < campfire.itemsBeingCooked.size(); ++i) {
            if (campfire.cookingTimes[i] > 0) {
                bl = true;
                campfire.cookingTimes[i] = MathHelper.clamp(campfire.cookingTimes[i] - 2, 0, campfire.cookingTotalTimes[i]);
            }
        }

        if (bl) {
            markDirty(world, pos, state);
        }

    }

    public static void clientTick(World world, BlockPos pos, BlockState state, StarCampFireBlockEntity campfire) {
        Random random = world.random;
        if (random.nextFloat() < 0.11F) {
            for(int i = 0; i < random.nextInt(2) + 2; ++i) {
                CampfireBlock.spawnSmokeParticle(world, pos, (Boolean)state.get(CampfireBlock.SIGNAL_FIRE), false);
            }
        }

        int i = ((Direction)state.get(CampfireBlock.FACING)).getHorizontal();

        for(int j = 0; j < campfire.itemsBeingCooked.size(); ++j) {
            if (!((ItemStack)campfire.itemsBeingCooked.get(j)).isEmpty() && random.nextFloat() < 0.2F) {
                Direction direction = Direction.fromHorizontal(Math.floorMod(j + i, 4));
                float f = 0.3125F;
                double d = (double)pos.getX() + (double)0.5F - (double)((float)direction.getOffsetX() * 0.3125F) + (double)((float)direction.rotateYClockwise().getOffsetX() * 0.3125F);
                double e = (double)pos.getY() + (double)0.5F;
                double g = (double)pos.getZ() + (double)0.5F - (double)((float)direction.getOffsetZ() * 0.3125F) + (double)((float)direction.rotateYClockwise().getOffsetZ() * 0.3125F);

                for(int k = 0; k < 4; ++k) {
                    world.addParticle(ParticleTypes.SMOKE, d, e, g, (double)0.0F, 5.0E-4, (double)0.0F);
                }
            }
        }

    }

    public DefaultedList<ItemStack> getItemsBeingCooked() {
        return this.itemsBeingCooked;
    }

    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.itemsBeingCooked.clear();
        Inventories.readNbt(nbt, this.itemsBeingCooked);
        if (nbt.contains("CookingTimes", 11)) {
            int[] is = nbt.getIntArray("CookingTimes");
            System.arraycopy(is, 0, this.cookingTimes, 0, Math.min(this.cookingTotalTimes.length, is.length));
        }

        if (nbt.contains("CookingTotalTimes", 11)) {
            int[] is = nbt.getIntArray("CookingTotalTimes");
            System.arraycopy(is, 0, this.cookingTotalTimes, 0, Math.min(this.cookingTotalTimes.length, is.length));
        }

    }

    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, this.itemsBeingCooked, true);
        nbt.putIntArray("CookingTimes", this.cookingTimes);
        nbt.putIntArray("CookingTotalTimes", this.cookingTotalTimes);
    }

    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound nbtCompound = new NbtCompound();
        Inventories.writeNbt(nbtCompound, this.itemsBeingCooked, true);
        return nbtCompound;
    }

    public Optional<CampfireCookingRecipe> getRecipeFor(ItemStack stack) {
        return this.itemsBeingCooked.stream().noneMatch(ItemStack::isEmpty) ? Optional.empty() : this.matchGetter.getFirstMatch(new SimpleInventory(new ItemStack[]{stack}), this.world);
    }

    public boolean addItem(@Nullable Entity user, ItemStack stack, int cookTime) {
        for(int i = 0; i < this.itemsBeingCooked.size(); ++i) {
            ItemStack itemStack = (ItemStack)this.itemsBeingCooked.get(i);
            if (itemStack.isEmpty()) {
                this.cookingTotalTimes[i] = cookTime;
                this.cookingTimes[i] = 0;
                this.itemsBeingCooked.set(i, stack.split(1));
                this.world.emitGameEvent(GameEvent.BLOCK_CHANGE, this.getPos(), GameEvent.Emitter.of(user, this.getCachedState()));
                this.updateListeners();
                return true;
            }
        }

        return false;
    }

    private void updateListeners() {
        this.markDirty();
        this.getWorld().updateListeners(this.getPos(), this.getCachedState(), this.getCachedState(), 3);
    }

    public void clear() {
        this.itemsBeingCooked.clear();
    }

    public void spawnItemsBeingCooked() {
        if (this.world != null) {
            this.updateListeners();
        }

    }
}
