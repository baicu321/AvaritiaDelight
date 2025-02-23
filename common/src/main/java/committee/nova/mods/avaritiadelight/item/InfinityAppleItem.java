package committee.nova.mods.avaritiadelight.item;

import committee.nova.mods.avaritiadelight.registry.ADItemGroups;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;

public class InfinityAppleItem extends Item {
    private static final int DURATION = 20 * 60 * 60;//1h

    public InfinityAppleItem() {
        super(new Item.Settings().arch$tab(ADItemGroups.MAIN).food(new FoodComponent.Builder()
                .hunger(4).saturationModifier(0.3F)
                .statusEffect(new StatusEffectInstance(StatusEffects.SATURATION, DURATION), 1)
                .statusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, DURATION, 4), 1)
                .statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, DURATION, 4), 1)
                .statusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, DURATION, 4), 1)
                .statusEffect(new StatusEffectInstance(StatusEffects.SPEED, DURATION, 1), 1)
                .build()));
    }
}
