package committee.nova.mods.avaritiadelight.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import committee.nova.mods.avaritiadelight.AvaritiaDelight;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;

import java.util.LinkedList;
import java.util.List;

public class CropExtractorRecipe implements Recipe<Inventory> {
    public static final Identifier ID = Identifier.of(AvaritiaDelight.MOD_ID, "crop_extractor");
    private final Identifier id;
    private final Ingredient input;
    private final List<ItemStack> outputs;
    private final int time;

    public CropExtractorRecipe(Identifier id, Ingredient input, List<ItemStack> outputs, int time) {
        this.id = id;
        this.input = input;
        this.outputs = outputs;
        this.time = time;
    }

    @Override
    public boolean matches(Inventory inventory, World world) {
        return this.input.test(inventory.getStack(0));
    }

    @Deprecated
    @Override
    public ItemStack craft(Inventory inventory, DynamicRegistryManager registryManager) {
        return ItemStack.EMPTY;
    }

    public List<ItemStack> getOutputs() {
        return this.outputs;
    }

    public int getTime() {
        return this.time;
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height > 0;
    }

    @Deprecated
    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return ItemStack.EMPTY;
    }

    @Override
    public Identifier getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public enum Type implements RecipeType<CropExtractorRecipe> {
        INSTANCE;
    }

    public enum Serializer implements RecipeSerializer<CropExtractorRecipe> {
        INSTANCE;

        @Override
        public CropExtractorRecipe read(Identifier id, JsonObject json) {
            Ingredient input = Ingredient.fromJson(JsonHelper.getObject(json, "ingredient"));
            List<ItemStack> outputs = JsonHelper.getArray(json, "results").asList().stream().map(JsonElement::getAsJsonObject).map(ShapedRecipe::outputFromJson).toList();
            int time = JsonHelper.getInt(json, "time", 200);
            return new CropExtractorRecipe(id, input, outputs, time);
        }

        @Override
        public CropExtractorRecipe read(Identifier id, PacketByteBuf buf) {
            Ingredient input = Ingredient.fromPacket(buf);
            int outputCount = buf.readInt();
            List<ItemStack> outputs = new LinkedList<>();
            for (int i = 0; i < outputCount; i++) outputs.add(buf.readItemStack());
            int cookTimeIn = buf.readInt();
            return new CropExtractorRecipe(id, input, outputs, cookTimeIn);
        }

        @Override
        public void write(PacketByteBuf buf, CropExtractorRecipe recipe) {
            recipe.input.write(buf);
            buf.writeInt(recipe.outputs.size());
            for (ItemStack stack : recipe.outputs) buf.writeItemStack(stack);
            buf.writeInt(recipe.time);
        }
    }
}
