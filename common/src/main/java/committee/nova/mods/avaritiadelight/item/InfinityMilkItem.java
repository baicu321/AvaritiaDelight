package committee.nova.mods.avaritiadelight.item;

import committee.nova.mods.avaritiadelight.registry.ADItemGroups;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MilkBucketItem;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.world.World;

public class InfinityMilkItem extends MilkBucketItem {
    public InfinityMilkItem() {
        super(new Settings().arch$tab(ADItemGroups.MAIN));
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof ServerPlayerEntity serverPlayerEntity) {
            Criteria.CONSUME_ITEM.trigger(serverPlayerEntity, stack);
            serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
        }
        if (!world.isClient)
            Registries.STATUS_EFFECT.stream().filter(x -> !x.isBeneficial()).forEach(user::removeStatusEffect);
        return stack;
    }
}
