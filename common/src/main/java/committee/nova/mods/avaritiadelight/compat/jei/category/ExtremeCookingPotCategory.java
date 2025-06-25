package committee.nova.mods.avaritiadelight.compat.jei.category;

import committee.nova.mods.avaritiadelight.AvaritiaDelight;
import committee.nova.mods.avaritiadelight.recipe.ExtremeCookingPotShapelessRecipe;
import committee.nova.mods.avaritiadelight.registry.ADBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExtremeCookingPotCategory implements IRecipeCategory<ExtremeCookingPotShapelessRecipe> {
    public static final RecipeType<ExtremeCookingPotShapelessRecipe> TYPE = new RecipeType<>(ExtremeCookingPotShapelessRecipe.ID, ExtremeCookingPotShapelessRecipe.class);
    private static final Identifier TEXTURE = Identifier.of(AvaritiaDelight.MOD_ID, "textures/gui/jei/extreme_cooking_pot.png");
    private final IDrawable background;
    private final IDrawable icon;

    public ExtremeCookingPotCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 192, 165);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ADBlocks.EXTREME_COOKING_POT.get()));
    }

    @Override
    public @NotNull RecipeType<ExtremeCookingPotShapelessRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public @NotNull Text getTitle() {
        return Text.translatable(ExtremeCookingPotShapelessRecipe.ID.toTranslationKey("jei.category"));
    }

    @SuppressWarnings("removal")
    @Override
    public @NotNull IDrawable getBackground() {
        return this.background;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, ExtremeCookingPotShapelessRecipe recipe, @NotNull IFocusGroup focuses) {
        ClientWorld world = MinecraftClient.getInstance().world;
        assert world != null;
        DefaultedList<Ingredient> inputs = recipe.getIngredients();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int index = j + (i * 9);
                if (index < inputs.size())
                    builder.addSlot(RecipeIngredientRole.INPUT, j * 18 + 2, i * 18 + 2).addIngredients(inputs.get(index));
            }
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 168, 76).addItemStack(recipe.getOutput(world.getRegistryManager()));
        builder.addSlot(RecipeIngredientRole.INPUT, 168, 99).addItemStack(recipe.getOutputContainer());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 168, 137).addItemStack(recipe.getOutput(world.getRegistryManager()));
        builder.moveRecipeTransferButton(170, 100);
    }
}
