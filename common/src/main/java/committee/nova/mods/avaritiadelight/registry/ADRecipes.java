package committee.nova.mods.avaritiadelight.registry;

import committee.nova.mods.avaritiadelight.AvaritiaDelight;
import committee.nova.mods.avaritiadelight.recipe.CropExtractorRecipe;
import committee.nova.mods.avaritiadelight.recipe.ExtremeCookingPotShapedRecipe;
import committee.nova.mods.avaritiadelight.recipe.ExtremeCookingPotShapelessRecipe;
import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.RegistryKeys;

public final class ADRecipes {
    public static final DeferredRegister<RecipeType<?>> TYPE_REGISTRY = DeferredRegister.create(AvaritiaDelight.MOD_ID, RegistryKeys.RECIPE_TYPE);
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZER_REGISTRY = DeferredRegister.create(AvaritiaDelight.MOD_ID, RegistryKeys.RECIPE_SERIALIZER);

    static {
        TYPE_REGISTRY.register(ExtremeCookingPotShapedRecipe.ID, () -> ExtremeCookingPotShapedRecipe.Type.INSTANCE);
        SERIALIZER_REGISTRY.register(ExtremeCookingPotShapedRecipe.ID, () -> ExtremeCookingPotShapedRecipe.Serializer.INSTANCE);

        TYPE_REGISTRY.register(ExtremeCookingPotShapelessRecipe.ID, () -> ExtremeCookingPotShapelessRecipe.Type.INSTANCE);
        SERIALIZER_REGISTRY.register(ExtremeCookingPotShapelessRecipe.ID, () -> ExtremeCookingPotShapelessRecipe.Serializer.INSTANCE);

        TYPE_REGISTRY.register(CropExtractorRecipe.ID, () -> CropExtractorRecipe.Type.INSTANCE);
        SERIALIZER_REGISTRY.register(CropExtractorRecipe.ID, () -> CropExtractorRecipe.Serializer.INSTANCE);
    }
}
