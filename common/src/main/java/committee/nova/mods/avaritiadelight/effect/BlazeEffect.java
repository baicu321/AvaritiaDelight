package committee.nova.mods.avaritiadelight.effect;

import committee.nova.mods.avaritiadelight.registry.ADEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;

public class BlazeEffect extends StatusEffect {
    public BlazeEffect(StatusEffectCategory category, int color) {
        super(category,color);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (!(entity instanceof PlayerEntity player)) return;

        //着火效果每刻执行
        LivingEntity attackedTarget = player.getAttacking();
        if (attackedTarget != null) {
            onAttack(player, attackedTarget, amplifier);
        }

        //掉落凋零骷髅头20或19刻检查一次,否则会掉19个头
        if (player.getWorld().getTime() % 19 == 0) {
            checkDeadSkeletons(player);
        }
    }

    private void checkDeadSkeletons(PlayerEntity player) {
        World world = player.getWorld();
        if (world.isClient) return; // 服务器端执行

        // 搜索玩家周围12格内的实体
        world.getEntitiesByClass(SkeletonEntity.class,
                player.getBoundingBox().expand(12), // 搜索范围,不知道会不会影响性能
                skeleton -> skeleton.isDead() && // 已死亡
                        skeleton.getLastAttacker() != null && // 有伤害来源
                        skeleton.getAttacker() == player // 被玩家击杀
        ).forEach(skeleton -> {
            // 防止重复掉落（用标签标记）(666这个是ai写的,我也不知道为什么)
            if (!skeleton.hasCustomName()) {
                onKillSkeleton(player, skeleton);
                skeleton.setCustomNameVisible(false);
                skeleton.setCustomName(null);
            }
        });
    }

    public static void onAttack(PlayerEntity player, LivingEntity target ,int amplifier){
        if (player.hasStatusEffect(ADEffects.BLAZE_EFFECT.get())){
            target.setOnFireFor(3 * 20 * (amplifier + 1));
        }
    }

    public static void onKillSkeleton(PlayerEntity player, SkeletonEntity skeleton){
        if (player.hasStatusEffect(ADEffects.BLAZE_EFFECT.get())){
            World world = skeleton.getWorld();
            if (!world.isClient) {
                skeleton.dropStack(new ItemStack(Items.WITHER_SKELETON_SKULL,1));
            }
        }
    }
}
