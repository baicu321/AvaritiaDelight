package committee.nova.mods.avaritiadelight.util;

import committee.nova.mods.avaritiadelight.mixin.BlockEntityTypeAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;

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
}
