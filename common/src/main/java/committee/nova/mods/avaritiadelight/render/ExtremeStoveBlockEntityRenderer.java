package committee.nova.mods.avaritiadelight.render;

import committee.nova.mods.avaritiadelight.item.block.entity.ExtremeStoveBlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec2f;
import vectorwing.farmersdelight.common.block.StoveBlock;

public class ExtremeStoveBlockEntityRenderer implements BlockEntityRenderer<ExtremeStoveBlockEntity> {
    private final ItemRenderer itemRenderer;

    public ExtremeStoveBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.itemRenderer = ctx.getItemRenderer();
    }

    @Override
    public void render(ExtremeStoveBlockEntity stoveEntity, float partialTicks, MatrixStack poseStack, VertexConsumerProvider buffer, int combinedLightIn, int combinedOverlayIn) {
        Direction direction = stoveEntity.getCachedState().get(StoveBlock.FACING).getOpposite();
        DefaultedList<ItemStack> stacks = stoveEntity.getInventory();
        int posLong = (int) stoveEntity.getPos().asLong();

        for (int i = 0; i < stacks.size(); ++i) {
            ItemStack stoveStack = stacks.get(i);
            if (!stoveStack.isEmpty()) {
                poseStack.push();
                poseStack.translate(0.5, 1.02, 0.5);
                float f = -direction.asRotation();
                poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(f));
                poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));
                Vec2f itemOffset = stoveEntity.getStoveItemOffset(i);
                poseStack.translate(itemOffset.x, itemOffset.y, 0.0);
                poseStack.scale(0.375F, 0.375F, 0.375F);
                if (stoveEntity.getWorld() != null)
                    this.itemRenderer.renderItem(stoveStack, ModelTransformationMode.FIXED, WorldRenderer.getLightmapCoordinates(stoveEntity.getWorld(), stoveEntity.getPos().up()), combinedOverlayIn, poseStack, buffer, stoveEntity.getWorld(), posLong + i);
                poseStack.pop();
            }
        }

    }
}
