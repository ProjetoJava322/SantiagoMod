package org.amemeida.santiago.registry;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DeathProtectionComponent;
import net.minecraft.component.type.FoodComponents;
import net.minecraft.entity.mob.PillagerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import org.amemeida.santiago.Santiago;
import org.amemeida.santiago.items.PunchCard;
import org.amemeida.santiago.items.SuperSnowball;
import org.amemeida.santiago.santiaguita.SantiaguitaMaterial;

import java.util.function.Function;

public class ModItems {
    public static final Item HAMSTER = register("hamster",
            new Item.Settings().food(FoodComponents.APPLE).component(DataComponentTypes.DEATH_PROTECTION, DeathProtectionComponent.TOTEM_OF_UNDYING));
    public static final Item SANTIAGUITA_SWORD = register("santiaguita_sword",
            new Item.Settings().sword(SantiaguitaMaterial.TOOL_MATERIAL, 3.0F, -2.4F));
    public static final Item SANTIAGUITA_PICKAXE = register("santiaguita_pickaxe",
            new Item.Settings().pickaxe(SantiaguitaMaterial.TOOL_MATERIAL, 1.0F, -2.8F));
    public static final Item SANTIAGUITA_AXE = register("santiaguita_axe",
            new Item.Settings().axe(SantiaguitaMaterial.TOOL_MATERIAL, 5.0F, -3.0F));
    public static final Item SANTIAGUITA_HOE = register("santiaguita_hoe",
            new Item.Settings().hoe(SantiaguitaMaterial.TOOL_MATERIAL, 1.0F, -5.0F));
    public static final Item SANTIAGUITA_SHOVEL = register("santiaguita_shovel",
            new Item.Settings().shovel(SantiaguitaMaterial.TOOL_MATERIAL, 1.0F, -1.0F));

    public static final Item PUNCH_CARD = register("punch_card", PunchCard::new);

    public static final Item RAW_SANTIAGUITA = register("raw_santiaguita");

    public static final Item SANTIAGUITA_INGOT = register("santiaguita_ingot");
    public static final Item UNDERLINE = register("underline");

    public static final Item SUPER_SNOWBALL = register("super_snowball", SuperSnowball::new);

    protected static Item register(String name) {
         return register(name, Item::new, new Item.Settings());
    }

    protected static Item register(String name, Function<Item.Settings, Item> itemFactory) {
        return register(name, itemFactory, new Item.Settings());
    }

    protected static Item register(String name, Item.Settings settings) {
        return register(name, Item::new, settings);
    }

    protected static Item register(String name, Function<Item.Settings, Item> itemFactory, Item.Settings settings) {
        // Create the item key.
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Santiago.MOD_ID, name));

        // Create the item instance.
        Item item = itemFactory.apply(settings.registryKey(itemKey));

        ItemGroupEvents.modifyEntriesEvent(ModGroups.DEFAULT_GROUP_KEY).register(group -> {
            group.add(item);
        });

        // Register the item.
        Registry.register(Registries.ITEM, itemKey, item);

        return item;
    }

    public static void initialize() {}
}
