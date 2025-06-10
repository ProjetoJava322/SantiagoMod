package org.amemeida.santiago.client.datagen.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import org.amemeida.santiago.registry.ModBlocks;

import java.util.concurrent.CompletableFuture;

import static net.minecraft.registry.RegistryKeys.*;

public class BlockTagGenerator extends FabricTagProvider<Block> {
    public BlockTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, BLOCK, registriesFuture);
    }

    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
                .add(ModBlocks.SANTIAGUITA_ORE)
                .add(ModBlocks.DEEPSLATE_SANTIAGUITA_ORE);
    }
}
