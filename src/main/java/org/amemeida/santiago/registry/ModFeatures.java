package org.amemeida.santiago.registry;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeature;
import org.amemeida.santiago.Santiago;

public class ModFeatures {
    public static final RegistryKey<PlacedFeature> SANTIAGUITA_ORE_KEY = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(Santiago.MOD_ID, "ore_santiaguita"));

    static {
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, SANTIAGUITA_ORE_KEY);
    }

    public static void initialize(){}
}

