package committee.nova.mods.avaritiadelight.effect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public class EndestEffect extends StatusEffect {
    // 吸引范围（半径10格）
    private static final double RANGE = 15.0D;
    // 处死距离
    private static final double KILL_DISTANCE = 1.5D;

    public EndestEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }
    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (!(entity instanceof PlayerEntity player)) return;

        // 1. 搜索范围内的生物（只吸引敌对生物，排除玩家自身和友方）
        player.getWorld().getOtherEntities(player,
                player.getBoundingBox().expand(RANGE),
                this::isValidTarget  // 过滤目标
        ).forEach(target -> {
            // 2. 吸引生物向玩家移动
            pullEntityToPlayer(target, player, amplifier);

            // 3. 若生物靠近到处死距离，执行杀死逻辑
            if (target.distanceTo(player) <= KILL_DISTANCE) {
                killEntity(target, player);
            }
        });
    }
    private boolean isValidTarget(Entity entity) {
        // 只吸引活体生物，排除玩家和非敌对生物（如村民、动物）
        return entity instanceof LivingEntity
                && !(entity instanceof PlayerEntity)
                && entity.isAlive()
                && (entity instanceof HostileEntity); // 只吸引敌对生物
    }
    private void pullEntityToPlayer(Entity target, PlayerEntity player, int amplifier) {
        // 计算目标到玩家的方向向量
        Vec3d playerPos = player.getPos();
        Vec3d targetPos = target.getPos();
        Vec3d direction = playerPos.subtract(targetPos).normalize(); // 从目标指向玩家的单位向量

        // 计算拉力（等级越高，拉力越强）
        double pullStrength = 0.3D + (amplifier * 0.15D); // 基础0.1，每级+0.05
        Vec3d velocity = direction.multiply(pullStrength); // 最终移动向量

        // 应用拉力（直接修改实体速度，实现平滑移动）
        target.setVelocity(velocity);
        target.velocityModified = true; // 标记速度已修改，防止被其他逻辑覆盖
    }
    private void killEntity(Entity target, PlayerEntity player) {
        if (target instanceof LivingEntity living) {
            living.damage(living.getDamageSources().magic(), Float.MAX_VALUE);
            // 粒子效果
            // spawnBlackHoleParticles(living.getWorld(), living.getPos());
        }
    }
}
