package committee.nova.mods.avaritiadelight.forge;

import committee.nova.mods.avaritiadelight.AvaritiaDelight;
import committee.nova.mods.avaritiadelight.block.entity.StarCampFireBlockEntity;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(AvaritiaDelight.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class AvaritiaDelightForge {
    @SuppressWarnings("removal")
    public AvaritiaDelightForge() {
        EventBuses.registerModEventBus(AvaritiaDelight.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        AvaritiaDelight.init();
    }

    @SubscribeEvent
    public static void onInit(FMLCommonSetupEvent event) {
        event.enqueueWork(AvaritiaDelight::process);
    }

}
