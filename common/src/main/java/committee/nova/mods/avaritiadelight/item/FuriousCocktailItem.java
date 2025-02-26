package committee.nova.mods.avaritiadelight.item;

import committee.nova.mods.avaritiadelight.registry.ADItemGroups;
import committee.nova.mods.avaritiadelight.util.EffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.UseAction;

import java.util.List;

public class FuriousCocktailItem extends Item {
    public FuriousCocktailItem() {
        super(EffectUtil.applyEffects(new Settings().arch$tab(ADItemGroups.MAIN), List.of(
                StatusEffects.FIRE_RESISTANCE,
                StatusEffects.INVISIBILITY,
                StatusEffects.JUMP_BOOST,
                StatusEffects.NIGHT_VISION,
                StatusEffects.POISON,
                StatusEffects.REGENERATION,
                StatusEffects.RESISTANCE,
                StatusEffects.SLOW_FALLING,
                StatusEffects.SLOWNESS,
                StatusEffects.SPEED,
                StatusEffects.STRENGTH,
                StatusEffects.WATER_BREATHING,
                StatusEffects.WEAKNESS
        ), Integer.MAX_VALUE));
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }
}
