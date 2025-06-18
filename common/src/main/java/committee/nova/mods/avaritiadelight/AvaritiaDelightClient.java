package committee.nova.mods.avaritiadelight;

import committee.nova.mods.avaritiadelight.registry.ADRenderers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class AvaritiaDelightClient {
    public static void process() {
        ADRenderers.registerBlockEntityRenderers();
        ADRenderers.registerRenderLayers();
        ADRenderers.registerScreenFactories();
    }
}
