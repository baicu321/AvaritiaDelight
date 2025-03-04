package committee.nova.mods.avaritiadelight.registry;

import committee.nova.mods.avaritiadelight.AvaritiaDelight;
import committee.nova.mods.avaritiadelight.item.*;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.RegistryKeys;

import java.util.function.Supplier;

public final class ADItems {
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(AvaritiaDelight.MOD_ID, RegistryKeys.ITEM);

    public static final RegistrySupplier<Item> BLAZE_KNIFE = register("blaze_knife", () -> new AvaritiaDelightKnifeItem(ToolMaterials.GOLD));
    public static final RegistrySupplier<Item> CRYSTAL_KNIFE = register("crystal_knife", () -> new AvaritiaDelightKnifeItem(ToolMaterials.DIAMOND));
    public static final RegistrySupplier<Item> NEUTRONIUM_KNIFE = register("neutronium_knife", () -> new AvaritiaDelightKnifeItem(ToolMaterials.NETHERITE));
    public static final RegistrySupplier<Item> INFINITY_KNIFE = register("infinity_knife", () -> new AvaritiaDelightKnifeItem(ToolMaterials.NETHERITE));

    public static final RegistrySupplier<Item> BLAZE_TOMATO_SEEDS = register("blaze_tomato_seeds", () -> new AliasedBlockItem(ADBlocks.BUDDING_BLAZE_TOMATO.get(), new Item.Settings().arch$tab(ADItemGroups.MAIN)));
    public static final RegistrySupplier<Item> CRYSTAL_CABBAGE_SEEDS = register("crystal_cabbage_seeds", () -> new AliasedBlockItem(ADBlocks.CRYSTAL_CABBAGE.get(), new Item.Settings().arch$tab(ADItemGroups.MAIN)));
    public static final RegistrySupplier<Item> NEUTRONIUM_WHEAT_SEEDS = register("neutronium_wheat_seeds", () -> new AliasedBlockItem(ADBlocks.NEUTRONIUM_WHEAT.get(), new Item.Settings().arch$tab(ADItemGroups.MAIN)));

    public static final RegistrySupplier<Item> BLAZE_TOMATO = register("blaze_tomato", () -> new Item(new Item.Settings().arch$tab(ADItemGroups.MAIN).food(new FoodComponent.Builder().hunger(8).saturationModifier(0.6F).build())));
    public static final RegistrySupplier<Item> CRYSTAL_CABBAGE = register("crystal_cabbage", () -> new Item(new Item.Settings().arch$tab(ADItemGroups.MAIN).food(new FoodComponent.Builder().hunger(8).saturationModifier(0.6F).build())));
    public static final RegistrySupplier<Item> NEUTRONIUM_WHEAT = register("neutronium_wheat", () -> new Item(new Item.Settings().arch$tab(ADItemGroups.MAIN)));

    public static final RegistrySupplier<Item> BLAZE_TOMATO_SAUCE = register("blaze_tomato_sauce", () -> new Item(new Item.Settings().arch$tab(ADItemGroups.MAIN)));
    public static final RegistrySupplier<Item> COSMIC_BEEF = register("cosmic_beef", () -> new Item(new Item.Settings().arch$tab(ADItemGroups.MAIN)));
    public static final RegistrySupplier<Item> COSMIC_BEEF_COOKED = register("cosmic_beef_cooked", () -> new Item(new Item.Settings().arch$tab(ADItemGroups.MAIN)));
    public static final RegistrySupplier<Item> CRYSTAL_CABBAGE_LEAF = register("crystal_cabbage_leaf", () -> new Item(new Item.Settings().arch$tab(ADItemGroups.MAIN)));
    public static final RegistrySupplier<Item> RAW_CRYSTAL_PASTA = register("raw_crystal_pasta", () -> new Item(new Item.Settings().arch$tab(ADItemGroups.MAIN)));
    public static final RegistrySupplier<Item> DIAMOND_LATTICE_FRIES = register("diamond_lattice_fries", () -> new Item(new Item.Settings().arch$tab(ADItemGroups.MAIN)));
    public static final RegistrySupplier<Item> INFINITY_APPLE = register("infinity_apple", InfinityAppleItem::new);
    public static final RegistrySupplier<Item> INFINITY_LARGE_HAMBURGER = register("infinity_large_hamburger", () -> new Item(new Item.Settings().arch$tab(ADItemGroups.MAIN)));
    public static final RegistrySupplier<Item> INFINITY_TACO = register("infinity_taco", () -> new Item(new Item.Settings().arch$tab(ADItemGroups.MAIN)));
    public static final RegistrySupplier<Item> NEUTRONIUM_POT = register("neutronium_pot", () -> new Item(new Item.Settings().arch$tab(ADItemGroups.MAIN)));
    public static final RegistrySupplier<Item> NEUTRONIUM_WHEAT_DOUGH = register("neutronium_wheat_dough", () -> new Item(new Item.Settings().arch$tab(ADItemGroups.MAIN)));
    public static final RegistrySupplier<Item> PASTA_WITH_COSMIC_MEATBALLS = register("pasta_with_cosmic_meatballs", () -> new Item(new Item.Settings().arch$tab(ADItemGroups.MAIN)));
    public static final RegistrySupplier<Item> SLICE_OF_ENDLESS_CAKE = register("slice_of_endless_cake", SliceOfEndlessCakeItem::new);

    public static final RegistrySupplier<Item> FURIOUS_COCKTAIL = register("furious_cocktail", FuriousCocktailItem::new);
    public static final RegistrySupplier<Item> HOW_DID_WE_GET_HERE = register("how_did_we_get_here", HowDidWeGetHereItem::new);
    public static final RegistrySupplier<Item> INFINITY_MILK = register("infinity_milk", InfinityMilkItem::new);

    public static <T extends Item> RegistrySupplier<T> register(String id, Supplier<T> supplier) {
        return REGISTRY.register(id, supplier);
    }
}
