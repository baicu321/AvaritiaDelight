package committee.nova.mods.avaritiadelight.registry;

import committee.nova.mods.avaritiadelight.AvaritiaDelight;
import committee.nova.mods.avaritiadelight.item.AvaritiaDelightKnifeItem;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.RegistryKeys;

import java.util.function.Supplier;

public final class ADItems {
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(AvaritiaDelight.MOD_ID, RegistryKeys.ITEM);

    public static final RegistrySupplier<Item> BLAZE_KNIFE = register("blaze_knife", () -> new AvaritiaDelightKnifeItem(ToolMaterials.GOLD));
    public static final RegistrySupplier<Item> CRYSTAL_KNIFE = register("crystal_knife", () -> new AvaritiaDelightKnifeItem(ToolMaterials.DIAMOND));
    public static final RegistrySupplier<Item> INFINITY_KNIFE = register("infinity_knife", () -> new AvaritiaDelightKnifeItem(ToolMaterials.NETHERITE));
    public static final RegistrySupplier<Item> NEUTRONIUM_KNIFE = register("neutronium_knife", () -> new AvaritiaDelightKnifeItem(ToolMaterials.NETHERITE));

    public static final RegistrySupplier<Item> BLAZE_TOMATO_SAUCE = register("blaze_tomato_sauce", () -> new Item(new Item.Settings().arch$tab(ADItemGroups.MAIN)));
    public static final RegistrySupplier<Item> COSMIC_BEEF_PATTY = register("cosmic_beef_patty", () -> new Item(new Item.Settings().arch$tab(ADItemGroups.MAIN)));
    public static final RegistrySupplier<Item> COSMIC_MINCED_BEEF = register("cosmic_minced_beef", () -> new Item(new Item.Settings().arch$tab(ADItemGroups.MAIN)));
    public static final RegistrySupplier<Item> CRYSTAL_CABBAGE_LEAF = register("crystal_cabbage_leaf", () -> new Item(new Item.Settings().arch$tab(ADItemGroups.MAIN)));
    public static final RegistrySupplier<Item> RAW_CRYSTAL_PASTA = register("raw_crystal_pasta", () -> new Item(new Item.Settings().arch$tab(ADItemGroups.MAIN)));
    public static final RegistrySupplier<Item> DIAMOND_LATTICE_FRIES = register("diamond_lattice_fries", () -> new Item(new Item.Settings().arch$tab(ADItemGroups.MAIN)));
    public static final RegistrySupplier<Item> INFINITY_APPLE = register("infinity_apple", () -> new Item(new Item.Settings().arch$tab(ADItemGroups.MAIN)));
    public static final RegistrySupplier<Item> INFINITY_LARGE_HAMBURGER = register("infinity_large_hamburger", () -> new Item(new Item.Settings().arch$tab(ADItemGroups.MAIN)));
    public static final RegistrySupplier<Item> INFINITY_TACO = register("infinity_taco", () -> new Item(new Item.Settings().arch$tab(ADItemGroups.MAIN)));
    public static final RegistrySupplier<Item> NEUTRONIUM_POT = register("neutronium_pot", () -> new Item(new Item.Settings().arch$tab(ADItemGroups.MAIN)));
    public static final RegistrySupplier<Item> NEUTRONIUM_WHEAT_DOUGH = register("neutronium_wheat_dough", () -> new Item(new Item.Settings().arch$tab(ADItemGroups.MAIN)));
    public static final RegistrySupplier<Item> PASTA_WITH_COSMIC_MEATBALLS = register("pasta_with_cosmic_meatballs", () -> new Item(new Item.Settings().arch$tab(ADItemGroups.MAIN)));
    public static final RegistrySupplier<Item> SLICE_OF_ENDLESS_CAKE = register("slice_of_endless_cake", () -> new Item(new Item.Settings().arch$tab(ADItemGroups.MAIN)));

    public static <T extends Item> RegistrySupplier<T> register(String id, Supplier<T> supplier) {
        return REGISTRY.register(id, supplier);
    }
}
