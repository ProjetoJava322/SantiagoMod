package org.amemeida.santiago.client.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import org.amemeida.santiago.registry.blocks.ModBlocks;
import org.amemeida.santiago.registry.items.ModItems;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RecipeGenerator extends FabricRecipeProvider {
    public RecipeGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected net.minecraft.data.recipe.RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registryLookup, RecipeExporter exporter) {
        return new net.minecraft.data.recipe.RecipeGenerator(registryLookup, exporter) {
            @Override
            public void generate() {
                createShaped(RecipeCategory.TOOLS, ModItems.SANTIAGUITA_PICKAXE)
                        .pattern("sss")
                        .pattern(" g ")
                        .pattern(" g ")
                        .input('s', ModItems.SANTIAGUITA_INGOT)
                        .input('g', ModItems.UNDERLINE)
                        .group("santiago_tools")
                        .criterion(hasItem(ModItems.SANTIAGUITA_INGOT), conditionsFromItem(ModItems.SANTIAGUITA_INGOT))
                        .offerTo(exporter);

                createShaped(RecipeCategory.TOOLS, ModItems.SANTIAGUITA_AXE)
                        .pattern(" ss")
                        .pattern(" gs")
                        .pattern(" g ")
                        .input('s', ModItems.SANTIAGUITA_INGOT)
                        .input('g', ModItems.UNDERLINE)
                        .group("santiago_tools")
                        .criterion(hasItem(ModItems.SANTIAGUITA_INGOT), conditionsFromItem(ModItems.SANTIAGUITA_INGOT))
                        .offerTo(exporter);

                createShaped(RecipeCategory.TOOLS, ModItems.SANTIAGUITA_SHOVEL)
                        .pattern(" s ")
                        .pattern(" g ")
                        .pattern(" g ")
                        .input('s', ModItems.SANTIAGUITA_INGOT)
                        .input('g', ModItems.UNDERLINE)
                        .group("santiago_tools")
                        .criterion(hasItem(ModItems.SANTIAGUITA_INGOT), conditionsFromItem(ModItems.SANTIAGUITA_INGOT))
                        .offerTo(exporter);

                createShaped(RecipeCategory.TOOLS, ModItems.SANTIAGUITA_HOE)
                        .pattern(" ss")
                        .pattern(" g ")
                        .pattern(" g ")
                        .input('s', ModItems.SANTIAGUITA_INGOT)
                        .input('g', ModItems.UNDERLINE)
                        .group("santiago_tools")
                        .criterion(hasItem(ModItems.SANTIAGUITA_INGOT), conditionsFromItem(ModItems.SANTIAGUITA_INGOT))
                        .offerTo(exporter);

                createShaped(RecipeCategory.COMBAT, ModItems.SANTIAGUITA_SWORD)
                        .pattern(" s ")
                        .pattern(" s ")
                        .pattern(" g ")
                        .input('s', ModItems.SANTIAGUITA_INGOT)
                        .input('g', ModItems.UNDERLINE)
                        .group("santiago_tools")
                        .criterion(hasItem(ModItems.SANTIAGUITA_INGOT), conditionsFromItem(ModItems.SANTIAGUITA_INGOT))
                        .offerTo(exporter);

                createShaped(RecipeCategory.COMBAT, ModItems.SANTIAGUITA_HELMET)
                        .input('#', ModItems.SANTIAGUITA_INGOT)
                        .pattern("###")
                        .pattern("# #")
                        .pattern("   ")
                        .group("santiago_tools")
                        .criterion(hasItem(ModItems.SANTIAGUITA_INGOT), conditionsFromItem(ModItems.SANTIAGUITA_INGOT))
                        .offerTo(exporter);

                createShaped(RecipeCategory.COMBAT, ModItems.SANTIAGUITA_CHESTPLATE)
                        .input('#', ModItems.SANTIAGUITA_INGOT)
                        .pattern("# #")
                        .pattern("###")
                        .pattern("###")
                        .group("santiago_tools")
                        .criterion(hasItem(ModItems.SANTIAGUITA_INGOT), conditionsFromItem(ModItems.SANTIAGUITA_INGOT))
                        .offerTo(exporter);

                createShaped(RecipeCategory.COMBAT, ModItems.SANTIAGUITA_LEGGINGS)
                        .input('#', ModItems.SANTIAGUITA_INGOT)
                        .pattern("###")
                        .pattern("# #")
                        .pattern("# #")
                        .group("santiago_tools")
                        .criterion(hasItem(ModItems.SANTIAGUITA_INGOT), conditionsFromItem(ModItems.SANTIAGUITA_INGOT))
                        .offerTo(exporter);

                createShaped(RecipeCategory.COMBAT, ModItems.SANTIAGUITA_BOOTS)
                        .input('#', ModItems.SANTIAGUITA_INGOT)
                        .pattern("   ")
                        .pattern("# #")
                        .pattern("# #")
                        .group("santiago_tools")
                        .criterion(hasItem(ModItems.SANTIAGUITA_INGOT), conditionsFromItem(ModItems.SANTIAGUITA_INGOT))
                        .offerTo(exporter);

                createShapeless(RecipeCategory.MISC, ModItems.SANTIAGUITA_NUGGET, 9)
                        .input(ModItems.SANTIAGUITA_INGOT)
                        .group("santiago")
                        .criterion(hasItem(ModItems.SANTIAGUITA_INGOT), conditionsFromItem(ModItems.SANTIAGUITA_INGOT))
                        .offerTo(exporter);

                createShapeless(RecipeCategory.MISC, ModItems.ENDER_CARD)
                        .input(ModItems.PUNCH_CARD)
                        .input(Items.ENDER_EYE)
                        .group("santiago_computer")
                        .criterion(hasItem(ModItems.PUNCH_CARD), conditionsFromItem(ModItems.PUNCH_CARD))
                        .offerTo(exporter);

                createShapeless(RecipeCategory.MISC, ModItems.ENDER_CARD)
                        .input(ModItems.ENDER_CARD)
                        .group("santiago_computer_reset")
                        .criterion(hasItem(ModItems.ENDER_CARD), conditionsFromItem(ModItems.ENDER_CARD))
                        .offerTo(exporter, "ender_card_reset");

                createShapeless(RecipeCategory.MISC, ModItems.PUNCH_CARD)
                        .input(ModItems.PUNCH_CARD)
                        .group("santiago_computer_reset")
                        .criterion(hasItem(ModItems.PUNCH_CARD), conditionsFromItem(ModItems.PUNCH_CARD))
                        .offerTo(exporter, "punch_reset");

                createShapeless(RecipeCategory.MISC, ModItems.PUNCH_CARD)
                        .input(Items.PAPER)
                        .input(ModItems.SANTIAGUITA_NUGGET)
                        .group("santiago_computer")
                        .criterion(hasItem(ModItems.PUNCH_CARD), conditionsFromItem(ModItems.PUNCH_CARD))
                        .offerTo(exporter);

                createShapeless(RecipeCategory.MISC, ModItems.FLOPPY_DISK)
                        .input(ModItems.SANTIAGUITA_INGOT, 2)
                        .group("santiago_computer")
                        .criterion(hasItem(ModItems.SANTIAGUITA_INGOT), conditionsFromItem(ModItems.SANTIAGUITA_INGOT))
                        .offerTo(exporter);

                createShapeless(RecipeCategory.MISC, ModItems.FLOPPY_DISK)
                        .input(ModItems.FLOPPY_DISK)
                        .group("santiago_computer_reset")
                        .criterion(hasItem(ModItems.FLOPPY_DISK), conditionsFromItem(ModItems.FLOPPY_DISK))
                        .offerTo(exporter, "floppy_reset");

                offerSmelting(
                        List.of(ModItems.RAW_SANTIAGUITA, ModBlocks.SANTIAGUITA_ORE.asItem(),
                                ModBlocks.DEEPSLATE_SANTIAGUITA_ORE.asItem()),
                        RecipeCategory.MISC,
                        ModItems.SANTIAGUITA_INGOT,
                        2f,
                        300,
                        "santiaguita_ingot_from_smelting_raw_santiaguita"
                );

                offerBlasting(
                        List.of(ModItems.RAW_SANTIAGUITA, ModBlocks.SANTIAGUITA_ORE.asItem(),
                                ModBlocks.DEEPSLATE_SANTIAGUITA_ORE.asItem()),
                        RecipeCategory.MISC,
                        ModItems.SANTIAGUITA_INGOT,
                        2f,
                        150,
                        "santiaguita_ingot_from_blasting_raw_santiaguita"
                );
            }
        };
    }

    @Override
    public String getName() {
        return "RecipeGenerator";
    }


}