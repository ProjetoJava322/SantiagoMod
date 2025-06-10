package org.amemeida.santiago.santiaguita;

import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.amemeida.santiago.Santiago;

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
}
