package org.amemeida.santiago.santiaguita;

import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentAsset;
import net.minecraft.item.equipment.EquipmentAssetKeys;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import org.amemeida.santiago.Santiago;

import java.util.Map;

import static org.amemeida.santiago.registry.ModTags.REPAIRS_SANTIAGUITA;


public class SantiaguitaArmorMaterial {
    public static final int BASE_DURABILITY = 30;
    public static final RegistryKey<EquipmentAsset> SANTIAGUITA_ARMOR_MATERIAL_KEY = RegistryKey.of(EquipmentAssetKeys.REGISTRY_KEY,
                                                                                    Identifier.of(Santiago.MOD_ID, "santiaguita"));

    public static final ArmorMaterial INSTANCE = new  ArmorMaterial(
            BASE_DURABILITY, Map.of(
            EquipmentType.HELMET, 6,
            EquipmentType.CHESTPLATE, 9,
            EquipmentType.LEGGINGS, 7,
            EquipmentType.BOOTS, 5
    ),5, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE,0.0F, 0.0F,
            REPAIRS_SANTIAGUITA, SANTIAGUITA_ARMOR_MATERIAL_KEY
    );
}
