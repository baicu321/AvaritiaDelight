package committee.nova.mods.avaritiadelight.item.block;

import net.minecraft.block.MapColor;
import net.minecraft.sound.BlockSoundGroup;
import vectorwing.farmersdelight.common.block.CookingPotBlock;

public class ExtremeCookingPotBlock extends CookingPotBlock {
    public ExtremeCookingPotBlock() {
        super(Settings.create().mapColor(MapColor.IRON_GRAY).strength(0.5F, 6.0F).sounds(BlockSoundGroup.LANTERN));
    }
}
