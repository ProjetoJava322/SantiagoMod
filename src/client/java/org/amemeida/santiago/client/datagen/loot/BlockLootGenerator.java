package org.amemeida.santiago.client.datagen.loot;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryWrapper;
import org.amemeida.santiago.registry.blocks.ModBlocks;
import org.amemeida.santiago.registry.items.ModItems;

import java.util.concurrent.CompletableFuture;

public class BlockLootGenerator extends FabricBlockLootTableProvider {
    public BlockLootGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        addDrop(ModBlocks.COMPUTER);
        addDrop(ModBlocks.INCUBATOR);
        addDrop(ModBlocks.REVOLUTION_TABLE);

        addOre(ModBlocks.SANTIAGUITA_ORE, ModItems.RAW_SANTIAGUITA);
        addOre(ModBlocks.DEEPSLATE_SANTIAGUITA_ORE, ModItems.RAW_SANTIAGUITA);
    }

    public void addOre(Block block, Item item) {
        addDrop(block, oreDrops(block, item));
    }
}
