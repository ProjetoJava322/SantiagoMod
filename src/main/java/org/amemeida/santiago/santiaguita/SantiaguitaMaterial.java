package org.amemeida.santiago.santiaguita;

import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentAsset;
import net.minecraft.item.equipment.EquipmentAssetKeys;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import org.amemeida.santiago.Santiago;

import java.util.Map;

public class SantiaguitaMaterial {
    public static final TagKey<Item> REPAIRS_SANTIAGUITA =
            TagKey.of(RegistryKeys.ITEM, Identifier.of(Santiago.MOD_ID, "repairs_santiaguita"));

    public static final ToolMaterial TOOL_MATERIAL = new ToolMaterial(
            BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
            1000,
            10,
            8,
            50,
            REPAIRS_SANTIAGUITA
    );

    public static final RegistryKey<EquipmentAsset> SANTIAGUITA_KEY = RegistryKey.of(EquipmentAssetKeys.REGISTRY_KEY,
            Identifier.of(Santiago.MOD_ID, "santiaguita"));

    public static final ArmorMaterial ARMOR_MATERIAL = new  ArmorMaterial(
            30, Map.of(
            EquipmentType.HELMET, 6,
            EquipmentType.CHESTPLATE, 9,
            EquipmentType.LEGGINGS, 7,
            EquipmentType.BOOTS, 5
    ),5, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE,0.0F, 0.0F,
            REPAIRS_SANTIAGUITA, SANTIAGUITA_KEY
    );
}
