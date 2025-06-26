package committee.nova.mods.avaritiadelight.recipe;

import committee.nova.mods.avaritiadelight.AvaritiaDelight;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;

public interface ExtremeCookingPotRecipe extends Recipe<Inventory> {
    Identifier ID = Identifier.of(AvaritiaDelight.MOD_ID, "extreme_cooking");

    ItemStack getOutputContainer();

    int getCookTime();

    boolean shapeless();

    int width();

    int height();
}
