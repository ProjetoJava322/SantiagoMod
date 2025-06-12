package org.amemeida.santiago.registry.blocks;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import org.amemeida.santiago.Santiago;
import org.amemeida.santiago.santiaguita.encubadora.IncubatorScreenHandler;

public class ModScreenHandlers {
    public static final ScreenHandlerType<IncubatorScreenHandler> INCUBATOR_SCREEN_HANDLER = register("incubator",
            IncubatorScreenHandler::new);

    public static <T extends ScreenHandler> ScreenHandlerType<T> register(String name, ScreenHandlerType.Factory<T> factory) {
        return Registry.register(Registries.SCREEN_HANDLER, Identifier.of(Santiago.MOD_ID, name),
                new ScreenHandlerType<>(factory, FeatureFlags.VANILLA_FEATURES));
    }

    public static void initialize() {}
}
