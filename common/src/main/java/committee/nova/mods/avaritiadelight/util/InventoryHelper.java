package committee.nova.mods.avaritiadelight.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

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
        ItemStack stack = new ItemStack(Registries.ITEM.get(new Identifier(nbt.getString("id"))), nbt.getInt("Count"));
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
}
