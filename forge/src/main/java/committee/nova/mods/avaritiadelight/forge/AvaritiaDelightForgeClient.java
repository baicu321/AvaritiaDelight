package committee.nova.mods.avaritiadelight.forge;

import committee.nova.mods.avaritiadelight.AvaritiaDelightClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class AvaritiaDelightForgeClient {
    @SubscribeEvent
    public static void onInit(FMLClientSetupEvent event) {
        event.enqueueWork(AvaritiaDelightClient::process);
    }
}
