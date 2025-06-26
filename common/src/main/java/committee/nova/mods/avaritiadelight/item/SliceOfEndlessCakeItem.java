package committee.nova.mods.avaritiadelight.item;

import committee.nova.mods.avaritiadelight.registry.ADItemGroups;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class SliceOfEndlessCakeItem extends Item {
    public SliceOfEndlessCakeItem() {
        super(new Item.Settings().arch$tab(ADItemGroups.MAIN));
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (entity instanceof PlayerEntity player && world.getTime() % 10 == 0) {
            HungerManager hungerManager = player.getHungerManager();
            hungerManager.addExhaustion(1);
            hungerManager.setSaturationLevel(hungerManager.getSaturationLevel() + 1);
        }
    }
}
