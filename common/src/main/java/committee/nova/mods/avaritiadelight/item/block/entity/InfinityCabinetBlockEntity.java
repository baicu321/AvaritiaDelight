package committee.nova.mods.avaritiadelight.item.block.entity;

import committee.nova.mods.avaritiadelight.AvaritiaDelight;
import committee.nova.mods.avaritiadelight.registry.ADBlockEntities;
import committee.nova.mods.avaritiadelight.screen.handler.InfinityCabinetScreenHandler;
import committee.nova.mods.avaritiadelight.util.ImplementedInventory;
import committee.nova.mods.avaritiadelight.util.InventoryHelper;
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ViewerCountManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Nameable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class InfinityCabinetBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory, Nameable {
    private final ViewerCountManager viewerCountManager;
    private final DefaultedList<ItemStack> stacks = DefaultedList.ofSize(243, ItemStack.EMPTY);
    @Nullable
    private Text customName = null;

    public InfinityCabinetBlockEntity(BlockPos pos, BlockState state) {
        super(ADBlockEntities.INFINITY_CABINET.get(), pos, state);
        this.viewerCountManager = new ViewerCountManager() {
            @Override
            protected void onContainerOpen(World world, BlockPos pos, BlockState state) {
                InfinityCabinetBlockEntity.this.playSound(state, SoundEvents.BLOCK_BARREL_OPEN);
                InfinityCabinetBlockEntity.this.setOpen(state, true);
            }

            @Override
            protected void onContainerClose(World world, BlockPos pos, BlockState state) {
                InfinityCabinetBlockEntity.this.playSound(state, SoundEvents.BLOCK_BARREL_CLOSE);
                InfinityCabinetBlockEntity.this.setOpen(state, false);
            }

            @Override
            protected void onViewerCountUpdate(World world, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount) {
            }

            @Override
            protected boolean isPlayerViewing(PlayerEntity player) {
                return player.currentScreenHandler instanceof InfinityCabinetScreenHandler handler && handler.getInventory() == InfinityCabinetBlockEntity.this;
            }
        };
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.stacks.clear();
        InventoryHelper.readNbt(nbt.getCompound("Items"), this.stacks);
        if (nbt.contains("CustomName")) this.customName = Text.Serializer.fromJson(nbt.getString("CustomName"));
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.put("Items", InventoryHelper.writeNbt(new NbtCompound(), this.stacks));
        if (this.customName != null) nbt.putString("CustomName", Text.Serializer.toJson(this.customName));
    }

    @Override
    public Text getName() {
        return this.customName;
    }

    @Override
    public Text getDisplayName() {
        return this.customName != null ? this.customName : Text.translatable("block.%s.infinity_cabinet".formatted(AvaritiaDelight.MOD_ID));
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new InfinityCabinetScreenHandler(syncId, this, playerInventory);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return this.stacks;
    }

    public void setCustomName(@Nullable Text customName) {
        this.customName = customName;
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return Inventory.canPlayerUse(this, player);
    }

    @Override
    public int getMaxCountPerStack() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void onOpen(PlayerEntity player) {
        if (!this.removed && !player.isSpectator())
            this.viewerCountManager.openContainer(player, this.getWorld(), this.getPos(), this.getCachedState());
    }

    @Override
    public void onClose(PlayerEntity player) {
        if (!this.removed && !player.isSpectator())
            this.viewerCountManager.closeContainer(player, this.getWorld(), this.getPos(), this.getCachedState());
    }

    public void tick() {
        if (!this.removed)
            this.viewerCountManager.updateViewerCount(this.getWorld(), this.getPos(), this.getCachedState());
    }

    private void setOpen(BlockState state, boolean open) {
        assert this.world != null;
        this.world.setBlockState(this.getPos(), state.with(BarrelBlock.OPEN, open), 3);
    }

    private void playSound(BlockState state, SoundEvent soundEvent) {
        Vec3i vec3i = state.get(BarrelBlock.FACING).getVector();
        double d = (double) this.pos.getX() + 0.5 + (double) vec3i.getX() / 2.0;
        double e = (double) this.pos.getY() + 0.5 + (double) vec3i.getY() / 2.0;
        double f = (double) this.pos.getZ() + 0.5 + (double) vec3i.getZ() / 2.0;
        assert this.world != null;
        this.world.playSound(null, d, e, f, soundEvent, SoundCategory.BLOCKS, 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
    }
}
