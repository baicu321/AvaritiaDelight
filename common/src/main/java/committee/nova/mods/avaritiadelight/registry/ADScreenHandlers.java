package committee.nova.mods.avaritiadelight.registry;

import committee.nova.mods.avaritiadelight.AvaritiaDelight;
import committee.nova.mods.avaritiadelight.screen.handler.ExtremeCookingPotScreenHandler;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;

import java.util.function.Supplier;

public final class ADScreenHandlers {
    public static final DeferredRegister<ScreenHandlerType<?>> REGISTRY = DeferredRegister.create(AvaritiaDelight.MOD_ID, RegistryKeys.SCREEN_HANDLER);

    public static final RegistrySupplier<ScreenHandlerType<ExtremeCookingPotScreenHandler>> EXTREME_COOKING_POT = register("extreme_cooking_pot", () -> MenuRegistry.ofExtended(ExtremeCookingPotScreenHandler::new));

    public static <T extends ScreenHandler> RegistrySupplier<ScreenHandlerType<T>> register(String id, Supplier<ScreenHandlerType<T>> supplier) {
        return REGISTRY.register(id, supplier);
    }
}
