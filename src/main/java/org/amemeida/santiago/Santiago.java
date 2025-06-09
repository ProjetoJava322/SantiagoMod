package org.amemeida.santiago;

import net.fabricmc.api.ModInitializer;
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
    }
}
