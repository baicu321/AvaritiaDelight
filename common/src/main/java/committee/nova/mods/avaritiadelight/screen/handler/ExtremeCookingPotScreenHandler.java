package committee.nova.mods.avaritiadelight.screen.handler;

import committee.nova.mods.avaritiadelight.item.block.entity.ExtremeCookingPotBlockEntity;
import committee.nova.mods.avaritiadelight.registry.ADScreenHandlers;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

import java.util.Objects;

public class ExtremeCookingPotScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final PlayerInventory playerInventory;
    private final ExtremeCookingPotBlockEntity blockEntity;
    private final PropertyDelegate delegate;

    public ExtremeCookingPotScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, new SimpleInventory(84), playerInventory, getBlockEntity(playerInventory, buf), new ArrayPropertyDelegate(2));
    }

    public ExtremeCookingPotScreenHandler(int syncId, Inventory inventory, PlayerInventory playerInventory, ExtremeCookingPotBlockEntity blockEntity, PropertyDelegate delegate) {
        super(ADScreenHandlers.EXTREME_COOKING_POT.get(), syncId);
        checkSize(inventory, 84);
        checkDataCount(delegate, 2);
        this.inventory = inventory;
        this.inventory.onOpen(playerInventory.player);
        this.playerInventory = playerInventory;
        this.blockEntity = blockEntity;
        this.delegate = delegate;
        this.addProperties(this.delegate);

        for (int i = 0; i < 9; ++i)
            for (int l = 0; l < 9; ++l)
                this.addSlot(new Slot(inventory, l + i * 9, l * 18 - 3, 28 + i * 18));
        this.addSlot(new Slot(inventory, ExtremeCookingPotBlockEntity.RESULT_SLOT, 195, 100) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            @Override
            public boolean canTakeItems(PlayerEntity playerEntity) {
                return false;
            }
        });//Output
        this.addSlot(new Slot(inventory, ExtremeCookingPotBlockEntity.CONTAINER_SLOT, 163, 135));//Container
        this.addSlot(new Slot(inventory, ExtremeCookingPotBlockEntity.RESULT_WITH_CONTAINER_SLOT, 195, 135) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }
        });//Final
        for (int i = 0; i < 3; ++i)
            for (int l = 0; l < 9; ++l)
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 28 + l * 18, 206 + i * 18));
        for (int i = 0; i < 9; ++i)
            this.addSlot(new Slot(playerInventory, i, 28 + i * 18, 264));
        this.inventory.markDirty();
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (this.inventory.size() <= invSlot && invSlot < this.inventory.size() + this.playerInventory.size()) {
                if (!this.insertItem(originalStack, 0, 81, false))
                    return ItemStack.EMPTY;
            } else if (!this.insertItem(originalStack, this.inventory.size(), this.inventory.size() + this.playerInventory.size(), false))
                return ItemStack.EMPTY;
            if (originalStack.isEmpty())
                slot.setStack(ItemStack.EMPTY);
            else
                slot.markDirty();
        }
        return newStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.inventory.onClose(player);
    }

    @Override
    public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
        super.onSlotClick(slotIndex, button, actionType, player);
        if (slotIndex >= 0)
            this.slots.get(slotIndex).markDirty();
    }

    public int getCookProgressionScaled() {
        double i = this.delegate.get(0);
        double j = this.delegate.get(1);
        return j != 0 && i != 0 ? (int) (i * 24 / j) : 0;
    }

    public boolean isHeated() {
        return this.blockEntity.isHeated();
    }

    private static ExtremeCookingPotBlockEntity getBlockEntity(PlayerInventory playerInventory, PacketByteBuf data) {
        Objects.requireNonNull(playerInventory, "playerInventory cannot be null");
        Objects.requireNonNull(data, "data cannot be null");
        BlockEntity tileAtPos = playerInventory.player.getWorld().getBlockEntity(data.readBlockPos());
        if (tileAtPos instanceof ExtremeCookingPotBlockEntity blockEntity) return blockEntity;
        else throw new IllegalStateException("Tile entity is not correct! " + tileAtPos);

    }
}
