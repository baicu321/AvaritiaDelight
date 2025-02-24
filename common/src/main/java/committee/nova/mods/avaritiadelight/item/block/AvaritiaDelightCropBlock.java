package committee.nova.mods.avaritiadelight.item.block;

import net.minecraft.block.CropBlock;
import net.minecraft.item.ItemConvertible;
import net.minecraft.sound.BlockSoundGroup;

public class AvaritiaDelightCropBlock extends CropBlock {
    public AvaritiaDelightCropBlock() {
        super(Settings.create().nonOpaque().noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.CROP));
    }

    @Override
    protected ItemConvertible getSeedsItem() {
        return this;
    }
}
