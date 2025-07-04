package org.amemeida.santiago.client.screens;

import net.minecraft.client.gui.screen.ingame.HandledScreens;
import org.amemeida.santiago.registry.blocks.ModScreenHandlers;

/**
 * @see net.minecraft.client.gui.screen.ingame.BookEditScreen
 */

/**
 * Registra as telas (screens) dos containers personalizados do mod.
 */
public class ModScreens {
    static {
        HandledScreens.register(ModScreenHandlers.INCUBATOR_SCREEN_HANDLER, IncubatorScreen::new);
        HandledScreens.register(ModScreenHandlers.REVOLUTION_TABLE_SCREEN_HANDLER, RevolutionTableScreen::new);
        HandledScreens.register(ModScreenHandlers.COMPUTER_SCREEN_HANDLER, ComputerScreen::new);
    }

    /**
     * Inicializa o registro das telas.
     * Método vazio, apenas para forçar a classe a ser carregada.
     */
    public static void initialize() {}
}

