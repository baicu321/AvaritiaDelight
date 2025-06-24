package committee.nova.mods.avaritiadelight.util;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

import java.util.List;

public class InventoryHelper {
    public static NbtCompound writeNbt(NbtCompound nbt, DefaultedList<ItemStack> stacks) {
        return writeNbt(nbt, stacks, true);
    }

    public static NbtCompound writeNbt(NbtCompound nbt, DefaultedList<ItemStack> stacks, boolean setIfEmpty) {
        NbtList nbtList = new NbtList();
        for (int i = 0; i < stacks.size(); ++i) {
            ItemStack itemStack = stacks.get(i);
            if (!itemStack.isEmpty()) {
                NbtCompound nbtCompound = new NbtCompound();
                nbtCompound.putByte("Slot", (byte) i);
                writeStackNbt(itemStack, nbtCompound);
                nbtList.add(nbtCompound);
            }
        }
        if (!nbtList.isEmpty() || setIfEmpty) nbt.put("Items", nbtList);
        return nbt;
    }

    public static void readNbt(NbtCompound nbt, DefaultedList<ItemStack> stacks) {
        NbtList nbtList = nbt.getList("Items", 10);
        for (int i = 0; i < nbtList.size(); ++i) {
            NbtCompound nbtCompound = nbtList.getCompound(i);
            int j = nbtCompound.getByte("Slot") & 255;
            if (j < stacks.size()) stacks.set(j, readStackNbt(nbtCompound));
        }
    }

    private static ItemStack readStackNbt(NbtCompound nbt) {
        ItemStack stack = new ItemStack(Registries.ITEM.get(Identifier.tryParse(nbt.getString("id"))), nbt.getInt("Count"));
        if (nbt.contains("tag", 10)) {
            stack.setNbt(nbt.getCompound("tag"));
            stack.getItem().postProcessNbt(stack.getNbt());
        }
        return stack;
    }

    private static void writeStackNbt(ItemStack stack, NbtCompound nbt) {
        Identifier identifier = Registries.ITEM.getId(stack.getItem());
        nbt.putString("id", identifier.toString());
        nbt.putInt("Count", stack.getCount());
        if (stack.getNbt() != null)
            nbt.put("tag", stack.getNbt().copy());
    }

    public static boolean canInsertItems(int start, Inventory inventory, List<ItemStack> insert) {
        return insertItems(start, copy(inventory), insert);
    }

    public static boolean insertItems(int start, Inventory inventory, List<ItemStack> insert) {
        for (ItemStack insertStack : insert)
            if (insertStack != null && !tryAddItemToInventory(start, inventory, insertStack.copy()))
                return false;
        return true;
    }

    private static boolean tryAddItemToInventory(int start, Inventory inventory, ItemStack stack) {
        if (stack.isEmpty()) return true;
        for (int i = start; i < inventory.size(); i++) {
            ItemStack inventoryStack = inventory.getStack(i);
            if (inventoryStack == null || ItemStack.canCombine(inventoryStack, stack)) {
                if (stack.getMaxCount() - (inventoryStack != null ? inventoryStack.getCount() : 0) > 0) {
                    int countToAdd = Math.min(stack.getCount(), stack.getMaxCount() - (inventoryStack != null ? inventoryStack.getCount() : 0));
                    if (inventoryStack == null) inventory.setStack(i, stack.copy());
                    else inventoryStack.increment(countToAdd);
                    stack.decrement(countToAdd);
                    if (stack.getCount() == 0) return true;
                }
            }
        }
        if (inventory instanceof PlayerInventory playerInventory) {
            playerInventory.offerOrDrop(stack);
            return true;
        } else for (int i = 0; i < inventory.size(); i++)
            if (inventory.getStack(i).isEmpty()) {
                inventory.setStack(i, stack);
                return true;
            }
        return false;
    }

    public static Inventory copy(Inventory another) {
        Inventory inventory = new SimpleInventory(another.size());
        for (int i = 0; i < another.size(); i++)
            inventory.setStack(i, another.getStack(i).copy());
        return inventory;
    }
}
