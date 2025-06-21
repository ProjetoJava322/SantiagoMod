package org.amemeida.santiago;

import net.fabricmc.api.ModInitializer;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.PlacedFeature;
import org.amemeida.santiago.registry.*;
import org.amemeida.santiago.registry.blocks.ModBlockEntities;
import org.amemeida.santiago.registry.blocks.ModBlocks;
import org.amemeida.santiago.registry.blocks.ModScreenHandlers;
import org.amemeida.santiago.registry.items.ModGroups;
import org.amemeida.santiago.registry.items.ModItems;
import org.amemeida.santiago.registry.recipes.ModRecipeBooks;
import org.amemeida.santiago.registry.recipes.ModRecipeSerializers;
import org.amemeida.santiago.registry.recipes.ModRecipeTypes;
import org.apache.commons.logging.Log;

public class Santiago implements ModInitializer {
    public static final String MOD_ID = "santiago";

    @Override
    public void onInitialize() {
        ModEntities.initialize();

        ModBlocks.initialize();
        ModBlockEntities.initialize();
        ModScreenHandlers.initialize();

        ModGroups.initialize();
        ModItems.initialize();

        ModTags.initialize();
        ModSounds.initialize();

        ModRecipeBooks.initialize();
        ModRecipeSerializers.initialize();
        ModRecipeTypes.initialize();
        ModFeatures.initialize();
    }
}
