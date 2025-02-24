package committee.nova.mods.avaritiadelight;

import committee.nova.mods.avaritiadelight.registry.ADBlocks;
import committee.nova.mods.avaritiadelight.registry.ADItemGroups;
import committee.nova.mods.avaritiadelight.registry.ADItems;

public final class AvaritiaDelight {
    public static final String MOD_ID = "avaritia_delight";

    public static void init() {
        ADBlocks.REGISTRY.register();
        ADItems.REGISTRY.register();
        ADItemGroups.REGISTRY.register();
    }

    public static void process() {
    }
}
