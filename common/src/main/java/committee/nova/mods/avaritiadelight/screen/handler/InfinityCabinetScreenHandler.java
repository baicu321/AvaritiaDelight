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
        this(syncId, new SimpleInventory(243) {
            @Override
            public int getMaxCountPerStack() {
                return Integer.MAX_VALUE;
            }
        }, playerInventory);
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
            } else if (!this.insertItem(originalStack, this.inventory.size(), this.inventory.size() + 36, false))
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

    @Override
    protected boolean insertItem(ItemStack stack, int startIndex, int endIndex, boolean fromLast) {
        boolean bl = false;
        int i = startIndex;
        if (fromLast)
            i = endIndex - 1;
        Slot slot;
        ItemStack itemStack;
        if (stack.isStackable()) {
            while (!stack.isEmpty()) {
                if (fromLast) {
                    if (i < startIndex) break;
                } else if (i >= endIndex) break;
                slot = this.slots.get(i);
                itemStack = slot.getStack();
                if (!itemStack.isEmpty() && ItemStack.canCombine(stack, itemStack)) {
                    int maxCount = i >= this.inventory.size() ? 64 : Integer.MAX_VALUE;
                    long j = (long) itemStack.getCount() + stack.getCount();//Use long to prevent overflow
                    if (j <= maxCount) {
                        stack.setCount(0);
                        itemStack.setCount((int) j);
                        slot.markDirty();
                        bl = true;
                    } else if (itemStack.getCount() < maxCount) {
                        stack.decrement(maxCount - itemStack.getCount());
                        itemStack.setCount(maxCount);
                        slot.markDirty();
                        bl = true;
                    }
                }
                if (fromLast) --i;
                else ++i;
            }
        }

        if (!stack.isEmpty()) {
            if (fromLast) i = endIndex - 1;
            else i = startIndex;
            while (true) {
                if (fromLast) {
                    if (i < startIndex) break;
                } else if (i >= endIndex) break;
                slot = this.slots.get(i);
                itemStack = slot.getStack();
                if (itemStack.isEmpty() && slot.canInsert(stack)) {
                    if (stack.getCount() > slot.getMaxItemCount())
                        slot.setStack(stack.split(slot.getMaxItemCount()));
                    else
                        slot.setStack(stack.split(stack.getCount()));

                    slot.markDirty();
                    bl = true;
                    break;
                }
                if (fromLast) --i;
                else ++i;
            }
        }
        return bl;
    }
}
