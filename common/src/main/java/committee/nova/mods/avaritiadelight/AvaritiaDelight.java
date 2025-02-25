package committee.nova.mods.avaritiadelight;

import com.mojang.logging.LogUtils;
import committee.nova.mods.avaritiadelight.registry.*;
import org.slf4j.Logger;

public final class AvaritiaDelight {
    public static final String MOD_ID = "avaritia_delight";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static void init() {
        ADBlocks.REGISTRY.register();
        ADBlockEntities.REGISTRY.register();
        ADItems.REGISTRY.register();
        ADItemGroups.REGISTRY.register();
        ADRecipes.TYPE_REGISTRY.register();
        ADRecipes.SERIALIZER_REGISTRY.register();
    }

    public static void process() {
    }
}
