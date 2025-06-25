package committee.nova.mods.avaritiadelight.compat.emi;

import committee.nova.mods.avaritiadelight.compat.emi.category.CropExtractorCategory;
import committee.nova.mods.avaritiadelight.compat.emi.category.ExtremeCookingPotCategory;
import committee.nova.mods.avaritiadelight.recipe.CropExtractorRecipe;
import committee.nova.mods.avaritiadelight.recipe.ExtremeCookingPotShapelessRecipe;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;

@EmiEntrypoint
public class ADEmiPlugin implements EmiPlugin {
    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(ExtremeCookingPotCategory.CATEGORY);
        registry.addWorkstation(ExtremeCookingPotCategory.CATEGORY, ExtremeCookingPotCategory.WORKSTATION);
        for (ExtremeCookingPotShapelessRecipe recipe : registry.getRecipeManager().listAllOfType(ExtremeCookingPotShapelessRecipe.Type.INSTANCE))
            registry.addRecipe(new ExtremeCookingPotCategory(recipe));

        registry.addCategory(CropExtractorCategory.CATEGORY);
        registry.addWorkstation(CropExtractorCategory.CATEGORY, CropExtractorCategory.WORKSTATION);
        for (CropExtractorRecipe recipe : registry.getRecipeManager().listAllOfType(CropExtractorRecipe.Type.INSTANCE))
            registry.addRecipe(new CropExtractorCategory(recipe));
    }
}
