package org.amemeida.santiago;

import net.fabricmc.api.ModInitializer;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.PlacedFeature;
import org.amemeida.santiago.registry.*;

public class Santiago implements ModInitializer {
    public static final String MOD_ID = "santiago";

    @Override
    public void onInitialize() {
        ModEntities.initialize();
        ModBlocks.initialize();
        ModSounds.initialize();
        ModGroups.initialize();
        ModItems.initialize();
        ModTags.initialize();
        ModFeatures.initialize();

        //Branch test upstream
    }
}
