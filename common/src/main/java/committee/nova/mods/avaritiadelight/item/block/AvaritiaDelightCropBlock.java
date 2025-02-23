package committee.nova.mods.avaritiadelight.item.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemConvertible;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class AvaritiaDelightCropBlock extends CropBlock {
    private final boolean fullBlock;

    public AvaritiaDelightCropBlock(boolean fullBlock) {
        super(Settings.create().nonOpaque().noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.CROP));
        this.fullBlock = fullBlock;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (this.fullBlock) return VoxelShapes.fullCube();
        return super.getOutlineShape(state, world, pos, context);
    }

    @Override
    protected ItemConvertible getSeedsItem() {
        return this;
    }
}
