package committee.nova.mods.avaritiadelight.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import committee.nova.mods.avaritiadelight.AvaritiaDelight;
import committee.nova.mods.avaritiadelight.mixin.ShapedRecipeAccessor;
import committee.nova.mods.avaritiadelight.registry.ADBlocks;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ExtremeCookingPotShapedRecipe implements ExtremeCookingPotRecipe {
    public static final Identifier ID = Identifier.of(AvaritiaDelight.MOD_ID, "extreme_cooking_shaped");
    private final Identifier id;
    private final String group;
    private final int width, height;
    private final DefaultedList<Ingredient> inputItems;
    private final ItemStack output;
    private final ItemStack container;
    private final int cookTime;

    public ExtremeCookingPotShapedRecipe(Identifier id, String group, int width, int height, DefaultedList<Ingredient> inputItems, ItemStack output, ItemStack container, int cookTime) {
        this.id = id;
        this.group = group;
        this.width = width;
        this.height = height;
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
        return false;
    }

    @Override
    public int width() {
        return this.width;
    }

    @Override
    public int height() {
        return this.height;
    }

    @Override
    public boolean matches(Inventory inv, World level) {
        for (int i = 0; i <= 9 - this.width; ++i)
            for (int j = 0; j <= 9 - this.height; ++j) {
                if (this.checkMatch(inv, i, j, true)) return true;
                if (this.checkMatch(inv, i, j, false)) return true;
            }
        return false;
    }

    private boolean checkMatch(Inventory inventory, int x, int y, boolean mirror) {
        for (int i = 0; i < 9; ++i)
            for (int j = 0; j < 9; ++j) {
                int k = i - x;
                int l = j - y;
                Ingredient ingredient = Ingredient.EMPTY;
                if (k >= 0 && l >= 0 && k < this.width && l < this.height)
                    ingredient = mirror ? this.inputItems.get(this.width - k - 1 + l * this.width) : this.inputItems.get(k + l * this.width);
                if (!ingredient.test(inventory.getStack(i + j * 9)))
                    return false;
            }
        return true;
    }

    @Override
    public boolean fits(int width, int height) {
        return width >= this.width && height >= this.height;
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

    public enum Type implements RecipeType<ExtremeCookingPotShapedRecipe> {
        INSTANCE
    }

    public enum Serializer implements RecipeSerializer<ExtremeCookingPotShapedRecipe> {
        INSTANCE;

        @Override
        public ExtremeCookingPotShapedRecipe read(Identifier recipeId, JsonObject json) {
            String group = JsonHelper.getString(json, "group", "");
            Map<String, Ingredient> map = ShapedRecipeAccessor.avaritia_delight$readSymbols(JsonHelper.getObject(json, "key"));
            String[] strings = ShapedRecipeAccessor.avaritia_delight$removePadding(getPattern(JsonHelper.getArray(json, "pattern")));
            int width = strings[0].length();
            int height = strings.length;
            DefaultedList<Ingredient> inputs = ShapedRecipeAccessor.avaritia_delight$createPatternMatrix(strings, map, width, height);
            ItemStack output = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "result"));
            ItemStack container = JsonHelper.hasElement(json, "container") ? ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "container")) : ItemStack.EMPTY;
            int cookTime = JsonHelper.getInt(json, "cookingtime", 200);
            return new ExtremeCookingPotShapedRecipe(recipeId, group, width, height, inputs, output, container, cookTime);
        }

        static String[] getPattern(JsonArray json) {
            String[] strings = new String[json.size()];
            if (strings.length > 9) throw new JsonSyntaxException("Invalid pattern: too many rows, 9 is maximum");
            else if (strings.length == 0) throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
            else {
                for (int i = 0; i < strings.length; ++i) {
                    String string = JsonHelper.asString(json.get(i), "pattern[" + i + "]");
                    if (string.length() > 9)
                        throw new JsonSyntaxException("Invalid pattern: too many columns, 9 is maximum");
                    if (i > 0 && strings[0].length() != string.length())
                        throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
                    strings[i] = string;
                }
                return strings;
            }
        }

        @Override
        public @NotNull ExtremeCookingPotShapedRecipe read(Identifier recipeId, PacketByteBuf buf) {
            String groupIn = buf.readString();
            int width = buf.readInt(), height = buf.readInt();
            DefaultedList<Ingredient> inputItemsIn = DefaultedList.ofSize(width * height, Ingredient.EMPTY);
            inputItemsIn.replaceAll(ignored -> Ingredient.fromPacket(buf));
            ItemStack outputIn = buf.readItemStack();
            ItemStack container = buf.readItemStack();
            int cookTimeIn = buf.readVarInt();
            return new ExtremeCookingPotShapedRecipe(recipeId, groupIn, width, height, inputItemsIn, outputIn, container, cookTimeIn);
        }

        @Override
        public void write(PacketByteBuf buf, ExtremeCookingPotShapedRecipe recipe) {
            buf.writeString(recipe.group);
            buf.writeInt(recipe.width);
            buf.writeInt(recipe.height);
            for (Ingredient ingredient : recipe.inputItems) ingredient.write(buf);
            buf.writeItemStack(recipe.output);
            buf.writeItemStack(recipe.container);
            buf.writeVarInt(recipe.cookTime);
        }
    }
}
