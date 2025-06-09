package org.amemeida.santiago.client.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.Models;
import org.amemeida.santiago.registry.ModBlocks;
import org.amemeida.santiago.registry.ModItems;

public class ModelGenerator extends FabricModelProvider {
    public ModelGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator gen) {
        gen.registerSimpleCubeAll(ModBlocks.CAT_BLOCK);
        gen.registerSimpleCubeAll(ModBlocks.MIKU_BLOCK);
        gen.registerSimpleCubeAll(ModBlocks.CREATURE_BLOCK);
    }

    @Override
    public void generateItemModels(ItemModelGenerator gen) {
        gen.register(ModItems.HAMSTER, Models.GENERATED);

        gen.register(ModItems.UNDERLINE, Models.HANDHELD_ROD);

        gen.register(ModItems.SANTIAGUITA_INGOT, Models.GENERATED);
        gen.register(ModItems.SANTIAGUITA_SHOVEL, Models.HANDHELD);
        gen.register(ModItems.SANTIAGUITA_AXE, Models.HANDHELD);
        gen.register(ModItems.SANTIAGUITA_HOE, Models.HANDHELD);
        gen.register(ModItems.SANTIAGUITA_PICKAXE, Models.HANDHELD);
        gen.register(ModItems.SANTIAGUITA_SWORD, Models.HANDHELD);

        gen.register(ModItems.SUPER_SNOWBALL, Models.GENERATED);
    }

    @Override
    public String getName() {
        return "Santiago Model Provider";
    }
}