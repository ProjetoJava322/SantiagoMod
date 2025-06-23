package org.amemeida.santiago.client.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.Models;
import org.amemeida.santiago.registry.blocks.ModBlocks;
import org.amemeida.santiago.registry.items.ModItems;

public class ModelGenerator extends FabricModelProvider {
    public ModelGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator gen) {
        gen.registerSimpleCubeAll(ModBlocks.SANTIAGUITA_ORE);
        gen.registerSimpleCubeAll(ModBlocks.DEEPSLATE_SANTIAGUITA_ORE);
        gen.registerSimpleCubeAll(ModBlocks.INCUBATOR);
        gen.registerSimpleCubeAll(ModBlocks.REVOLUTION_TABLE);
        gen.registerSimpleCubeAll(ModBlocks.COMPUTER);
        gen.registerSimpleCubeAll(ModBlocks.SANTIAGUITA_BLOCK);
    }

    @Override
    public void generateItemModels(ItemModelGenerator gen) {

        gen.register(ModItems.UNDERLINE, Models.HANDHELD_ROD);

        gen.register(ModItems.SANTIAGUITA_INGOT, Models.GENERATED);
        gen.register(ModItems.SANTIAGUITA_NUGGET, Models.GENERATED);
        gen.register(ModItems.RAW_SANTIAGUITA, Models.GENERATED);

        gen.register(ModItems.SANTIAGUITA_SHOVEL, Models.HANDHELD);
        gen.register(ModItems.SANTIAGUITA_AXE, Models.HANDHELD);
        gen.register(ModItems.SANTIAGUITA_HOE, Models.HANDHELD);
        gen.register(ModItems.SANTIAGUITA_PICKAXE, Models.HANDHELD);
        gen.register(ModItems.SANTIAGUITA_SWORD, Models.HANDHELD);

        gen.register(ModItems.SANTIAGUITA_HELMET, Models.GENERATED);
        gen.register(ModItems.SANTIAGUITA_CHESTPLATE, Models.GENERATED);
        gen.register(ModItems.SANTIAGUITA_LEGGINGS, Models.GENERATED);
        gen.register(ModItems.SANTIAGUITA_BOOTS, Models.GENERATED);

        gen.register(ModItems.SUPER_SNOWBALL, Models.GENERATED);
        gen.register(ModItems.PUNCH_CARD, Models.GENERATED);
        gen.register(ModItems.ENDER_CARD, Models.GENERATED);
        gen.register(ModItems.FLOPPY_DISK, Models.GENERATED);
        gen.register(ModItems.STRIKE_TOTEM, Models.GENERATED);

        gen.register(ModItems.SANTIAGOS_ANTHEM_MUSIC_DISC, Models.GENERATED);
    }

    @Override
    public String getName() {
        return "Santiago Model Provider";
    }
}
