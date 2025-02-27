package committee.nova.mods.avaritiadelight.screen.handler;

import committee.nova.mods.avaritiadelight.registry.ADScreenHandlers;
import committee.nova.mods.avaritiadelight.screen.slot.InfinitySlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class InfinityCabinetScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final PlayerInventory playerInventory;

    public InfinityCabinetScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, new SimpleInventory(243), playerInventory);
    }

    public InfinityCabinetScreenHandler(int syncId, Inventory inventory, PlayerInventory playerInventory) {
        super(ADScreenHandlers.INFINITY_CABINET.get(), syncId);
        checkSize(inventory, 243);
        this.inventory = inventory;
        this.inventory.onOpen(playerInventory.player);
        this.playerInventory = playerInventory;

        for (int i = 0; i < 9; ++i)
            for (int l = 0; l < 27; ++l)
                this.addSlot(new InfinitySlot(inventory, l + i * 27, 8 + l * 18, 18 + i * 18));
        for (int i = 0; i < 3; ++i)
            for (int l = 0; l < 9; ++l)
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 170 + l * 18, 194 + i * 18));
        for (int i = 0; i < 9; ++i)
            this.addSlot(new Slot(playerInventory, i, 170 + i * 18, 252));
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
                if (!this.insertItem(originalStack, 0, this.inventory.size(), false))
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

    public Inventory getInventory() {
        return this.inventory;
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
}
