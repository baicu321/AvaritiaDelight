package committee.nova.mods.avaritiadelight.mixin;

import com.google.gson.JsonObject;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

@Mixin(ShapedRecipe.class)
public interface ShapedRecipeAccessor {
    @Invoker("readSymbols")
    static Map<String, Ingredient> avaritia_delight$readSymbols(JsonObject json) {
        throw new AssertionError("This method should be replaced by Mixin.");
    }

    @Invoker("removePadding")
    static String[] avaritia_delight$removePadding(String... pattern) {
        throw new AssertionError("This method should be replaced by Mixin.");
    }

    @Invoker("createPatternMatrix")
    static DefaultedList<Ingredient> avaritia_delight$createPatternMatrix(String[] pattern, Map<String, Ingredient> symbols, int width, int height) {
        throw new AssertionError("This method should be replaced by Mixin.");
    }
}
