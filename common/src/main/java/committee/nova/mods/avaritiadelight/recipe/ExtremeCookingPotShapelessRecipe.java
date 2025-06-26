package committee.nova.mods.avaritiadelight.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import committee.nova.mods.avaritiadelight.AvaritiaDelight;
import committee.nova.mods.avaritiadelight.registry.ADBlocks;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class ExtremeCookingPotShapelessRecipe implements ExtremeCookingPotRecipe {
    public static final Identifier ID = Identifier.of(AvaritiaDelight.MOD_ID, "extreme_cooking_shapeless");
    private final Identifier id;
    private final String group;
    private final DefaultedList<Ingredient> inputItems;
    private final ItemStack output;
    private final ItemStack container;
    private final int cookTime;

    public ExtremeCookingPotShapelessRecipe(Identifier id, String group, DefaultedList<Ingredient> inputItems, ItemStack output, ItemStack container, int cookTime) {
        this.id = id;
        this.group = group;
        this.inputItems = inputItems;
        this.output = output;
        if (!container.isEmpty())
            this.container = container;
        else if (output.getItem().getRecipeRemainder() != null)
            this.container = output.getItem().getRecipeRemainder().getDefaultStack();
        else
            this.container = ItemStack.EMPTY;
        this.cookTime = cookTime;
    }

    @Override
    public Identifier getId() {
        return this.id;
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        return this.inputItems;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager access) {
        return this.output;
    }

    @Override
    public ItemStack getOutputContainer() {
        return this.container;
    }

    @Override
    public ItemStack craft(Inventory inv, DynamicRegistryManager access) {
        return this.output.copy();
    }

    @Override
    public int getCookTime() {
        return this.cookTime;
    }

    @Override
    public boolean shapeless() {
        return true;
    }

    @Override
    public int width() {
        return 9;
    }

    @Override
    public int height() {
        return 9;
    }

    @Override
    public boolean matches(Inventory inv, World level) {
        RecipeMatcher stackedContents = new RecipeMatcher();
        int i = 0;

        for (int j = 0; j < 81; ++j) {
            ItemStack itemstack = inv.getStack(j);
            if (!itemstack.isEmpty()) {
                ++i;
                stackedContents.addInput(itemstack, 1);
            }
        }

        return i == this.inputItems.size() && stackedContents.match(this, null);
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= this.inputItems.size();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(ADBlocks.EXTREME_COOKING_POT.get());
    }

    public enum Type implements RecipeType<ExtremeCookingPotShapelessRecipe> {
        INSTANCE
    }

    public enum Serializer implements RecipeSerializer<ExtremeCookingPotShapelessRecipe> {
        INSTANCE;

        @Override
        public ExtremeCookingPotShapelessRecipe read(Identifier recipeId, JsonObject json) {
            String groupIn = JsonHelper.getString(json, "group", "");
            DefaultedList<Ingredient> inputItemsIn = readIngredients(JsonHelper.getArray(json, "ingredients"));
            if (inputItemsIn.isEmpty()) throw new JsonParseException("No ingredients for cooking recipe");
            else if (inputItemsIn.size() > 81)
                throw new JsonParseException("Too many ingredients for cooking recipe! The max is 81");
            else {
                ItemStack outputIn = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "result"));
                ItemStack container = JsonHelper.hasElement(json, "container") ? ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "container")) : ItemStack.EMPTY;
                int cookTimeIn = JsonHelper.getInt(json, "cookingtime", 200);
                return new ExtremeCookingPotShapelessRecipe(recipeId, groupIn, inputItemsIn, outputIn, container, cookTimeIn);
            }
        }

        private static DefaultedList<Ingredient> readIngredients(JsonArray ingredientArray) {
            DefaultedList<Ingredient> ingredients = DefaultedList.of();
            for (int i = 0; i < ingredientArray.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(ingredientArray.get(i));
                if (!ingredient.isEmpty()) ingredients.add(ingredient);
            }
            return ingredients;
        }

        @Override
        public @NotNull ExtremeCookingPotShapelessRecipe read(Identifier recipeId, PacketByteBuf buf) {
            String groupIn = buf.readString();
            DefaultedList<Ingredient> inputItemsIn = DefaultedList.ofSize(buf.readVarInt(), Ingredient.EMPTY);
            inputItemsIn.replaceAll(ignored -> Ingredient.fromPacket(buf));
            ItemStack outputIn = buf.readItemStack();
            ItemStack container = buf.readItemStack();
            int cookTimeIn = buf.readVarInt();
            return new ExtremeCookingPotShapelessRecipe(recipeId, groupIn, inputItemsIn, outputIn, container, cookTimeIn);
        }

        @Override
        public void write(PacketByteBuf buf, ExtremeCookingPotShapelessRecipe recipe) {
            buf.writeString(recipe.group);
            buf.writeVarInt(recipe.inputItems.size());
            for (Ingredient ingredient : recipe.inputItems) ingredient.write(buf);
            buf.writeItemStack(recipe.output);
            buf.writeItemStack(recipe.container);
            buf.writeVarInt(recipe.cookTime);
        }
    }
}
