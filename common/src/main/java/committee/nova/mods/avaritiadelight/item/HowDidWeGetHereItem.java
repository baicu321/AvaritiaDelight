package committee.nova.mods.avaritiadelight.item;

import committee.nova.mods.avaritiadelight.registry.ADItemGroups;
import committee.nova.mods.avaritiadelight.util.EffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.UseAction;

import java.util.List;

public class HowDidWeGetHereItem extends Item {
    public HowDidWeGetHereItem() {
        super(EffectUtil.applyEffects(new Settings().arch$tab(ADItemGroups.MAIN), List.of(
                StatusEffects.ABSORPTION,
                StatusEffects.BAD_OMEN,
                StatusEffects.BLINDNESS,
                StatusEffects.CONDUIT_POWER,
                StatusEffects.DARKNESS,
                StatusEffects.DOLPHINS_GRACE,
                StatusEffects.FIRE_RESISTANCE,
                StatusEffects.GLOWING,
                StatusEffects.HASTE,
                StatusEffects.HERO_OF_THE_VILLAGE,
                StatusEffects.HUNGER,
                StatusEffects.INVISIBILITY,
                StatusEffects.JUMP_BOOST,
                StatusEffects.LEVITATION,
                StatusEffects.MINING_FATIGUE,
                StatusEffects.NAUSEA,
                StatusEffects.NIGHT_VISION,
                StatusEffects.POISON,
                StatusEffects.REGENERATION,
                StatusEffects.RESISTANCE,
                StatusEffects.SLOW_FALLING,
                StatusEffects.SLOWNESS,
                StatusEffects.SPEED,
                StatusEffects.STRENGTH,
                StatusEffects.WATER_BREATHING,
                StatusEffects.WEAKNESS,
                StatusEffects.WITHER
        ), Integer.MAX_VALUE));
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }
}
