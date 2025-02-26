package committee.nova.mods.avaritiadelight.util;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;

import java.util.List;

public class EffectUtil {
    public static Item.Settings applyEffects(Item.Settings settings, List<StatusEffect> effects, int duration) {
        FoodComponent.Builder builder = new FoodComponent.Builder();
        for (StatusEffect effect : effects)
            builder.statusEffect(new StatusEffectInstance(effect, duration), 1);
        return settings.food(builder.build());
    }
}
