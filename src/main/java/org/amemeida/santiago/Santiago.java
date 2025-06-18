package org.amemeida.santiago;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.PlacedFeature;
import org.amemeida.santiago.net.OpenScreenS2CPayload;
import org.amemeida.santiago.registry.*;
import org.amemeida.santiago.registry.blocks.ModBlockEntities;
import org.amemeida.santiago.registry.blocks.ModBlocks;
import org.amemeida.santiago.registry.blocks.ModScreenHandlers;
import org.amemeida.santiago.registry.items.ModGroups;
import org.amemeida.santiago.registry.items.ModItems;
import org.amemeida.santiago.registry.recipes.ModRecipeBooks;
import org.amemeida.santiago.registry.recipes.ModRecipeSerializers;
import org.amemeida.santiago.registry.recipes.ModRecipeTypes;

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

        PayloadTypeRegistry.playS2C().register(OpenScreenS2CPayload.ID, OpenScreenS2CPayload.CODEC);
    }
}
