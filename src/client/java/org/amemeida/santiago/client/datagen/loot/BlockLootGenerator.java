package org.amemeida.santiago.client.datagen.loot;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.registry.RegistryWrapper;
import org.amemeida.santiago.registry.ModBlocks;

import java.util.concurrent.CompletableFuture;

public class BlockLootGenerator extends FabricBlockLootTableProvider {
    public BlockLootGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        addDrop(ModBlocks.CAT_BLOCK);
        addDrop(ModBlocks.CREATURE_BLOCK);
        addDrop(ModBlocks.MIKU_BLOCK);
    }
}
