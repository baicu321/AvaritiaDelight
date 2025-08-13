package committee.nova.mods.avaritiadelight.render;

import committee.nova.mods.avaritiadelight.item.block.StarCampFireBlock;
import committee.nova.mods.avaritiadelight.item.block.entity.StarCampFireBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
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


@Environment(EnvType.CLIENT)
public class StarCampFireRenderer implements BlockEntityRenderer<StarCampFireBlockEntity> {
    private static final float SCALE = 0.375F;

    private static final Vec2f[] OFFSETS = new Vec2f[]{
            new Vec2f(-0.25F, -0.25F),
            new Vec2f(0.25F, -0.25F),
            new Vec2f(0.25F, 0.25F),
            new Vec2f(-0.25F, 0.25F)
    };
    private final ItemRenderer itemRenderer;

    public StarCampFireRenderer (BlockEntityRendererFactory.Context ctx) {
        this.itemRenderer = ctx.getItemRenderer();
    }

    public void render(StarCampFireBlockEntity campfireBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
        Direction direction = campfireBlockEntity.getCachedState().get(StarCampFireBlock.FACING);
        DefaultedList<ItemStack> defaultedList = campfireBlockEntity.getItemsBeingCooked();
        long k = campfireBlockEntity.getPos().asLong();

        for(int l = 0; l < defaultedList.size(); ++l) {
            ItemStack itemStack = defaultedList.get(l);
            if (!itemStack.isEmpty()) {
                matrixStack.push();

                matrixStack.translate(0.5F, 0.44921875F, 0.5F);

                Direction direction2 = Direction.fromHorizontal((l + direction.getHorizontal()) % 4);
                float g = -direction2.asRotation();
                matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(g));
                matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));

                Vec2f offset = OFFSETS[l % OFFSETS.length];
                matrixStack.translate(offset.x, offset.y, 0.0F);
                matrixStack.scale(SCALE, SCALE, SCALE);
                // 渲染物品
                this.itemRenderer.renderItem(
                        itemStack,
                        ModelTransformationMode.FIXED,
                        i,
                        j,
                        matrixStack,
                        vertexConsumerProvider,
                        campfireBlockEntity.getWorld(),
                        (int)(k + l)
                );
                matrixStack.pop();
            }
        }
    }
}
