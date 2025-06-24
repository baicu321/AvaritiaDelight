package committee.nova.mods.avaritiadelight.item.block.entity;

import committee.nova.mods.avaritiadelight.AvaritiaDelight;
import committee.nova.mods.avaritiadelight.recipe.CropExtractorRecipe;
import committee.nova.mods.avaritiadelight.registry.ADBlockEntities;
import committee.nova.mods.avaritiadelight.screen.handler.CropExtractorScreenHandler;
import committee.nova.mods.avaritiadelight.util.ImplementedInventory;
import committee.nova.mods.avaritiadelight.util.InventoryHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class CropExtractorBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {
    private final DefaultedList<ItemStack> stacks = DefaultedList.ofSize(5, ItemStack.EMPTY);
    private int time;
    private int timeTotal;
    private CropExtractorRecipe lastRecipe = null;
    private final PropertyDelegate cookingProgress = new PropertyDelegate() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> CropExtractorBlockEntity.this.time;
                case 1 -> CropExtractorBlockEntity.this.timeTotal;
                default -> throw new IllegalStateException("Unexpected value: " + index);
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0:
                    CropExtractorBlockEntity.this.time = value;
                case 1:
                    CropExtractorBlockEntity.this.timeTotal = value;
            }
        }

        @Override
        public int size() {
            return 2;
        }
    };

    public CropExtractorBlockEntity(BlockPos pos, BlockState state) {
        super(ADBlockEntities.CROP_EXTRACTOR.get(), pos, state);
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("block.%s.crop_extractor".formatted(AvaritiaDelight.MOD_ID));
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new CropExtractorScreenHandler(syncId, this, playerInventory, this.cookingProgress);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return this.stacks;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt.getCompound("Items"), this.stacks);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.put("Items", InventoryHelper.writeNbt(new NbtCompound(), this.stacks));
    }

    public static void tick(World world, BlockPos pos, BlockState state, CropExtractorBlockEntity blockEntity) {
        CropExtractorRecipe matched = world.getRecipeManager().getFirstMatch(CropExtractorRecipe.Type.INSTANCE, blockEntity, world).orElse(null);
        if (blockEntity.lastRecipe != null && matched != null && Objects.equals(blockEntity.lastRecipe.getId(), matched.getId()) && InventoryHelper.canInsertItems(1, blockEntity, matched.getOutputs())) {
            blockEntity.time++;
            if (blockEntity.time >= blockEntity.timeTotal) {
                blockEntity.time = 0;
                blockEntity.getStack(0).decrement(1);
                InventoryHelper.insertItems(1, blockEntity, matched.getOutputs());
            }
        } else {
            blockEntity.lastRecipe = matched;
            blockEntity.time = 0;
            blockEntity.timeTotal = matched == null ? 0 : matched.getTime();
        }
    }
}
