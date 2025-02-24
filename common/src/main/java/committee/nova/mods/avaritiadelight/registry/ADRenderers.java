package committee.nova.mods.avaritiadelight.registry;

import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;

@Environment(EnvType.CLIENT)
public class ADRenderers {
    public static void registerRenderLayers() {
        RenderTypeRegistry.register(RenderLayer.getCutout(), ADBlocks.BLAZE_TOMATO.get(), ADBlocks.BUDDING_BLAZE_TOMATO.get(), ADBlocks.CRYSTAL_CABBAGE.get(), ADBlocks.DIAMOND_LATTICE_POTATO.get(), ADBlocks.NEUTRONIUM_WHEAT.get());
    }
}
