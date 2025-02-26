package committee.nova.mods.avaritiadelight.registry;

import committee.nova.mods.avaritiadelight.render.ExtremeStoveBlockEntityRenderer;
import committee.nova.mods.avaritiadelight.screen.gui.ExtremeCookingPotScreen;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;

@Environment(EnvType.CLIENT)
public final class ADRenderers {
    public static void registerBlockEntityRenderers() {
        BlockEntityRendererRegistry.register(ADBlockEntities.EXTREME_STOVE.get(), ExtremeStoveBlockEntityRenderer::new);
    }

    public static void registerRenderLayers() {
        RenderTypeRegistry.register(RenderLayer.getCutout(), ADBlocks.BLAZE_TOMATO.get(), ADBlocks.BUDDING_BLAZE_TOMATO.get(), ADBlocks.CRYSTAL_CABBAGE.get(), ADBlocks.DIAMOND_LATTICE_POTATO.get(), ADBlocks.NEUTRONIUM_WHEAT.get());
    }

    public static void registerScreenFactories() {
        MenuRegistry.registerScreenFactory(ADScreenHandlers.EXTREME_COOKING_POT.get(), ExtremeCookingPotScreen::new);
    }
}
