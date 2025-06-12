package org.amemeida.santiago.client.screens;

import net.minecraft.client.gui.screen.ingame.HandledScreens;
import org.amemeida.santiago.registry.blocks.ModScreenHandlers;

public class ModScreens {
    static {
        HandledScreens.register(ModScreenHandlers.INCUBATOR_SCREEN_HANDLER, IncubatorScreen::new);
    }

    public static void initialize() {}
}
