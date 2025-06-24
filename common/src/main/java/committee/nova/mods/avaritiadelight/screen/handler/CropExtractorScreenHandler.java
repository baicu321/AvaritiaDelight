package committee.nova.mods.avaritiadelight.screen.handler;

import committee.nova.mods.avaritiadelight.registry.ADScreenHandlers;
import committee.nova.mods.avaritiadelight.screen.slot.TakeOnlySlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

public class CropExtractorScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final PlayerInventory playerInventory;
    private final PropertyDelegate delegate;

    public CropExtractorScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, new SimpleInventory(5), playerInventory, new ArrayPropertyDelegate(2));
    }

    public CropExtractorScreenHandler(int syncId, Inventory inventory, PlayerInventory playerInventory, PropertyDelegate delegate) {
        super(ADScreenHandlers.CROP_EXTRACTOR.get(), syncId);
        checkSize(inventory, 5);
        checkDataCount(delegate, 2);
        this.inventory = inventory;
        this.inventory.onOpen(playerInventory.player);
        this.playerInventory = playerInventory;
        this.delegate = delegate;
        this.addProperties(this.delegate);

        this.addSlot(new Slot(inventory, 0, 44, 35));
        for (int i = 0; i < 2; ++i)
            for (int l = 0; l < 2; ++l)
                this.addSlot(new TakeOnlySlot(inventory, l + i * 2 + 1, 107 + l * 18, 26 + i * 18));

        for (int i = 0; i < 3; ++i)
            for (int l = 0; l < 9; ++l)
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
        for (int i = 0; i < 9; ++i)
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
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
                if (!this.insertItem(originalStack, 0, 1, false))
                    return ItemStack.EMPTY;
            } else if (!this.insertItem(originalStack, this.inventory.size(), this.inventory.size() + 36, false))
                return ItemStack.EMPTY;
            if (originalStack.isEmpty()) slot.setStack(ItemStack.EMPTY);
            else slot.markDirty();
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

    public double getPercentage() {
        return 1.0 * this.delegate.get(0) / this.delegate.get(1);
    }

    @Override
    public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
        super.onSlotClick(slotIndex, button, actionType, player);
        if (slotIndex >= 0)
            this.slots.get(slotIndex).markDirty();
    }
}
