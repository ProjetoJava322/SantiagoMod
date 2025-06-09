package org.amemeida.santiago.registry;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.amemeida.santiago.Santiago;

public class ModGroups {
    public static final RegistryKey<ItemGroup> DEFAULT_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(Santiago.MOD_ID, "item_group"));
    public static final ItemGroup DEFAULT_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(Items.POTATO))
            .displayName(Text.translatable("itemGroup.santiago"))
            .build();

    static {
        Registry.register(Registries.ITEM_GROUP, DEFAULT_GROUP_KEY, DEFAULT_GROUP);
    }

    public static void initialize() {}
}
