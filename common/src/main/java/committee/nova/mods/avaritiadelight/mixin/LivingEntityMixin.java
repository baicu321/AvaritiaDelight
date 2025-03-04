package committee.nova.mods.avaritiadelight.mixin;

import committee.nova.mods.avaritia.Static;
import committee.nova.mods.avaritiadelight.registry.ADItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @SuppressWarnings("all")
    @Inject(method = "eatFood", at = @At("RETURN"))
    private void onFinishUsingUntimateStew(World world, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        if (stack.isOf(Registries.ITEM.get(Identifier.of(Static.MOD_ID, "ultimate_stew"))) && (Object) this instanceof PlayerEntity player && !player.getAbilities().creativeMode)
            player.getInventory().offerOrDrop(new ItemStack(ADItems.NEUTRONIUM_POT.get()));
    }
}
