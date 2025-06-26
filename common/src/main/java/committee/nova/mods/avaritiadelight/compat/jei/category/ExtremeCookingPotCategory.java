package committee.nova.mods.avaritiadelight.compat.jei.category;

import committee.nova.mods.avaritiadelight.AvaritiaDelight;
import committee.nova.mods.avaritiadelight.recipe.ExtremeCookingPotRecipe;
import committee.nova.mods.avaritiadelight.recipe.ExtremeCookingPotShapedRecipe;
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

public class ExtremeCookingPotCategory<T extends ExtremeCookingPotRecipe> implements IRecipeCategory<T> {
    public static final RecipeType<ExtremeCookingPotShapedRecipe> SHAPED_TYPE = new RecipeType<>(ExtremeCookingPotShapedRecipe.ID, ExtremeCookingPotShapedRecipe.class);
    public static final RecipeType<ExtremeCookingPotShapelessRecipe> SHAPELESS_TYPE = new RecipeType<>(ExtremeCookingPotShapelessRecipe.ID, ExtremeCookingPotShapelessRecipe.class);
    private static final Identifier TEXTURE = Identifier.of(AvaritiaDelight.MOD_ID, "textures/gui/jei/extreme_cooking_pot.png");
    private final IDrawable background;
    private final IDrawable icon;
    private final RecipeType<T> type;

    public ExtremeCookingPotCategory(IGuiHelper helper, RecipeType<T> type) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 192, 165);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ADBlocks.EXTREME_COOKING_POT.get()));
        this.type = type;
    }

    @Override
    public @NotNull RecipeType<T> getRecipeType() {
        return this.type;
    }

    @Override
    public @NotNull Text getTitle() {
        return Text.translatable(ExtremeCookingPotRecipe.ID.toTranslationKey("jei.category"));
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
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, ExtremeCookingPotRecipe recipe, @NotNull IFocusGroup focuses) {
        ClientWorld world = MinecraftClient.getInstance().world;
        assert world != null;
        DefaultedList<Ingredient> inputs = recipe.getIngredients();
        int surroundWidth = (9 - recipe.width()) / 2, surroundHeight = (9 - recipe.height()) / 2;
        for (int i = 0; i < recipe.height(); i++) {
            for (int j = 0; j < recipe.width(); j++) {
                int index = j + (i * recipe.width());
                if (index < inputs.size())
                    builder.addSlot(RecipeIngredientRole.INPUT, (j + surroundWidth) * 18 + 2, (i + surroundHeight) * 18 + 2).addIngredients(inputs.get(index));
            }
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 168, 76).addItemStack(recipe.getOutput(world.getRegistryManager()));
        builder.addSlot(RecipeIngredientRole.INPUT, 168, 99).addItemStack(recipe.getOutputContainer());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 168, 137).addItemStack(recipe.getOutput(world.getRegistryManager()));
        builder.moveRecipeTransferButton(170, 30);
        if (recipe.shapeless()) builder.setShapeless();
    }
}
