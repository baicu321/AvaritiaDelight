package committee.nova.mods.avaritiadelight.compat.jei;

import committee.nova.mods.avaritiadelight.AvaritiaDelight;
import committee.nova.mods.avaritiadelight.compat.jei.category.ExtremeCookingPotCategory;
import committee.nova.mods.avaritiadelight.recipe.ExtremeCookingPotRecipe;
import committee.nova.mods.avaritiadelight.registry.ADBlocks;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
public class ADJeiPlugin implements IModPlugin {
    @Override
    public @NotNull Identifier getPluginUid() {
        return new Identifier(AvaritiaDelight.MOD_ID, AvaritiaDelight.MOD_ID);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper helper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new ExtremeCookingPotCategory(helper));
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        ClientWorld world = MinecraftClient.getInstance().world;
        assert world != null;
        RecipeManager manager = world.getRecipeManager();
        registration.addRecipes(ExtremeCookingPotCategory.TYPE, manager.listAllOfType(ExtremeCookingPotRecipe.Type.INSTANCE));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ADBlocks.EXTREME_COOKING_POT.get()), ExtremeCookingPotCategory.TYPE);
    }
}
