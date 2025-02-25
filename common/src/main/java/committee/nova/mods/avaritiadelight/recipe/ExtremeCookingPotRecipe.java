package committee.nova.mods.avaritiadelight.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.JsonOps;
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
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab;

import java.util.EnumSet;

public class ExtremeCookingPotRecipe implements Recipe<Inventory> {
    public static final Identifier ID = Identifier.of(AvaritiaDelight.MOD_ID, "extreme_cooking_pot");
    private final Identifier id;
    private final String group;
    private final CookingPotRecipeBookTab tab;
    private final DefaultedList<Ingredient> inputItems;
    private final ItemStack output;
    private final ItemStack container;
    private final float experience;
    private final int cookTime;

    public ExtremeCookingPotRecipe(Identifier id, String group, @Nullable CookingPotRecipeBookTab tab, DefaultedList<Ingredient> inputItems, ItemStack output, ItemStack container, float experience, int cookTime) {
        this.id = id;
        this.group = group;
        this.tab = tab;
        this.inputItems = inputItems;
        this.output = output;
        if (!container.isEmpty())
            this.container = container;
        else if (output.getItem().getRecipeRemainder() != null)
            this.container = output.getItem().getRecipeRemainder().getDefaultStack();
        else
            this.container = ItemStack.EMPTY;
        this.experience = experience;
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

    public @Nullable CookingPotRecipeBookTab getRecipeBookTab() {
        return this.tab;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        return this.inputItems;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager access) {
        return this.output;
    }

    public ItemStack getOutputContainer() {
        return this.container;
    }

    @Override
    public ItemStack craft(Inventory inv, DynamicRegistryManager access) {
        return this.output.copy();
    }

    public float getExperience() {
        return this.experience;
    }

    public int getCookTime() {
        return this.cookTime;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return o instanceof ExtremeCookingPotRecipe that &&
                Float.compare(that.getExperience(), this.getExperience()) == 0 &&
                this.getCookTime() == that.getCookTime() &&
                this.getId().equals(that.getId()) &&
                this.getGroup().equals(that.getGroup()) &&
                this.tab == that.tab &&
                this.inputItems.equals(that.inputItems) &&
                this.output.equals(that.output) &&
                this.container.equals(that.container);
    }

    @Override
    public int hashCode() {
        int result = this.getId().hashCode();
        result = 31 * result + this.getGroup().hashCode();
        result = 31 * result + (this.getRecipeBookTab() != null ? this.getRecipeBookTab().hashCode() : 0);
        result = 31 * result + this.inputItems.hashCode();
        result = 31 * result + this.output.hashCode();
        result = 31 * result + this.container.hashCode();
        result = 31 * result + (this.getExperience() != 0.0F ? Float.floatToIntBits(this.getExperience()) : 0);
        result = 31 * result + this.getCookTime();
        return result;
    }

    public enum Type implements RecipeType<ExtremeCookingPotRecipe> {
        INSTANCE
    }

    public enum Serializer implements RecipeSerializer<ExtremeCookingPotRecipe> {
        INSTANCE;

        @Override
        public ExtremeCookingPotRecipe read(Identifier recipeId, JsonObject json) {
            String groupIn = JsonHelper.getString(json, "group", "");
            DefaultedList<Ingredient> inputItemsIn = readIngredients(JsonHelper.getArray(json, "ingredients"));
            if (inputItemsIn.isEmpty()) throw new JsonParseException("No ingredients for cooking recipe");
            else if (inputItemsIn.size() > 81)
                throw new JsonParseException("Too many ingredients for cooking recipe! The max is 81");
            else {
                String tabKeyIn = JsonHelper.getString(json, "recipe_book_tab", null);
                CookingPotRecipeBookTab tabIn = CookingPotRecipeBookTab.findByName(tabKeyIn);
                if (tabKeyIn != null && tabIn == null)
                    AvaritiaDelight.LOGGER.warn("Optional field 'recipe_book_tab' does not match any valid tab. If defined, must be one of the following: {}", EnumSet.allOf(CookingPotRecipeBookTab.class));

                ItemStack outputIn = ItemStack.CODEC.parse(JsonOps.INSTANCE, JsonHelper.getObject(json, "result")).resultOrPartial(AvaritiaDelight.LOGGER::error).orElse(ItemStack.EMPTY);
                ItemStack container = JsonHelper.hasElement(json, "container") ? ItemStack.CODEC.parse(JsonOps.INSTANCE, JsonHelper.getObject(json, "container")).resultOrPartial(AvaritiaDelight.LOGGER::error).orElse(ItemStack.EMPTY) : ItemStack.EMPTY;
                float experienceIn = JsonHelper.getFloat(json, "experience", 0.0F);
                int cookTimeIn = JsonHelper.getInt(json, "cookingtime", 200);
                return new ExtremeCookingPotRecipe(recipeId, groupIn, tabIn, inputItemsIn, outputIn, container, experienceIn, cookTimeIn);
            }
        }

        private static DefaultedList<Ingredient> readIngredients(JsonArray ingredientArray) {
            DefaultedList<Ingredient> nonnulllist = DefaultedList.of();
            for (int i = 0; i < ingredientArray.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(ingredientArray.get(i));
                if (!ingredient.isEmpty()) nonnulllist.add(ingredient);
            }
            return nonnulllist;
        }

        @Override
        public @NotNull ExtremeCookingPotRecipe read(Identifier recipeId, PacketByteBuf buffer) {
            String groupIn = buffer.readString();
            CookingPotRecipeBookTab tabIn = CookingPotRecipeBookTab.findByName(buffer.readString());
            int i = buffer.readVarInt();
            DefaultedList<Ingredient> inputItemsIn = DefaultedList.ofSize(i, Ingredient.EMPTY);
            inputItemsIn.replaceAll(ignored -> Ingredient.fromPacket(buffer));
            ItemStack outputIn = buffer.readItemStack();
            ItemStack container = buffer.readItemStack();
            float experienceIn = buffer.readFloat();
            int cookTimeIn = buffer.readVarInt();
            return new ExtremeCookingPotRecipe(recipeId, groupIn, tabIn, inputItemsIn, outputIn, container, experienceIn, cookTimeIn);
        }

        @Override
        public void write(PacketByteBuf buffer, ExtremeCookingPotRecipe recipe) {
            buffer.writeString(recipe.group);
            buffer.writeString(recipe.tab != null ? recipe.tab.toString() : "");
            buffer.writeVarInt(recipe.inputItems.size());
            for (Ingredient ingredient : recipe.inputItems)
                ingredient.write(buffer);
            buffer.writeItemStack(recipe.output);
            buffer.writeItemStack(recipe.container);
            buffer.writeFloat(recipe.experience);
            buffer.writeVarInt(recipe.cookTime);
        }
    }
}
