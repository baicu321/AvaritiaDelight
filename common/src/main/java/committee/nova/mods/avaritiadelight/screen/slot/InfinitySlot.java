package committee.nova.mods.avaritiadelight.screen.slot;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class InfinitySlot extends Slot {
    public InfinitySlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public int getMaxItemCount(ItemStack stack) {
        return this.getMaxItemCount();
    }

    @Override
    public int getMaxItemCount() {
        return Integer.MAX_VALUE;
    }
}
