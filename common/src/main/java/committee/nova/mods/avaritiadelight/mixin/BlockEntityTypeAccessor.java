package committee.nova.mods.avaritiadelight.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(BlockEntityType.class)
public interface BlockEntityTypeAccessor {
    @Accessor("blocks")
    @Mutable
    Set<Block> getBlocks();

    @Accessor("blocks")
    @Mutable
    void setBlocks(Set<Block> blocks);
}
