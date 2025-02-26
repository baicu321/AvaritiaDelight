package committee.nova.mods.avaritiadelight.util;

import committee.nova.mods.avaritiadelight.mixin.BlockEntityTypeAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BlockEntityUtil {
    public static void appendBlockToType(BlockEntityType<?> type, Block... append) {
        BlockEntityTypeAccessor accessor = (BlockEntityTypeAccessor) type;
        Set<Block> set = new HashSet<>(accessor.getBlocks());
        set.addAll(List.of(append));
        accessor.setBlocks(set);
    }

    public static int calcRedstoneFromInventory(@Nullable Inventory inventory) {
        if (inventory == null) return 0;
        else {
            int i = 0;
            float f = 0.0F;

            for (int j = 0; j < inventory.size(); ++j) {
                ItemStack itemstack = inventory.getStack(j);
                if (!itemstack.isEmpty()) {
                    f += (float) itemstack.getCount() / (float) Math.min(inventory.getStack(j).getMaxCount(), itemstack.getMaxCount());
                    ++i;
                }
            }

            f /= (float) inventory.size();
            return MathHelper.floor(f * 14.0F) + (i > 0 ? 1 : 0);
        }
    }
}
