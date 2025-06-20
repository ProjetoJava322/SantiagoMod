package org.amemeida.santiago.registry.items;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DeathProtectionComponent;
import net.minecraft.component.type.FoodComponents;
import net.minecraft.item.Item;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import org.amemeida.santiago.Santiago;
import org.amemeida.santiago.components.IOComponent;
import org.amemeida.santiago.components.ScriptComponent;
import org.amemeida.santiago.items.FloppyDisk;
import org.amemeida.santiago.items.PunchCard;
import org.amemeida.santiago.items.SuperSnowball;
import org.amemeida.santiago.santiaguita.SantiaguitaMaterial;
import org.amemeida.santiago.registry.ModSounds;

import java.util.function.Function;

/**
 * @see net.minecraft.item.Items
 */

public class ModItems {
    public static final Item HAMSTER = register("hamster",
            new Item.Settings().food(FoodComponents.APPLE)
                    .component(DataComponentTypes.DEATH_PROTECTION, DeathProtectionComponent.TOTEM_OF_UNDYING));
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

    public static final Item PUNCH_CARD = register("punch_card", PunchCard::new, new Item.Settings()
            .maxCount(1).component(ModComponents.IO, new IOComponent("")));
    public static final Item FLOPPY_DISK = register("floppy_disk", FloppyDisk::new, new Item.Settings()
            .maxCount(1).component(ModComponents.SCRIPT, new ScriptComponent()));

    public static final Item RAW_SANTIAGUITA = register("raw_santiaguita");

    public static final Item SANTIAGUITA_INGOT = register("santiaguita_ingot");
    public static final Item UNDERLINE = register("underline");

    public static final Item SUPER_SNOWBALL = register("super_snowball", SuperSnowball::new);

    public static final Item SANTIAGOS_ANTHEM_MUSIC_DISC = register("santiagos_anthem_music_disc", new Item.Settings().jukeboxPlayable(ModSounds.SANTIAGOS_ANTHEM_KEY).maxCount(1));

    public static final Item SANTIAGUITA_HELMET = register("santiaguita_helmet", new Item.Settings()
            .armor(SantiaguitaMaterial.ARMOR_MATERIAL, EquipmentType.HELMET));
    public static final Item SANTIAGUITA_CHESTPLATE = register("santiaguita_chestplate", new Item.Settings()
            .armor(SantiaguitaMaterial.ARMOR_MATERIAL, EquipmentType.CHESTPLATE));
    public static final Item SANTIAGUITA_LEGGINGS = register("santiaguita_leggings", new Item.Settings()
            .armor(SantiaguitaMaterial.ARMOR_MATERIAL, EquipmentType.LEGGINGS));
    public static final Item SANTIAGUITA_BOOTS = register("santiaguita_boots", new Item.Settings()
            .armor(SantiaguitaMaterial.ARMOR_MATERIAL, EquipmentType.BOOTS));

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

        ItemGroupEvents.modifyEntriesEvent(ModGroups.DEFAULT_GROUP_KEY).register(group -> group.add(item));

        // Register the item.
        Registry.register(Registries.ITEM, itemKey, item);

        return item;
    }

    public static void initialize() {}
}
