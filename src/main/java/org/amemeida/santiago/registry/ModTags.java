package org.amemeida.santiago.registry;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.amemeida.santiago.Santiago;

public class ModTags {
    public static final TagKey<Item> REPAIRS_SANTIAGUITA =
            TagKey.of(RegistryKeys.ITEM, Identifier.of(Santiago.MOD_ID, "repairs_santiaguita"));

    public static void initialize() {}
}
