package committee.nova.mods.avaritiadelight.compat.emi;

import committee.nova.mods.avaritiadelight.compat.emi.category.ExtremeCookingPotCategory;
import committee.nova.mods.avaritiadelight.recipe.ExtremeCookingPotRecipe;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;

@EmiEntrypoint
public class ADEmiPlugin implements EmiPlugin {
    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(ExtremeCookingPotCategory.CATEGORY);
        registry.addWorkstation(ExtremeCookingPotCategory.CATEGORY, ExtremeCookingPotCategory.WORKSTATION);
        for (ExtremeCookingPotRecipe recipe : registry.getRecipeManager().listAllOfType(ExtremeCookingPotRecipe.Type.INSTANCE))
            registry.addRecipe(new ExtremeCookingPotCategory(recipe));
    }
}
