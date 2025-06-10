package org.amemeida.santiago.client.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import org.amemeida.santiago.registry.ModBlocks;
import org.amemeida.santiago.registry.ModItems;

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
                        .criterion(hasItem(ModItems.SANTIAGUITA_PICKAXE), conditionsFromItem(ModItems.SANTIAGUITA_PICKAXE))
                        .offerTo(exporter);

                createShaped(RecipeCategory.TOOLS, ModItems.SANTIAGUITA_AXE)
                        .pattern(" ss")
                        .pattern(" gs")
                        .pattern(" g ")
                        .input('s', ModItems.SANTIAGUITA_INGOT)
                        .input('g', ModItems.UNDERLINE)
                        .group("santiago_tools")
                        .criterion(hasItem(ModItems.SANTIAGUITA_AXE), conditionsFromItem(ModItems.SANTIAGUITA_AXE))
                        .offerTo(exporter);

                createShaped(RecipeCategory.TOOLS, ModItems.SANTIAGUITA_SHOVEL)
                        .pattern(" s ")
                        .pattern(" g ")
                        .pattern(" g ")
                        .input('s', ModItems.SANTIAGUITA_INGOT)
                        .input('g', ModItems.UNDERLINE)
                        .group("santiago_tools")
                        .criterion(hasItem(ModItems.SANTIAGUITA_SHOVEL), conditionsFromItem(ModItems.SANTIAGUITA_SHOVEL))
                        .offerTo(exporter);

                createShaped(RecipeCategory.TOOLS, ModItems.SANTIAGUITA_HOE)
                        .pattern(" ss")
                        .pattern(" g ")
                        .pattern(" g ")
                        .input('s', ModItems.SANTIAGUITA_INGOT)
                        .input('g', ModItems.UNDERLINE)
                        .group("santiago_tools")
                        .criterion(hasItem(ModItems.SANTIAGUITA_HOE), conditionsFromItem(ModItems.SANTIAGUITA_HOE))
                        .offerTo(exporter);

                createShaped(RecipeCategory.COMBAT, ModItems.SANTIAGUITA_SWORD)
                        .pattern(" s ")
                        .pattern(" s ")
                        .pattern(" g ")
                        .input('s', ModItems.SANTIAGUITA_INGOT)
                        .input('g', ModItems.UNDERLINE)
                        .group("santiago_tools")
                        .criterion(hasItem(ModItems.SANTIAGUITA_SWORD), conditionsFromItem(ModItems.SANTIAGUITA_SWORD))
                        .offerTo(exporter);

                offerSmelting(
                        List.of(Items.SLIME_BALL),
                        RecipeCategory.FOOD,
                        ModItems.HAMSTER,
                        0.1f,
                        20,
                        "smelt_slime_to_hamster"
                );

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