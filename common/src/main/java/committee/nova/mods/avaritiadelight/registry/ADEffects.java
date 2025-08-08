package committee.nova.mods.avaritiadelight.registry;

import committee.nova.mods.avaritiadelight.AvaritiaDelight;
import committee.nova.mods.avaritiadelight.effect.BlazeEffect;
import committee.nova.mods.avaritiadelight.effect.EndestEffect;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.RegistryKeys;

import java.util.function.Supplier;


public class ADEffects {
    public static final DeferredRegister<StatusEffect> REGISTRY = DeferredRegister.create(AvaritiaDelight.MOD_ID, RegistryKeys.STATUS_EFFECT);


    public static final RegistrySupplier<StatusEffect> BLAZE_EFFECT = register("blaze_effect", () -> new BlazeEffect(StatusEffectCategory.BENEFICIAL,16731392));
    public static final RegistrySupplier<StatusEffect> ENDEST_EFFECT = register("endest_effect", () -> new EndestEffect(StatusEffectCategory.BENEFICIAL,0));


    public static <T extends StatusEffect> RegistrySupplier<T> register(String id, Supplier<T> effect) {
        return REGISTRY.register(id, effect);
    }
}
