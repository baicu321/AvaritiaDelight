package committee.nova.mods.avaritiadelight.registry;

import committee.nova.mods.avaritiadelight.AvaritiaDelight;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;

import java.util.function.Supplier;

public final class ADItemGroups {
    public static final DeferredRegister<ItemGroup> REGISTRY = DeferredRegister.create(AvaritiaDelight.MOD_ID, RegistryKeys.ITEM_GROUP);

    public static final RegistrySupplier<ItemGroup> MAIN = register("main", () -> CreativeTabRegistry.create(Text.translatable("itemGroup.%s.main".formatted(AvaritiaDelight.MOD_ID)), () -> new ItemStack(ADItems.INFINITY_APPLE.get())));

    public static RegistrySupplier<ItemGroup> register(String id, Supplier<ItemGroup> supplier) {
        return REGISTRY.register(id, supplier);
    }
}
