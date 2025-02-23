package committee.nova.mods.avaritiadelight.fabric;

import committee.nova.mods.avaritiadelight.AvaritiaDelight;
import net.fabricmc.api.ModInitializer;

public final class AvaritiaDelightFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        AvaritiaDelight.init();
        AvaritiaDelight.process();
    }
}
