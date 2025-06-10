package org.amemeida.santiago.client.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import org.amemeida.santiago.registry.ModBlocks;
import org.amemeida.santiago.registry.ModItems;
import org.amemeida.santiago.santiaguita.SantiaguitaMaterial;

import java.util.concurrent.CompletableFuture;

public class TagGenerator extends FabricTagProvider<Item> {
    public TagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.ITEM, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(SantiaguitaMaterial.REPAIRS_SANTIAGUITA)
                .add(ModItems.SANTIAGUITA_INGOT);

        getOrCreateTagBuilder(ItemTags.PICKAXES)
                .add(ModItems.SANTIAGUITA_PICKAXE);

        getOrCreateTagBuilder(ItemTags.SHOVELS)
                .add(ModItems.SANTIAGUITA_SHOVEL);

        getOrCreateTagBuilder(ItemTags.AXES)
                .add(ModItems.SANTIAGUITA_AXE);

        getOrCreateTagBuilder(ItemTags.HOES)
                .add(ModItems.SANTIAGUITA_HOE);

        getOrCreateTagBuilder(ItemTags.SWORDS)
                .add(ModItems.SANTIAGUITA_SWORD);

        getOrCreateTagBuilder(ConventionalItemTags.ORES)
                .add(ModBlocks.SANTIAGUITA_ORE.asItem());

        getOrCreateTagBuilder(ConventionalItemTags.INGOTS)
                .add(ModItems.SANTIAGUITA_INGOT);

        getOrCreateTagBuilder(ConventionalItemTags.RODS)
                .add(ModItems.UNDERLINE);

        getOrCreateTagBuilder(ConventionalItemTags.RAW_MATERIALS)
                .add(ModItems.RAW_SANTIAGUITA);

        getOrCreateTagBuilder(ConventionalItemTags.TOOLS)
                .add(ModItems.SANTIAGUITA_PICKAXE)
                .add(ModItems.SANTIAGUITA_AXE)
                .add(ModItems.SANTIAGUITA_HOE)
                .add(ModItems.SANTIAGUITA_SHOVEL);

        getOrCreateTagBuilder(ConventionalItemTags.MELEE_WEAPON_TOOLS)
                .add(ModItems.SANTIAGUITA_SWORD)
                .add(ModItems.SANTIAGUITA_INGOT);
    }
}
