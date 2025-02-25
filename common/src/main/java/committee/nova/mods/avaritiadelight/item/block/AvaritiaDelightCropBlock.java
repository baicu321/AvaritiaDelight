package committee.nova.mods.avaritiadelight.item.block;

import net.minecraft.block.CropBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.sound.BlockSoundGroup;

import java.util.function.Supplier;

public class AvaritiaDelightCropBlock extends CropBlock {
    private final Supplier<Item> seed;

    public AvaritiaDelightCropBlock() {
        this(null);
    }

    public AvaritiaDelightCropBlock(Supplier<Item> seed) {
        super(Settings.create().nonOpaque().noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.CROP));
        this.seed = seed;
    }

    @Override
    protected ItemConvertible getSeedsItem() {
        return this.seed == null ? this : this.seed.get();
    }
}
